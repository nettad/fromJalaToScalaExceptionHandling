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
      //Tries can be transformed
      scalaUtilTryMathCalculator.divide(x = 10, by = 0) must be_===("ERROR")
    }

    "trying to divide strings and return the response as an int" in new ctx {
      scalaUtilTryMathCalculator.tryToDivideStrings(x = "10", by = "2") must beSuccessfulTry(5)
    }

    "trying to divide strings should result in a math error when one of the strings is not an int" in new ctx {
      //Try.recoverWith can be used replace one util.Failure with another
      scalaUtilTryMathCalculator.tryToDivideStrings(x = "10", by = "foo") must beFailedTry.withThrowable[MathError]
    }

    "trying to divide strings should return -1 when x is equal to 'skip'" in new ctx {
      //Try.recoverWith can be used to replace a util.Failure with a util.Success
      scalaUtilTryMathCalculator.tryToDivideStrings(x = "skip", by = "2") must beSuccessfulTry(-1)
    }

    "divide strings and return the response as an int" in new ctx {
      scalaUtilTryMathCalculator.divideStrings(x = "10", by = "2") must be_===(5)
    }

    "dividing strings should result in a math error when one of the strings is not an int" in new ctx {
      //Try.recover can be used replace one util.Failure with another
      scalaUtilTryMathCalculator.divideStrings(x = "10", by = "foo") must throwA[MathError]
    }

    "dividing strings should return -1 when x is equal to 'skip'" in new ctx {
      //Try.recover can be used to replace a util.Failure with a util.Success
      scalaUtilTryMathCalculator.divideStrings(x = "skip", by = "2") must be_===(-1)
    }

    

  }

  trait ctx extends Scope {
    val scalaUtilTryMathCalculator = new ScalaUtilTryMathCalculator
  }

}
