package me.laiseca.oauth1.core.signature

import me.laiseca.oauth1.core.model.{ConsumerKeyParameter, OAuth1Request, Request, SignatureMethodParameter}
import org.scalatest.{FlatSpec, Matchers}

class SignatureBaseBuilderTest extends FlatSpec with Matchers {

  "build" should "generate a signature base with request method, url and parameters" in {
    val expected =
      "POST&" +
        "https%3A%2F%2Fexample.org%2Foauth1%2Finitiate&" +
        "formParameter%3DformValue%26" +
        "oauth_consumer_key%3Dconsumer_key%26" +
        "oauth_signature_method%3DHMAC-SHA256%26" +
        "queryParameter%3DqueryValue"

    val request = OAuth1Request(
      oAuth1Parameters = List(ConsumerKeyParameter -> "consumer_key", SignatureMethodParameter -> "HMAC-SHA256"),
      original = new Request(
        method = "POST",
        url = "https://example.org/oauth1/initiate",
        queryParameters = List("queryParameter" -> "queryValue"),
        formParameters = List("formParameter" -> "formValue")))

    val testFunction = SignatureBaseBuilder.build _

    testFunction(request) shouldBe expected
  }

  it should "sort parameters before concatenating them with values" in {
    val expected =
      "POST&" +
        "https%3A%2F%2Fexample.org%2Foauth1%2Finitiate&" +
        "oauth_consumer_key%3Dconsumer_key%26" +
        "oauth_signature_method%3DHMAC-SHA256%26" +
        "parameter%3DqueryValue%26" +
        "parameter1%3DformValue"

    val request = OAuth1Request(
      oAuth1Parameters = List(ConsumerKeyParameter -> "consumer_key", SignatureMethodParameter -> "HMAC-SHA256"),
      original = new Request(
        method = "POST",
        url = "https://example.org/oauth1/initiate",
        queryParameters = List("parameter" -> "queryValue"),
        formParameters = List("parameter1" -> "formValue")))

    val testFunction = SignatureBaseBuilder.build _

    testFunction(request) shouldBe expected
  }

  it should "sort parameters with same name by value" in {
    val expected =
      "POST&" +
        "https%3A%2F%2Fexample.org%2Foauth1%2Finitiate&" +
        "oauth_consumer_key%3Dconsumer_key%26" +
        "oauth_signature_method%3DHMAC-SHA256%26" +
        "parameter%3DformValue%26" +
        "parameter%3DqueryValue"

    val request = OAuth1Request(
      oAuth1Parameters = List(ConsumerKeyParameter -> "consumer_key", SignatureMethodParameter -> "HMAC-SHA256"),
      original = new Request(
        method = "POST",
        url = "https://example.org/oauth1/initiate",
        queryParameters = List("parameter" -> "queryValue"),
        formParameters = List("parameter" -> "formValue")))

    val testFunction = SignatureBaseBuilder.build _

    testFunction(request) shouldBe expected
  }

}
