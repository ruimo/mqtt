package com.ruimo.mqtt.mqttclient

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.fusesource.mqtt.client.{MQTT, CallbackConnection => Connection, Callback}

// Adapter for mqtt-client https://github.com/fusesource/mqtt-client
object CallbackMqttClient {
  val logger = LoggerFactory.getLogger(getClass)

  def createConnectionWithUserPassword(
    url: String, clientId: String, user: String, password: String
  ): Connection = {
    val mqtt = new MQTT
    mqtt.setTracer(new MqttTracer(logger))
    mqtt.setHost(url)
    mqtt.setClientId(clientId)
    mqtt.setUserName(user)
    mqtt.setPassword(password)
    mqtt.callbackConnection()
  }

  def voidCallback(
    success: => Unit, failure: Throwable => Unit
  ): Callback[Void] = new Callback[Void] {
    override def onFailure(t: Throwable) {
      failure(t)
    }
    override def onSuccess(value: Void) {
      success
    }
  }

  def callback[T](
    success: T => Unit, failure: Throwable => Unit
  ): Callback[T] = new Callback[T] {
    override def onFailure(t: Throwable) {
      failure(t)
    }
    override def onSuccess(value: T) {
      success(value)
    }
  }
}
