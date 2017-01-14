package me.laiseca.oauth1.core.model

abstract class SignatureAlgorithm(val name: String)
case object HmacSHA1 extends SignatureAlgorithm("HMAC-SHA1")
case object HmacSHA256 extends SignatureAlgorithm("HMAC-SHA256")
