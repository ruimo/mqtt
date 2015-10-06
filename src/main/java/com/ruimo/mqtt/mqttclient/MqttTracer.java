package com.ruimo.mqtt.mqttclient;

import org.slf4j.Logger;
import org.fusesource.mqtt.client.Tracer;
import static java.util.Objects.requireNonNull;
import org.fusesource.mqtt.codec.MQTTFrame;

public class MqttTracer extends Tracer {
    final Logger logger;

    public MqttTracer(Logger logger) {
        this.logger = requireNonNull(logger);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void onSend(MQTTFrame frame) {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending " + frame);
        }
    }

    public void onReceive(MQTTFrame frame) {
        if (logger.isDebugEnabled()) {
            logger.debug("Receiving " + frame);
        }
    }
}
