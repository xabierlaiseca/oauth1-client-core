package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{CallbackParameter, ConsumerKeyParameter, HmacSHA1, NonceParameter, SignatureMethodParameter, TimestampParameter, VersionParameter}
import org.scalatest.{FlatSpec, Matchers}

class OAuth1ParametersBuilderTest extends FlatSpec with Matchers {
  "base" should "return a list of common parameters to all OAuth1 requests" in {
    val nonce = "1234567890"
    val timestamp = 1111111L
    val algorithm = HmacSHA1

    val testFunction = OAuth1ParametersBuilder.base _
    val actual = testFunction(() => nonce, () => timestamp, algorithm)

    actual shouldBe List(
      NonceParameter -> nonce,
      VersionParameter -> "1.0",
      SignatureMethodParameter -> algorithm.name,
      TimestampParameter -> timestamp.toString
    )
  }

  "requestToken" should "return a list of request token request specific parameters" in {
    val consumerKey = "consumer_key"
    val callback = "http://example.org/callback"

    val testFunction = OAuth1ParametersBuilder.requestToken _
    val actual = testFunction(consumerKey, callback)

    actual shouldBe List(
      ConsumerKeyParameter -> consumerKey,
      CallbackParameter -> callback
    )

  }
}
