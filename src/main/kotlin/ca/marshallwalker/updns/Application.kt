package ca.marshallwalker.updns

import ca.marshallwalker.updns.config.Config
import ca.marshallwalker.updns.ext.logger
import ca.marshallwalker.updns.service.AddressCheckService
import ca.marshallwalker.updns.service.CloudFlareService
import ca.marshallwalker.updns.task.ZoneCheckTask
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Application

fun main() {
    Application.run {
        logger.info("Starting UpDNS...")

        val yamlObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val configFile = File("config.yml")

        if (!configFile.exists()) {
            logger.warn("Edit config.yml and restart application.")

            configFile.createNewFile()
            val resource = Application::class.java.getResourceAsStream("/config.yml")

            resource.use { input ->
                configFile.outputStream().use { input.copyTo(it) }
            }

            System.exit(0)
            return
        }

        logger.info("Creating Apache http client")
        val httpClient = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                }
            }
        }

        logger.info("Loading configuration")
        val (cloudflare, checker) = yamlObjectMapper.readValue<Config>(configFile)

        logger.info("Creating CloudFlare Service")
        val cloudFlareService = CloudFlareService(httpClient, cloudflare)

        logger.info("Creating AddressCheck Service")
        val addressCheckService = AddressCheckService(httpClient, checker)

        logger.info("Starting zone check task")
        val scheduler = Executors.newSingleThreadScheduledExecutor()
        val zoneCheckTask = ZoneCheckTask(addressCheckService, cloudFlareService, cloudflare)
        scheduler.scheduleAtFixedRate(zoneCheckTask, checker.frequency, checker.frequency, TimeUnit.MINUTES)
    }
}