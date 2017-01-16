package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{HmacSHA256, Request}
import org.scalatest.{FlatSpec, Matchers}

class RequestTokenRequestSignatureTest extends FlatSpec with Matchers {
  val consumerKey = "my_consumer_key"
  val consumerSecret = "my_consumer_secret"
  val callback = "https://my.host.com/oauth1/callback"
  val nonce = "e3bc0e0b-1d6c-479d-8e74-16bb5662d8d0"
  val algorithm = HmacSHA256
  val timestamp = 1484510633L

  "signature" should "generate an authorization headers with OAuth1 parameters on it" in {
    val expected = List(
      buildExpectedAuthorizationHeader("X%2BoyS8Hs0Ff5IcG9WYUXgYISu4BqbjL4Z3Qdd1Zpk%2BM%3D"))

    val request = Request("POST", "https://example.org/oauth/initiate", Nil, Nil)

    val testObject = new RequestTokenRequestSignature(
      consumerKey, consumerSecret, callback, algorithm, () => nonce, () => timestamp
    )

    testObject.signature(request) shouldBe expected
  }

  it should "generate an authorization headers with OAuth1 parameters on it when query parameters are provided" in {
    val expected = List(
      buildExpectedAuthorizationHeader("7ktkRsjQga1o%2FcxUAY%2FxYbFLmEe5nVzVrFj%2BRv87foo%3D"))

    val request = Request("POST", "https://example.org/oauth/initiate", ("query_param" -> "query_value") :: Nil, Nil)

    val testObject = new RequestTokenRequestSignature(
      consumerKey, consumerSecret, callback, algorithm, () => nonce, () => timestamp
    )

    testObject.signature(request) shouldBe expected
  }

  it should "generate an authorization headers with OAuth1 parameters on it when body parameters are provided" in {
    val expected = List(
      buildExpectedAuthorizationHeader("CSPyPR0%2F4XorOapqHWVEju0RYDNqBvugse7diBQ1koQ%3D"))

    val request = Request("POST", "https://example.org/oauth/initiate", Nil, ("body_param" -> "body_value") :: Nil)

    val testObject = new RequestTokenRequestSignature(
      consumerKey, consumerSecret, callback, algorithm, () => nonce, () => timestamp
    )

    testObject.signature(request) shouldBe expected
  }



  private[this] def buildExpectedAuthorizationHeader(oAuthSignature: String): (String, String) = {
    val headerValue = "OAuth " +
      "oauth_callback=\"https%3A%2F%2Fmy.host.com%2Foauth1%2Fcallback\", " +
      s"""oauth_consumer_key="$consumerKey", """ +
      s"""oauth_nonce="$nonce", """ +
      s"""oauth_signature=\"$oAuthSignature\", """ +
      s"""oauth_signature_method="${algorithm.name}", """ +
      s"""oauth_timestamp="$timestamp", """ +
      "oauth_version=\"1.0\""

    "Authorization" -> headerValue
  }
}
