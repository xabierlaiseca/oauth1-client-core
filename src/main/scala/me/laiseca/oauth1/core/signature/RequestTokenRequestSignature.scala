package me.laiseca.oauth1.core.signature

import java.util.UUID

import me.laiseca.oauth1.core.model.{Headers, OAuth1Parameters, OAuth1Request, Request, SignatureAlgorithm, SignatureParameter}

class RequestTokenRequestSignature private[signature](consumerKey: String,
                                                      consumerSecret: String,
                                                      callback: String,
                                                      algorithm: SignatureAlgorithm,
                                                      nonceGenerator: () => String,
                                                      timestamper: () => Long) {

  def this(consumerKey: String,
           consumerSecret: String,
           callback: String,
           algorithm: SignatureAlgorithm) {
    this(
      consumerKey = consumerKey,
      consumerSecret = consumerSecret,
      callback = callback,
      algorithm = algorithm,
      nonceGenerator = () => UUID.randomUUID().toString,
      timestamper = () => System.currentTimeMillis() / 1000)
  }

  def signature(request: Request): Headers =
    (emptyOAuthRequest _ andThen
      addBaseParameters andThen
      addRequestTokenParameters andThen
      addSignatureParameter andThen
      buildSignatureHeaders) (request)

  private[this] def emptyOAuthRequest(request: Request): OAuth1Request = OAuth1Request(request, Nil)

  private[this] def addBaseParameters(request: OAuth1Request): OAuth1Request =
    addParameters(request, baseParametersGenerator())

  private[this] def baseParametersGenerator(): OAuth1Parameters =
    OAuth1ParametersBuilder.base(nonceGenerator, timestamper, algorithm)

  private[this] def addRequestTokenParameters(request: OAuth1Request): OAuth1Request =
    addParameters(request, OAuth1ParametersBuilder.requestToken(consumerKey, callback))

  private[this] def addSignatureParameter(request: OAuth1Request): OAuth1Request =
    addParameters(request, List(
      SignatureParameter -> algorithm.digest(SignatureBaseBuilder.build(request), Some(consumerSecret), None)
    ))

  private[this] def buildSignatureHeaders(request: OAuth1Request): Headers =
    SignatureHeadersBuilder.build(request.oAuth1Parameters)

  private[this] def addParameters(request: OAuth1Request, parameters: OAuth1Parameters): OAuth1Request =
    request.copy(oAuth1Parameters = parameters ::: request.oAuth1Parameters)
}
