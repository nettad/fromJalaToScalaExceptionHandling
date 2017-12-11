package com.wix.errorhandling

trait MathCalculator {

  def divide(x: Int, by: Int): String

  def divideStrings(x: String, by: String): Int

  def businessDivide(x: Int, by: Int): Int
}

case class MathError(e: Throwable) extends RuntimeException

case class SomeMathBusinessException(e: Throwable) extends RuntimeException
