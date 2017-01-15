package me.laiseca.oauth1.core.model

import org.scalatest.{FlatSpec, Matchers}

class SignatureAlgorithmTest extends FlatSpec with Matchers {
  val text = "some text to digest"
  val consumerSecret = "consumer_secret"
  val tokenSecret = "token_secret"

  "digest" should "generate a digest message when both consumer secret and token secret are provided" in {
    val testObj = new TestAlgorithm

    val actual = testObj.digest(text, Some(consumerSecret), Some(tokenSecret))

    actual shouldBe "c29tZSB0ZXh0IHRvIGRpZ2VzdGNvbnN1bWVyX3NlY3JldCZ0b2tlbl9zZWNyZXQ="
  }

  it should "generate a digest message when only consumer secret is provided" in {
    val testObj = new TestAlgorithm

    val actual = testObj.digest(text, Some(consumerSecret), None)

    actual shouldBe "c29tZSB0ZXh0IHRvIGRpZ2VzdGNvbnN1bWVyX3NlY3JldCY="
  }

  it should "generate a digest message when only token secret is provided" in {
    val testObj = new TestAlgorithm

    val actual = testObj.digest(text, None, Some(tokenSecret))

    actual shouldBe "c29tZSB0ZXh0IHRvIGRpZ2VzdCZ0b2tlbl9zZWNyZXQ="
  }

  it should "fail when neither consumer secret not token secret are provided" in {
    intercept[AssertionError] {
      val testObj = new TestAlgorithm

      testObj.digest(text, None, None)
    }
  }
}

private class TestAlgorithm extends SignatureAlgorithm("test-algorithm") {
  override protected def digest(key: Array[Byte], text: Array[Byte]): Array[Byte] =
    text ++ key
}
