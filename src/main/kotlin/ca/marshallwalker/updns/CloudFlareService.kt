package ca.marshallwalker.updns

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.header

class CloudFlareService(
    private val jsonObjectMapper: ObjectMapper,
    config: Config.CloudFlareConfig
) {
    private val client = HttpClient(Apache) {
        defaultRequest {
            header("X-Auth-Email", config.email)
            header("X-Auth-Key", config.key)
        }
    }

}