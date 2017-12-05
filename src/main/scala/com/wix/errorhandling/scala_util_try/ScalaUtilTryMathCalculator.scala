package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.{MatchCalculator, MathError}

import scala.util.{Failure, Success, Try}

class ScalaUtilTryMathCalculator extends MatchCalculator {

  //Try can be transformed using map
  //In this example we can see that a scala.util.Failure can be transformed to another value using getOrElse
  def divide(x: Int, by: Int): String =
    Try(x / by) map(_.toString) getOrElse "ERROR"


  //Failures can be replaced by other Failures using recoverWith
  //Alternatively the Failure can be ignored altogether and a Successful result returned instead
  override def divideStrings(x: String, by: String): Int = {
    Try(x.toInt/by.toInt) recoverWith {
      case _: NumberFormatException if x == "skip" => Success(-1)
      case e: NumberFormatException => Failure(MathError(e))
    } get
  }
}
