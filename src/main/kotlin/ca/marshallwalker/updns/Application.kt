package ca.marshallwalker.updns

import ca.marshallwalker.updns.config.Config
import ca.marshallwalker.updns.ext.logger
import ca.marshallwalker.updns.service.CloudFlareService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

object Application

fun main() {
    Application.run {
        logger.info("Starting updns...")

        val yamlObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val jsonObjectMapper = jacksonObjectMapper()

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

        logger.info("Loading config.yml...")
        val (cloudflare) = yamlObjectMapper.readValue<Config>(configFile)
        val cloudFlareService = CloudFlareService(jsonObjectMapper, cloudflare)
    }
}