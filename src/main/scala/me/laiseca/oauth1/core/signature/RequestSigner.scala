package me.laiseca.oauth1.core.signature

import java.util.UUID

import me.laiseca.oauth1.core.model.{Headers, OAuth1Parameters, OAuth1Request, Request, SignatureAlgorithm, SignatureParameter}

class RequestSigner private[signature](consumerSecret: String,
                                       algorithm: SignatureAlgorithm,
                                       nonceGenerator: () => String = () => UUID.randomUUID().toString,
                                       timestamper: () => Long = () => System.currentTimeMillis() / 1000) {

  def this(consumerSecret: String, algorithm: SignatureAlgorithm) =
    this(
      consumerSecret = consumerSecret,
      algorithm = algorithm,
      nonceGenerator = () => UUID.randomUUID().toString,
      timestamper = () => System.currentTimeMillis() / 1000)

  def sign(request: Request, additionalParameters: OAuth1Parameters): Headers =
    (emptyOAuthRequest _ andThen
      addParameters(additionalParameters) andThen
      addSignatureParameter andThen
      buildSignatureHeaders) (request)


  private[this] def emptyOAuthRequest(request: Request): OAuth1Request = OAuth1Request(request, Nil)

  private[this] def addParameters(additionalParameters: OAuth1Parameters)(request: OAuth1Request): OAuth1Request =
    addParameters(request, baseParametersGenerator() ++ additionalParameters)

  private[this] def baseParametersGenerator(): OAuth1Parameters =
    OAuth1ParametersBuilder.base(nonceGenerator, timestamper, algorithm)

  private[this] def addSignatureParameter(request: OAuth1Request): OAuth1Request =
    addParameters(request, List(
      SignatureParameter -> algorithm.digest(SignatureBaseBuilder.build(request), Some(consumerSecret), None)
    ))

  private[this] def buildSignatureHeaders(request: OAuth1Request): Headers =
    SignatureHeadersBuilder.build(request.oAuth1Parameters)

  private[this] def addParameters(request: OAuth1Request, parameters: OAuth1Parameters): OAuth1Request =
    request.copy(oAuth1Parameters = parameters ::: request.oAuth1Parameters)
}
