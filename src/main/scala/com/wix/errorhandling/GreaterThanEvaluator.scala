package com.wix.errorhandling

object GreaterThanEvaluator {

  def howMuchGreater(isX: String, thanY: String) = {
    val x = isX.toInt
    val y = thanY.toInt
    
    if (x < y) throw XIsLessThanYException()
    x - y
  }

}

case class XIsLessThanYException() extends RuntimeException
