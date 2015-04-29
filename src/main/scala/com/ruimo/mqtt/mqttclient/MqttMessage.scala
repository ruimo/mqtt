package com.ruimo.mqtt.mqttclient

import com.ruimo.scoins.ImmutableByteArray
import org.fusesource.mqtt.client.Message

case class MqttMessage(
  topic: String,
  payload: ImmutableByteArray
)

object MqttMessage {
  def apply(m: Message): MqttMessage = MqttMessage(
    topic = m.getTopic,
    payload = ImmutableByteArray(m.getPayload)
  )
}
