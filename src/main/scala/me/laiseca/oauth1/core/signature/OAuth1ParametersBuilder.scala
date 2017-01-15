package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{CallbackParameter, ConsumerKeyParameter, NonceParameter, OAuth1Parameters, SignatureAlgorithm, SignatureMethodParameter, TimestampParameter, VersionParameter}

object OAuth1ParametersBuilder {

  def base(nonceGenerator: () => String, timestamper: () => Long)
          (algorithm: SignatureAlgorithm): OAuth1Parameters =
    List(
      NonceParameter -> nonceGenerator(),
      VersionParameter -> "1.0",
      SignatureMethodParameter -> algorithm.name,
      TimestampParameter -> timestamper().toString
    )

  def requestToken(consumerKey: String, callback: String): OAuth1Parameters =
    List(
      ConsumerKeyParameter -> consumerKey,
      CallbackParameter -> callback
    )
}
