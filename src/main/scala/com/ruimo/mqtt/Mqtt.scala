package com.ruimo.mqtt

import scala.util.{Try, Success, Failure}
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttException, MqttMessage, MqttConnectOptions, MqttCallback, IMqttDeliveryToken}

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mqtt {
  val logger = LoggerFactory.getLogger(getClass)

  private def nullOnConnectionLost(client: MqttClient, cause: Throwable) {}
  private def nullOnMessageArrived(client: MqttClient, topic: String, message: MqttMessage) {}
  private def nullOnDeliveryComplete(client: MqttClient, token: IMqttDeliveryToken) {}

  def withClient[T](
    url: String, clientId: String, 
    connectOption: Option[MqttConnectOptions] = None,
    onConnectionLost: (MqttClient, Throwable) => Unit = nullOnConnectionLost,
    onMessageArrived: (MqttClient, String, MqttMessage) => Unit = nullOnMessageArrived,
    onDeliveryComplete: (MqttClient, IMqttDeliveryToken) => Unit = nullOnDeliveryComplete
  )(f: MqttClient => T): Try[T] = 
    Try {
      val c = new MqttClient(url, clientId)
      c.setCallback(new MqttCallback {
        override def connectionLost(cause: Throwable) {
          onConnectionLost(c, cause)
        }

        override def messageArrived(topic: String, message: MqttMessage) {
          onMessageArrived(c, topic, message)
        }

        override def deliveryComplete(token: IMqttDeliveryToken) {
          onDeliveryComplete(c, token)
        }
      })
      connectOption match {
        case None => 
          logger.info("Connecting without token...")
          c.connect()
        case Some(opt) => 
          logger.info("Connecting with option..." + opt)
          c.connect(opt)
      }
      logger.info("Connected.")
      c
    }.flatMap { client =>
      val result = Try {
        f(client)
      }

      try {
        logger.info("Disconnectiong...")
        client.disconnect()
        logger.info("Disconnected.")
      }
      catch {
        case e: MqttException => logger.error("Error in disconnecting mqtt client.", e)
      }

      result
    }

  def withClientWithUserPassword[T](
    url: String, clientId: String, user: String, password: String,
    onConnectionLost: (MqttClient, Throwable) => Unit = nullOnConnectionLost,
    onMessageArrived: (MqttClient, String, MqttMessage) => Unit = nullOnMessageArrived,
    onDeliveryComplete: (MqttClient, IMqttDeliveryToken) => Unit = nullOnDeliveryComplete
  )(f: MqttClient => T): Try[T] = {
    val connectOption = new MqttConnectOptions
    connectOption.setUserName(user)
    connectOption.setPassword(password.toCharArray)

    withClient(url, clientId, Some(connectOption), onConnectionLost, onMessageArrived, onDeliveryComplete)(f)
  }
}
