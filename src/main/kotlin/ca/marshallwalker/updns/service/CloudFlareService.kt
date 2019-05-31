package ca.marshallwalker.updns.service

import ca.marshallwalker.updns.config.cloudflare.CloudFlareConfig
import ca.marshallwalker.updns.model.CloudFlareResult
import ca.marshallwalker.updns.model.DnsRecord
import ca.marshallwalker.updns.model.Zone
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CloudFlareService(
    private val httpClient: HttpClient,
    private val config: CloudFlareConfig
) {
    suspend fun getZones() =
        httpClient.get<CloudFlareResult<Zone>>("${config.api}zones") {
            header("X-Auth-Email", config.credentials.email)
            header("X-Auth-Key", config.credentials.key)
        }.result

    suspend fun getZoneDnsRecords(zoneId: String) =
        httpClient.get<CloudFlareResult<DnsRecord>>("${config.api}zones/$zoneId/dns_records") {
            header("X-Auth-Email", config.credentials.email)
            header("X-Auth-Key", config.credentials.key)
        }.result

    suspend fun updateZoneDnsRecord(zoneId: String, record: DnsRecord) {
        httpClient.put<Unit>("${config.api}zones/$zoneId/dns_records/${record.id}") {
            header("X-Auth-Email", config.credentials.email)
            header("X-Auth-Key", config.credentials.key)
            contentType(ContentType.Application.Json)
            body = record
        }
    }
}

