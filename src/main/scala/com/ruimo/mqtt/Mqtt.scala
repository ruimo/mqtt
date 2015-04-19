package com.ruimo.mqtt

import scala.util.{Try, Success, Failure}
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttException, MqttMessage, MqttConnectOptions}

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mqtt {
  val logger = LoggerFactory.getLogger(getClass)

  def withClient[T](
    url: String, clientId: String, connectOption: Option[MqttConnectOptions] = None
  )(f: MqttClient => T): Try[T] = 
    Try {
      val c = new MqttClient(url, clientId)
      connectOption match {
        case None => 
          println("Connecting without token...")
          c.connect()
        case Some(opt) => 
          println("Connecting with option..." + opt)
          c.connect(opt)
      }
      println("Connected.")
      c
    }.flatMap { client =>
      val result = Try {
        f(client)
      }

      try {
        println("Disconnectiong...")
        client.disconnect()
        println("Disconnected.")
      }
      catch {
        case e: MqttException => logger.error("Error in disconnecting mqtt client.", e)
      }

      result
    }

  def withClientWithUserPassword[T](
    url: String, clientId: String, user: String, password: String
  )(f: MqttClient => T): Try[T] = {
    val connectOption = new MqttConnectOptions
    connectOption.setUserName(user)
    connectOption.setPassword(password.toCharArray)

    withClient(url, clientId, Some(connectOption))(f)
  }
}
