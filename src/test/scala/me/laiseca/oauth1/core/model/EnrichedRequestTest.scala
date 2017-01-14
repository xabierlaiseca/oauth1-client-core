package me.laiseca.oauth1.core.model

import org.scalatest.{FlatSpec, Matchers}

class EnrichedRequestTest extends FlatSpec with Matchers {
  "baseUrl" should "build the same url when only scheme, host and path are defined in request" in {
    val original = new Request("POST", "http://example.org/path", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "http://example.org/path"
  }

  it should "build the same url when only scheme, host, non standard port and path are defined in request" in {
    val original = new Request("POST", "http://example.org:8080/path", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "http://example.org:8080/path"
  }

  it should "drop the port when 80 defined for http" in {
    val original = new Request("POST", "http://example.org:80/path", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "http://example.org/path"
  }

  it should "drop the port when 443 defined for https" in {
    val original = new Request("POST", "https://example.org:443/path", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "drop query parameters" in {
    val original = new Request("POST", "https://example.org/path?param=value", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "drop the fragment" in {
    val original = new Request("POST", "https://example.org/path#content", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "https://example.org/path"
  }

  it should "only convert scheme and host to lower case" in {
    val original = new Request("POST", "HTTPs://EXAMPLE.ORG/PATH", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.baseUrl shouldBe "https://example.org/PATH"
  }

  "method" should "return the value specified in the original request" in {
    val original = new Request("POST", "https://example.org/path#content", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.method shouldBe "POST"
  }

  "url" should "return the value specified in the original request" in {
    val original = new Request("POST", "https://example.org/path#content", Map.empty, Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.url shouldBe "https://example.org/path#content"
  }

  "queryParameters" should "return the value specified in the original request" in {
    val original = new Request("POST", "https://example.org/path#content", Map("param" -> "value"), Map.empty)
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.queryParameters shouldBe Map("param" -> "value")
  }

  "formParameters" should "return the value specified in the original request" in {
    val original = new Request("POST", "https://example.org/path#content", Map.empty, Map("param" -> "value"))
    val testObj = EnrichedRequest(Map.empty, original)

    testObj.formParameters shouldBe Map("param" -> "value")
  }
}
