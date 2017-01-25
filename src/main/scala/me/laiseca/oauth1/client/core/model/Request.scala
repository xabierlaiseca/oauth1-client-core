package me.laiseca.oauth1.client.core.model

import java.net.URI

case class Request(method: String, url: String, queryParameters: Parameters, formParameters: Parameters)

private[oauth1] case class OAuth1Request(original: Request, oAuth1Parameters: OAuth1Parameters) {

  val baseUrl: String = {
    val uri = new URI(original.url)
    val scheme = uri.getScheme.toLowerCase()
    val host = uri.getHost.toLowerCase()

    Some(uri.getPort)
      .filterNot( isStandardPort(_, scheme) )
      .map ( port => s"$scheme://${uri.getHost.toLowerCase()}:$port${uri.getPath}" )
      .getOrElse(s"$scheme://$host${uri.getPath}")
  }

  private[this] def isStandardPort(port: Int, scheme: String) =
    (port < 0) || (port == 80 && scheme == "http") || (port == 443 && scheme == "https")
}
