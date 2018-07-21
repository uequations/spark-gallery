name := "ueq-spark-gallery"
version := "1.0"
scalaVersion := "2.12.6"

lazy val slf4jV = "1.7.25"
lazy val log4jV = "1.2.17"
lazy val scalaTestV = "3.0.5"
lazy val sparkCoreV = "2.3.1"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % slf4jV,
  "org.slf4j" % "slf4j-log4j12" % slf4jV,
  "log4j" % "log4j" % log4jV,
  "org.apache.spark" % "spark-core_2.11" % sparkCoreV,
  "org.scalatest" %% "scalatest" % scalaTestV % "test")
