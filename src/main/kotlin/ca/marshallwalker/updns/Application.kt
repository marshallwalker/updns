package ca.marshallwalker.updns

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

class Application

fun main() {
    val yamlObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
    val jsonObjectMapper = jacksonObjectMapper()

    val configFile = File("config.yml")

    if (!configFile.exists()) {
        println("Please edit config.yml then restart the application.")

        configFile.createNewFile()
        val resource = Application::class.java.getResourceAsStream("/config.yml")

        resource.use { input ->
            configFile.outputStream().use { input.copyTo(it) }
        }

        System.exit(0)
        return
    }

    val (cloudflare) = yamlObjectMapper.readValue<Config>(configFile)
    val cloudFlareService = CloudFlareService(jsonObjectMapper, cloudflare)
}