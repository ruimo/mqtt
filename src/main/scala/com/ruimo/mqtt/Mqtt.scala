package com.ruimo.mqtt

import scala.util.{Try, Success, Failure}
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttException, MqttMessage => PahoMqttMessage, MqttConnectOptions, MqttCallback, IMqttDeliveryToken}

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mqtt {
  val logger = LoggerFactory.getLogger(getClass)

  private def nullOnConnectionLost(cause: Throwable) {}
  private def nullOnMessageArrived(topic: String, message: MqttMessage) {}
  private def nullOnDeliveryComplete(token: IMqttDeliveryToken) {}

  // You need to call MqttClient.disconnect() by yourself.
  def createClientWithUserPassword(
    url: String, clientId: String, user: String, password: String,
    onConnectionLost: (Throwable) => Unit = nullOnConnectionLost,
    onMessageArrived: (String, MqttMessage) => Unit = nullOnMessageArrived,
    onDeliveryComplete: (IMqttDeliveryToken) => Unit = nullOnDeliveryComplete
  ): MqttClient = {
    val connectOption = new MqttConnectOptions
    connectOption.setUserName(user)
    connectOption.setPassword(password.toCharArray)
    createClient(url, clientId, Some(connectOption), onConnectionLost, onMessageArrived, onDeliveryComplete)
  }

  // You need to call MqttClient.disconnect() by yourself.
  def createClient(
    url: String, clientId: String, 
    connectOption: Option[MqttConnectOptions] = None,
    onConnectionLost: (Throwable) => Unit = nullOnConnectionLost,
    onMessageArrived: (String, MqttMessage) => Unit = nullOnMessageArrived,
    onDeliveryComplete: (IMqttDeliveryToken) => Unit = nullOnDeliveryComplete
  ): MqttClient = {
    val c = new MqttClient(url, clientId)
    c.setCallback(new MqttCallback {
      override def connectionLost(cause: Throwable) {
        onConnectionLost(cause)
      }

      override def messageArrived(topic: String, message: PahoMqttMessage) {
        onMessageArrived(topic, MqttMessage(message))
      }

      override def deliveryComplete(token: IMqttDeliveryToken) {
        onDeliveryComplete(token)
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
  }

  def withClient[T](clientFactory: () => MqttClient)(f: MqttClient => T): Try[T] = 
    Try {
      clientFactory()
    }.flatMap { client =>
      val result = Try {
        f(client)
      }

      try {
        logger.info("Disconnecting...")
        client.disconnect()
        logger.info("Disconnected.")
      }
      catch {
        case e: MqttException => logger.error("Error in disconnecting mqtt client.", e)
      }

      result
    }
}
