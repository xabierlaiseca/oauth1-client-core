package me.laiseca.oauth1.core.model

import org.scalatest.{FlatSpec, Matchers}

class OAuth1RequestTest extends FlatSpec with Matchers {
  "baseUrl" should "build the same url when only scheme, host and path are defined in request" in {
    val original = new Request("POST", "http://example.org/path", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "http://example.org/path"
  }

  it should "build the same url when only scheme, host, non standard port and path are defined in request" in {
    val original = new Request("POST", "http://example.org:8080/path", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "http://example.org:8080/path"
  }

  it should "drop the port when 80 defined for http" in {
    val original = new Request("POST", "http://example.org:80/path", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "http://example.org/path"
  }

  it should "drop the port when 443 defined for https" in {
    val original = new Request("POST", "https://example.org:443/path", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "drop query parameters" in {
    val original = new Request("POST", "https://example.org/path?param=value", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "drop the fragment" in {
    val original = new Request("POST", "https://example.org/path#content", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "only convert scheme and host to lower case" in {
    val original = new Request("POST", "HTTPs://EXAMPLE.ORG/PATH", List.empty, List.empty)
    val testObj = OAuth1Request(original, List.empty)

    testObj.baseUrl shouldBe "https://example.org/PATH"
  }
}
