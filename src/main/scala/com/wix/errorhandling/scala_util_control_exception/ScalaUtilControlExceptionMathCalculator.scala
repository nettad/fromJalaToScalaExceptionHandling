package com.wix.errorhandling.scala_util_control_exception

import com.wix.errorhandling.{MathCalculator, MathError, SomeMathBusinessException}

import scala.util.control.Exception
import scala.util.control.Exception._

class ScalaUtilControlExceptionMathCalculator() extends MathCalculator {


  override def divide(x: Int, by: Int): String = {
    val divideOrReturnError = Exception.failAsValue(classOf[ArithmeticException])("ERROR")
    divideOrReturnError(x / by).toString
  }

  //All these building blocks that ultimately wrap the divide function can be reused.
  override def divideStrings(x: String, by: String): Int = {
    val handleFormatErrorBySkipOrThrow = catching(classOf[NumberFormatException]).
      withApply { e: Throwable => if (x == "skip") -1 else throw MathError(e) }

    val handleArithmeticError = catching(classOf[ArithmeticException]) withApply { e: Throwable => throw MathError(e) }

    val divideOrSkipIfNeededElseReturnError = handleFormatErrorBySkipOrThrow or handleArithmeticError
    divideOrSkipIfNeededElseReturnError(x.toInt / by.toInt)
  }

  override def businessDivide(x: Int, by: Int): Int = {
    val divideOrThrowBusinessException = nonFatalCatch withApply (_ => throw new SomeMathBusinessException)
    divideOrThrowBusinessException(x / by)
  }
}
