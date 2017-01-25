organization := "me.laiseca.oauth1"
name := "oauth1-client-core"
version := "0.1"

scalaVersion := "2.12.1"
crossScalaVersions := Seq("2.10.6", "2.11.8")

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
