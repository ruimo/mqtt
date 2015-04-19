name := """mqtt"""

organization := "com.ruimo"

version := "1.0-SNAPSHOT"

crossScalaVersions := List("2.10.4", "2.11.2") 

publishTo := Some(
  Resolver.file(
    "mqtt",
    new File(Option(System.getenv("RELEASE_DIR")).getOrElse("/tmp"))
  )
)

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9"

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers += "Eclipse paho" at "https://repo.eclipse.org/content/repositories/paho-releases/"

resolvers += "ruimo.com" at "http://static.ruimo.com/release"

// Eclipse paho
libraryDependencies += "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.0.2"

// Logging
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.slf4j" % "slf4j-api" % "1.7.12"
)

// Ruimo scoins
libraryDependencies += "com.ruimo" %% "scoins" % "1.0-SNAPSHOT"
