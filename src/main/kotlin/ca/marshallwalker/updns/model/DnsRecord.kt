package ca.marshallwalker.updns.model

data class DnsRecord(
    val id: String,
    var name: String,
    var type: String,
    var content: String,
    var proxied: Boolean
)