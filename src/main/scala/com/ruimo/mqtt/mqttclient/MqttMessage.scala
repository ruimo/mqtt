package com.ruimo.mqtt.mqttclient

import com.ruimo.scoins.ImmutableByteArray
import org.fusesource.mqtt.client.Message

case class MqttMessage(
  topic: String,
  payload: ImmutableByteArray
)

object MqttMessage {
  def apply(m: Message): MqttMessage = {
    val result = MqttMessage(
      topic = m.getTopic,
      payload = ImmutableByteArray(m.getPayload)
    )
    m.ack()
    result
  }
}
