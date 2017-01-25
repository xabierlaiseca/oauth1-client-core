package me.laiseca.oauth1.client.core.signature

import me.laiseca.oauth1.client.core.model.{CallbackParameter, NonceParameter, SignatureMethodParameter, TimestampParameter}
import org.scalatest.{FlatSpec, Matchers}

class SignatureHeadersBuilderTest extends FlatSpec with Matchers {
  "build" should "generate an authorization header" in {
    val expectedAuthorizationValue =
      "OAuth " +
      s"""${NonceParameter.name}=\"nonce\", """ +
      s"""${SignatureMethodParameter.name}=\"HMAC-SHA1\", """ +
      s"""${TimestampParameter.name}=\"1234567890\""""

    val expected = List("Authorization" -> expectedAuthorizationValue)

    val parameters = List(
      NonceParameter -> "nonce",
      TimestampParameter -> "1234567890",
      SignatureMethodParameter -> "HMAC-SHA1"
    )

    val testFunction = SignatureHeadersBuilder.build _

    testFunction(parameters) shouldBe expected
  }

  it should "encode OAuth1 parameter values in the authorization header" in {
    val expectedAuthorizationValue =
      "OAuth " +
        s"""${CallbackParameter.name}=\"https%3A%2F%2Fexample.org%2Foauth1%2Fcallback\", """ +
        s"""${NonceParameter.name}=\"nonce\", """ +
        s"""${TimestampParameter.name}=\"1234567890\""""

    val expected = List("Authorization" -> expectedAuthorizationValue)

    val parameters = List(
      NonceParameter -> "nonce",
      CallbackParameter -> "https://example.org/oauth1/callback",
      TimestampParameter -> "1234567890"
    )

    val testFunction = SignatureHeadersBuilder.build _

    testFunction(parameters) shouldBe expected
  }
}
