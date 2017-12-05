package com.wix.errorhandling.scala_util_try

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryMathCalculatorTest extends SpecWithJUnit {

  "MatchCalculator" should {
    "divide 2 numbers and return the response as a String" in new ctx {
      scalaUtilTryMathCalculator.divide(x = 10, by = 2) must be_===("5")
    }

    "return ERROR when division fails" in new ctx {
      scalaUtilTryMathCalculator.divide(x = 10, by = 0) must be_===("ERROR")
    }
  }

  trait ctx extends Scope {
    val scalaUtilTryMathCalculator = new ScalaUtilTryMathCalculator
  }

}
