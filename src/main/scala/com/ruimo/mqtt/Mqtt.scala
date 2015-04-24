package com.ruimo.mqtt

import scala.util.{Try, Success, Failure}
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttException, MqttMessage, MqttConnectOptions, MqttCallback}

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mqtt {
  val logger = LoggerFactory.getLogger(getClass)

  def withClient[T](
    url: String, clientId: String, 
    connectOption: Option[MqttConnectOptions] = None,
    callback: Option[MqttCallback] = None
  )(f: MqttClient => T): Try[T] = 
    Try {
      val c = new MqttClient(url, clientId)
      callback.foreach { callback => c.setCallback(callback) }
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
    callback: Option[MqttCallback] = None
  )(f: MqttClient => T): Try[T] = {
    val connectOption = new MqttConnectOptions
    connectOption.setUserName(user)
    connectOption.setPassword(password.toCharArray)

    withClient(url, clientId, Some(connectOption), callback)(f)
  }
}
