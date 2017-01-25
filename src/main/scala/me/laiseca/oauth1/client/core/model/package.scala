package me.laiseca.oauth1.client.core

package object model {
  type Parameters = List[(String, String)]
  type OAuth1Parameters = List[(OAuth1Parameter, String)]
  type Headers = List[(String, String)]
}
