package com.ruimo.mqtt

import org.specs2.mutable.Specification

class QosSpec extends Specification {
  "Qos" should {
    "Can convert to int" in {
      val code0: Int = Qos.AtMostOnce
      code0 === Qos.AtMostOnce.code

      val code1: Int = Qos.AtLeastOnce
      code1 === Qos.AtLeastOnce.code

      val code2: Int = Qos.ExactlyOnce
      code2 === Qos.ExactlyOnce.code
    }
  }
}
