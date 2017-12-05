package com.wix.errorhandling

trait MatchCalculator {

  def divide(x: Int, by: Int): String

  def divideStrings(x: String, by: String): Int
}

case class MathError(e: Exception) extends RuntimeException
