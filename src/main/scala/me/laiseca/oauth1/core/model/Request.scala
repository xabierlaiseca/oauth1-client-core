package me.laiseca.oauth1.core.model

import java.net.URI

class Request(val method: String, val url: String, val queryParameters: Parameters, val formParameters: Parameters)

private[oauth1] case class EnrichedRequest(oAuth1Parameters: OAuth1Parameters,
                                           private val request: Request
                                          ) extends Request(request.method, request.url, request.queryParameters, request.formParameters) {

  val baseUrl: String = {
    val uri = new URI(url)
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
