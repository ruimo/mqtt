package com.ruimo.mqtt.mqttclient

import scala.util.{Try, Success, Failure}
import org.fusesource.mqtt.client.{MQTT, BlockingConnection => Connection}
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// Adapter for mqtt-client https://github.com/fusesource/mqtt-client
object BlockingMqttClient {
  val logger = LoggerFactory.getLogger(getClass)

  def createConnectionWithUserPassword(
    url: String, clientId: String, user: String, password: String
  ): Connection = {
    val mqtt = new MQTT
    mqtt.setHost(url)
    mqtt.setClientId(clientId)
    mqtt.setUserName(user)
    mqtt.setPassword(password)
    mqtt.blockingConnection()
  }

  def withConnection[T](connectionFactory: () => Connection)(f: Connection => T): Try[T] = 
    Try {
      connectionFactory()
    }.flatMap { conn =>
      val result = Try {
        conn.connect()
        f(conn)
      }

      try {
        logger.info("Disconnecting...")
        conn.disconnect()
        logger.info("Disconnected.")
      }
      catch {
        case e: Exception => logger.error("Error in disconnecting mqtt connection.", e)
      }

      result
    }
}
