package me.laiseca.oauth1.core.model

import java.net.URI

class Request(val method: String, val url: String, val queryParameters: Parameters, val formParameters: Parameters)

private[oauth1] case class EnrichedRequest(oAuth1Parameters: OAuth1Parameters,
                                           private val r: Request
                                          ) extends Request(r.method, r.url, r.queryParameters, r.formParameters) {

  val baseUrl: String = {
    val uri = new URI(url)
    Some(uri.getPort)
      .filterNot( isStandardPort(_, uri.getScheme) )
      .map ( port => s"${uri.getScheme}://${uri.getHost}:$port${uri.getPath}" )
      .getOrElse(s"${uri.getScheme}://${uri.getHost}${uri.getPath}")
  }

  private[this] def isStandardPort(port: Int, scheme: String) =
    (port < 0) || (port == 80 && scheme == "http") || (port == 443 && scheme == "https")
}
