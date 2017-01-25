package me.laiseca.oauth1.client.core.model

import me.laiseca.oauth1.client.core.Defaults
import me.laiseca.oauth1.client.core.util.UrlEncode
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.HmacUtils

abstract class SignatureAlgorithm(val name: String) {

  def digest(text: String, consumerSecret: Option[String], tokenSecret: Option[String]): String = {
    assert(consumerSecret.isDefined || tokenSecret.isDefined)

    val d = digest(
      key(consumerSecret, tokenSecret).getBytes(Defaults.Charset),
      text.getBytes(Defaults.Charset))

    val digest64 = new Base64(0).encode(d)
    new String(digest64, Defaults.Charset)
  }

  private[this] def key(consumerSecret: Option[String], tokenSecret: Option[String]): String = concat {
    UrlEncode.encode(List(consumerSecret.getOrElse(""), tokenSecret.getOrElse("")))
  }

  private[this] def concat(items: Iterable[String]): String = items.mkString("&")

  protected def digest(key: Array[Byte], text: Array[Byte]): Array[Byte]
}

case object HmacSHA1 extends SignatureAlgorithm("HMAC-SHA1") {
  override protected def digest(key: Array[Byte], text: Array[Byte]): Array[Byte] =
    HmacUtils.hmacSha1(key, text)
}

case object HmacSHA256 extends SignatureAlgorithm("HMAC-SHA256") {
  override protected def digest(key: Array[Byte], text: Array[Byte]): Array[Byte] =
    HmacUtils.hmacSha256(key, text)
}
