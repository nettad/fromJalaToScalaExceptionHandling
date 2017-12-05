package com.wix.errorhandling

import scala.util.Try

trait MatchCalculator {

  def divide(x: Int, by: Int): String

  def tryToDivideStrings(x: String, by: String): Try[Int]

  def divideStrings(x: String, by: String): Int
}

case class MathError(e: Exception) extends RuntimeException
