package com.wix.errorhandling.scala_util_control_exception

import com.wix.errorhandling.{MathError, SomeMathBusinessException}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilControlExceptionMathCalculatorTest extends SpecWithJUnit {

  "MatchCalculator" should {
    "divide 2 numbers and return the response as a String" in new ctx {
      scalaUtilControlExceptionMathCalculator.divide(x = 10, by = 2) must be_===("5")
    }

    "return ERROR when division fails" in new ctx {
      scalaUtilControlExceptionMathCalculator.divide(x = 10, by = 0) must be_===("ERROR")
    }

    "divide strings and return the response as an int" in new ctx {
      scalaUtilControlExceptionMathCalculator.divideStrings(x = "10", by = "2") must be_===(5)
    }

    "dividing strings should result in a math error when one of the strings is not an int" in new ctx {
      scalaUtilControlExceptionMathCalculator.divideStrings(x = "10", by = "foo") must throwA[MathError]
    }

    "dividing strings should return -1 when x is equal to 'skip'" in new ctx {
      scalaUtilControlExceptionMathCalculator.divideStrings(x = "skip", by = "2") must be_===(-1)
    }

    "dividing strings should result in math error when two numbers fail to be divided" in new ctx {
      scalaUtilControlExceptionMathCalculator.divideStrings(x = "10", by = "0") must throwA[MathError]
    }

    "return result of successful business divide" in new ctx {
      scalaUtilControlExceptionMathCalculator.businessDivide(x = 10, by = 2) must be_===(5)
    }

    "replace any exception with SomeMathBusinessException when businessDivide fails" in new ctx {
      scalaUtilControlExceptionMathCalculator.businessDivide(x = 10, by = 0) must throwA[SomeMathBusinessException]
    }


  }

  trait ctx extends Scope {
    val scalaUtilControlExceptionMathCalculator = new ScalaUtilControlExceptionMathCalculator
  }

}
