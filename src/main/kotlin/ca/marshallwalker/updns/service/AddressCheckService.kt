package ca.marshallwalker.updns.service

import ca.marshallwalker.updns.config.check.CheckConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class AddressCheckService(
    private val httpClient: HttpClient,
    private val config: CheckConfig
) {
    suspend fun getAddress() =
        httpClient.get<String>(config.url).replace("\n", "")
}