package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.MatchCalculator

import scala.util.Try

class ScalaUtilTryMathCalculator extends MatchCalculator {

  def divide(x: Int, by: Int): String =
    Try(x / by) map(_.toString) getOrElse "ERROR"

}
