package ca.marshallwalker.updns.task

import ca.marshallwalker.updns.config.cloudflare.ZoneConfig
import ca.marshallwalker.updns.ext.logger
import ca.marshallwalker.updns.service.AddressCheckService
import ca.marshallwalker.updns.service.CloudFlareService
import kotlinx.coroutines.runBlocking

class CheckAddressTask(
    private val addressCheckService: AddressCheckService,
    private val cloudFlareService: CloudFlareService,
    private val zoneConfigs: List<ZoneConfig>
) : Runnable {
    override fun run() = runBlocking {
        logger.info("Checking zones...")

        val address = addressCheckService.getAddress()
        val zones = cloudFlareService.getZones()
        logger.info("Current Address $address")

        var dirty = false

        for (zone in zones) {
            val dnsRecords = cloudFlareService.getZoneDnsRecords(zone.id)
            val zoneConfig = zoneConfigs.find { it.name == zone.name } ?: continue

            logger.info("Checking zone \"${zone.name}\"")

            val records = dnsRecords
                .filter { dnsRecord -> zoneConfig.records.any { dnsRecord.name.contains(it) } }

            for (record in records) {
                if (record.content == address) continue

                logger.info("Updating record \"${record.name}\" in zone \"${zone.name}\" old address \"${record.content}\" new address \"$address\"")

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