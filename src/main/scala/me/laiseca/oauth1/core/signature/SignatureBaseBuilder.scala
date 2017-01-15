package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{EnrichedRequest, Parameters}
import me.laiseca.oauth1.core.util.UrlEncode

class SignatureBaseBuilder {

  def build(request: EnrichedRequest): String = concat {
    List(
      UrlEncode.encode(request.method),
      UrlEncode.encode(request.baseUrl),
      buildParametersString(request))
  }

  private[this] def buildParametersString(request: EnrichedRequest): String =
    (extractParameters _ andThen
      normalize andThen
      (UrlEncode.encode(_))
    )(request)

  private[this] def extractParameters(request: EnrichedRequest): Parameters =
    request.oAuth1Parameters.map { case (key, value) => key.name -> value } ++
      request.queryParameters ++
      request.formParameters

  private[this] def normalize(parameters: Parameters): String = concat {
    parameters
      .sorted
      .map { case (key, value) => s"${UrlEncode.encode(key)}=${UrlEncode.encode(value)}" }
  }

  private[this] def concat(items: List[String]): String = items.mkString("&")
}
