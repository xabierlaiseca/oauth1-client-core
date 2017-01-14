package me.laiseca.oauth1.core.signature.requesttoken

import me.laiseca.oauth1.core.model.{CallbackParameter, ConsumerKeyParameter, EnrichedRequest, HmacSHA1, NonceParameter, Request, SignatureMethodParameter, TimestampParameter, VersionParameter}
import org.scalatest.{FlatSpec, Matchers}

class RequestTokenRequestEnricherTest extends FlatSpec with Matchers {

  "enrich" should "add OAuth1 parameters to the request" in {
    val original = new Request("POST", "http://example.org/path", Map.empty, Map.empty)
    val nonce = "1234567890"
    val timestamp = 11111111L
    val consumerKey = "consumer_key"
    val callback = "http://example.org/callback"
    val algorithm = HmacSHA1

    val testObj = new RequestTokenRequestEnricher(() => nonce, () => timestamp)
    val actual = testObj.enrich(original, consumerKey, callback, algorithm)

    actual shouldBe EnrichedRequest(
      Map(
        NonceParameter -> nonce,
        VersionParameter -> "1.0",
        SignatureMethodParameter -> algorithm.name,
        TimestampParameter -> timestamp.toString,
        ConsumerKeyParameter -> consumerKey,
        CallbackParameter -> callback
      ),
      original
    )
  }

}
