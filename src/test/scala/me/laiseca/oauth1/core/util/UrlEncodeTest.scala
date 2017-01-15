package me.laiseca.oauth1.core.util

import org.scalatest.{FlatSpec, Matchers}

class UrlEncodeTest extends FlatSpec with Matchers {

  "encode" should "generate a url safe string" in {
    UrlEncode.encode("$0methin&_to_encode") shouldBe "%240methin%26_to_encode"
  }

  it should "generate '%20' from ' '" in {
    UrlEncode.encode(" ") shouldBe "%20"
  }

  it should "generate '%2A' from '*'" in {
    UrlEncode.encode("*") shouldBe "%2A"
  }

  it should "keep '~' unmodified" in {
    UrlEncode.encode("~") shouldBe "~"
  }

  it should "generate multiple url safe strings" in {
    UrlEncode.encode(List("$0methin&", "to", "€ncod€")) shouldBe List("%240methin%26", "to", "%E2%82%ACncod%E2%82%AC")
  }
}
