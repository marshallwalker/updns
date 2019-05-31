package ca.marshallwalker.updns.config.cloudflare

data class ZoneConfig(
    val name: String,
    val records: List<String>
)