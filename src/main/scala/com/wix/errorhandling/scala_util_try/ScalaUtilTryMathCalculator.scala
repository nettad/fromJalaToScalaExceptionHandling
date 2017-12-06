package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.{MathCalculator, MathError, SomeMathBusinessException}

import scala.util.{Failure, Success, Try}

class ScalaUtilTryMathCalculator extends MathCalculator {


  //Try can be transformed using map
  //In this example we can see that a scala.util.Failure can be transformed to another value using getOrElse
  override def divide(x: Int, by: Int): String =
    Try(x / by) map(_.toString) getOrElse "ERROR"


  //Failures can be replaced by other Failures using recover
  //Alternatively the Failure can be ignored altogether and a Successful result returned instead
  override def tryToDivideStrings(x: String, by: String): Try[Int] =
    Try(x.toInt/by.toInt) recover {
      case _: NumberFormatException if x == "skip" => -1
      case e: NumberFormatException => throw MathError(e)
    }


  //Failures can be replaced by other Failures using recoverWith
  //Alternatively the Failure can be ignored altogether and a Successful result returned instead
  override def divideStrings(x: String, by: String): Int =
  Try(x.toInt/by.toInt) recoverWith {
    case _: NumberFormatException if x == "skip" => Success(-1)
    case e: NumberFormatException => Failure(MathError(e))
  } get

  //Failures can be replaced by other failures altogether
  //this can be achieved via getOrElse(throw SomeMathBusinessException()) as well
  override def businessDivide(x: Int, by: Int): Int =
    Try(x / by) orElse Failure(SomeMathBusinessException()) get

}
