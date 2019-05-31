package ca.marshallwalker.updns.config.cloudflare

data class CloudFlareConfig(
    val api: String,
    val credentials: CloudFlareCredentials,
    val zones: List<ZoneConfig>
)