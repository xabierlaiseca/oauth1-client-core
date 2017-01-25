package me.laiseca.oauth1.client.core.util

import java.nio.charset.Charset

import me.laiseca.oauth1.client.core.Defaults
import org.apache.commons.codec.net.URLCodec

object UrlEncode {
  def encode(items: Iterable[String]): Iterable[String] = encode(items, Defaults.Charset)
  def encode(items: Iterable[String], charset: Charset): Iterable[String] =
    items.map { encode(_, charset) }

  def encode(s: String, charset: Charset = Defaults.Charset): String =
    new URLCodec(charset.displayName())
      .encode(s)
      .replaceAll("\\+", "%20")
      .replaceAll("\\*", "%2A")
      .replaceAll("%7E", "~")
}
