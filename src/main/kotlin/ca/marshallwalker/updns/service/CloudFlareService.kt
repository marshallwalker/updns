package ca.marshallwalker.updns.service

import ca.marshallwalker.updns.config.CloudFlareConfig
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import org.apache.http.HttpResponse

class CloudFlareService(
    private val jsonObjectMapper: ObjectMapper,
    config: CloudFlareConfig
) {
    private val client = HttpClient(Apache) {
        defaultRequest {
            header("X-Auth-Email", config.email)
            header("X-Auth-Key", config.key)
        }
    }

    suspend fun getZone(zoneId: String) {
        client.get<HttpResponse>()
    }
}