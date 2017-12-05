package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.{MatchCalculator, MathError}

import scala.util.{Failure, Try}

class ScalaUtilTryMathCalculator extends MatchCalculator {

  //Try can be transformed using map
  //In this example we can see that a scala.util.Failure can be transformed to another value using getOrElse
  def divide(x: Int, by: Int): String =
    Try(x / by) map(_.toString) getOrElse "ERROR"

  override def divideStrings(x: String, by: String): Int = {
    Try(x.toInt/by.toInt) recoverWith { case _: NumberFormatException => Failure(MathError()) } get
  }
}
