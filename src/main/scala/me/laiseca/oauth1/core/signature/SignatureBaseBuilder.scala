package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{OAuth1Request, Parameters}
import me.laiseca.oauth1.core.util.UrlEncode

object SignatureBaseBuilder {

  def build(request: OAuth1Request): String = concat {
    List(
      UrlEncode.encode(request.original.method),
      UrlEncode.encode(request.baseUrl),
      buildParametersString(request))
  }

  private[this] def buildParametersString(request: OAuth1Request): String =
    (extractParameters _ andThen
      normalize andThen
      (UrlEncode.encode(_))
    )(request)

  private[this] def extractParameters(request: OAuth1Request): Parameters =
    request.oAuth1Parameters.map { case (key, value) => key.name -> value } ++
      request.original.queryParameters ++
      request.original.formParameters

  private[this] def normalize(parameters: Parameters): String = concat {
    parameters
      .sorted
      .map { case (key, value) => s"${UrlEncode.encode(key)}=${UrlEncode.encode(value)}" }
  }

  private[this] def concat(items: List[String]): String = items.mkString("&")
}
