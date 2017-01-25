package me.laiseca.oauth1.client.core.signature

import me.laiseca.oauth1.client.core.model.{Headers, OAuth1Parameters}
import me.laiseca.oauth1.client.core.util.UrlEncode

object SignatureHeadersBuilder {
  def build(parameters: OAuth1Parameters): Headers =
  List("Authorization" -> s"OAuth ${parametersString(parameters)}")

  private[this] def parametersString(parameters: OAuth1Parameters): String =
    parameters.map { case (parameter, value) =>
      s"""${UrlEncode.encode(parameter.name)}=\"${UrlEncode.encode(value)}\""""
    }
      .sorted
      .mkString(", ")
}
