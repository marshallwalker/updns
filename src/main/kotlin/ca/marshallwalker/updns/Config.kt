package ca.marshallwalker.updns

data class Config(
    val cloudflare: CloudFlareConfig
) {
    data class CloudFlareConfig(
        val email: String,
        val key: String
    )
}