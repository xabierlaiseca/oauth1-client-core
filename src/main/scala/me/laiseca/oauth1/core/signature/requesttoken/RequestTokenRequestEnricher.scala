package me.laiseca.oauth1.core.signature.requesttoken

import me.laiseca.oauth1.core.model.{CallbackParameter, ConsumerKeyParameter, EnrichedRequest, NonceParameter, OAuth1Parameters, Request, SignatureAlgorithm, SignatureMethodParameter, TimestampParameter, VersionParameter}

class RequestTokenRequestEnricher(nonceGenerator: () => String,
                                  timestamper: () => Long = () => System.currentTimeMillis() / 1000) {
  def enrich(request: Request, consumerKey: String, callback: String, algorithm: SignatureAlgorithm): EnrichedRequest =
    EnrichedRequest(
      baseParameters(algorithm) ++ additionalParameters(consumerKey, callback),
      request
    )

  private[this] def additionalParameters(consumerKey: String, callback: String): OAuth1Parameters =
    List(
      ConsumerKeyParameter -> consumerKey,
      CallbackParameter -> callback
    )

  private[this] def baseParameters(algorithm: SignatureAlgorithm): OAuth1Parameters =
    List(
      NonceParameter -> nonceGenerator(),
      VersionParameter -> "1.0",
      SignatureMethodParameter -> algorithm.name,
      TimestampParameter -> timestamper().toString
    )
}
