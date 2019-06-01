package ca.marshallwalker.updns.task

import ca.marshallwalker.updns.config.cloudflare.CloudFlareConfig
import ca.marshallwalker.updns.ext.logger
import ca.marshallwalker.updns.service.AddressCheckService
import ca.marshallwalker.updns.service.CloudFlareService
import kotlinx.coroutines.runBlocking

class ZoneCheckTask(
    private val addressCheckService: AddressCheckService,
    private val cloudFlareService: CloudFlareService,
    private val cloudFlareConfig: CloudFlareConfig
) : Runnable {
    override fun run() = runBlocking {
        logger.info("Checking zones...")
        val address = addressCheckService.getAddress()
        logger.info("address: $address")

        val zones = cloudFlareService.getZones()
        var dirty = false

        for (zone in zones) {
            val zoneConfig = cloudFlareConfig.zones
                .find { it.name == zone.name } ?: continue

            val records = cloudFlareService.getZoneDnsRecords(zone.id)
                .filter { dnsRecord -> zoneConfig.records.any { dnsRecord.name.contains(it) } }

            logger.info("Checking zone \"${zone.name}\"")

            for (record in records) {
                if (record.content == address) continue

                logger.info("Updating record \"${record.name}\" in zone \"${zone.name}\"")

                dirty = true
                record.content = address
                cloudFlareService.updateZoneDnsRecord(zone.id, record)
            }
        }

        if (!dirty) {
            logger.info("No records updated.")
        }
    }
}