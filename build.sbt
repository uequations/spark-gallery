name := "ueq-spark-gallery"
version := "1.0"
scalaVersion := "2.12.6"

lazy val slf4jV = "1.7.25"
lazy val log4jV = "1.2.17"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % slf4jV,
  "org.slf4j" % "slf4j-log4j12" % slf4jV,
  "log4j" % "log4j" % log4jV)
