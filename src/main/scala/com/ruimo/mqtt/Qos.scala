package com.ruimo.mqtt

import scala.language.implicitConversions

abstract sealed class Qos(val code: Int) {
  implicit def qosToInt(qos: Qos) = qos.code
}

object Qos {
  case object AtMostOnce extends Qos(0)
  case object AtLeastOnce extends Qos(1)
  case object ExactlyOnce extends Qos(2)

  def apply(code: Int): Qos = code match {
    case 0 => AtMostOnce
    case 1 => AtLeastOnce
    case 2 => ExactlyOnce
    case _ => throw new IllegalArgumentException("Invalid qos code " + code)
  }
}
