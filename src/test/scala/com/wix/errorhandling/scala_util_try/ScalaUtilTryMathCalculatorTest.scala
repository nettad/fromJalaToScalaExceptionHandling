package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.MathError
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

    "divide strings and return the response as an int" in new ctx {
      scalaUtilTryMathCalculator.divideStrings(x = "10", by = "2") must be_===(5)
    }

    "dividing strings should result in a math error when one of the strings is not an int" in new ctx {
      scalaUtilTryMathCalculator.divideStrings(x = "10", by = "foo") must throwA[MathError]
    }

    "dividing strings should result in a math error when one of the strings is null" in new ctx {
      scalaUtilTryMathCalculator.divideStrings(x = "10", by = "foo") must throwA[MathError]
    }

    

  }

  trait ctx extends Scope {
    val scalaUtilTryMathCalculator = new ScalaUtilTryMathCalculator
  }

}
