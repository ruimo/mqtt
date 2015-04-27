package com.ruimo.mqtt

import org.eclipse.paho.client.mqttv3.{MqttMessage => PahoMqttMessage}

// Immutable MqttMessage.
case class MqttMessage(
  qos: Qos,
  isRetained: Boolean,
  payload: Seq[Byte]
)

object MqttMessage {
  def apply(m: PahoMqttMessage): MqttMessage = MqttMessage(
    qos = Qos(m.getQos),
    isRetained = m.isRetained,
    payload = m.getPayload.clone
  )
}

