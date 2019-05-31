package ca.marshallwalker.updns.config

import ca.marshallwalker.updns.config.check.CheckConfig
import ca.marshallwalker.updns.config.cloudflare.CloudFlareConfig

data class Config(
    val cloudflare: CloudFlareConfig,
    val checker: CheckConfig
)