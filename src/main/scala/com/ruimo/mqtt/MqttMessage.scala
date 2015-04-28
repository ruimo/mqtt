package com.ruimo.mqtt

import org.eclipse.paho.client.mqttv3.{MqttMessage => PahoMqttMessage}
import com.ruimo.scoins.ImmutableByteArray

// Immutable MqttMessage.
case class MqttMessage(
  qos: Qos,
  isRetained: Boolean,
  payload: ImmutableByteArray
)

object MqttMessage {
  def apply(m: PahoMqttMessage): MqttMessage = MqttMessage(
    qos = Qos(m.getQos),
    isRetained = m.isRetained,
    payload = ImmutableByteArray(m.getPayload)
  )
}
