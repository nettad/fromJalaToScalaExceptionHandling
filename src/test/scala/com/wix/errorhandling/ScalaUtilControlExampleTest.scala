package com.wix.errorhandling

import java.io.File

import org.specs2.mutable.SpecWithJUnit

import scala.io.{BufferedSource, Source}
import scala.util.control.Exception
import scala.util.control.Exception.{Catch, catching, handling, nonFatalCatch, ultimately}

class ScalaUtilControlExampleTest extends SpecWithJUnit {

  "Example of how to use basic handle/by/ultimately construct to read a file" in {
    def read(file: String): String =
    logExceptionsAndThrow {
      val fileResource = new File(getClass.getClassLoader.getResource(file).getPath)
      process(file = Source.fromFile(fileResource))
    }

    def process(file: BufferedSource) =
    closeFileWhenDone(file) {
      logExceptionsAndThrow {
        file.mkString
      }
    }

    def closeFileWhenDone[T](input: BufferedSource): Catch[T] =
    ultimately[T] {
      if (input != null) input.close
    }

    //Notice how logExceptionsAndThrow is reused in this code
    def logExceptionsAndThrow = handling(classOf[Exception]).by { e =>
      println("Where is my file?")
      throw e
    }

    read("file.txt") must be_===("This file contains some random text. It's really not important what's written here.")

  }

  "Example of Exception.failAsValue" in {
    val divideOrReturnError = Exception.failAsValue(classOf[ArithmeticException])("ERROR")

    divideOrReturnError(10 / 2) must be_===(5)

    divideOrReturnError(10 / 0) must be_===("ERROR")
  }

  "Example of Exception.catching and chaining of Exception.catching" in {
    def handleFormatErrorBySkipOrThrow(x: String) = catching(classOf[NumberFormatException]).
      withApply { e: Throwable => if (x == "skip") -1 else throw MathError(e) }

    val handleArithmeticError = catching(classOf[ArithmeticException]) withApply { e: Throwable => throw MathError(e) }

    (handleFormatErrorBySkipOrThrow("10") or handleArithmeticError)(10/2) must be_===(5)

    (handleFormatErrorBySkipOrThrow("skip") or handleArithmeticError)("skip".toInt/2) must be_===(-1)

    (handleFormatErrorBySkipOrThrow("10") or handleArithmeticError)(10/0) must throwA[MathError]

    
  }

  "Example of Exception.nonFatalCatch recovering with some other Exception" in {
    //catches all non fatal exceptions
    val divideOrThrowBusinessException = nonFatalCatch withApply { e: Throwable => throw SomeBusinessException(e) }

    divideOrThrowBusinessException(10 / 2) must be_===(5)

    divideOrThrowBusinessException(10/0) must throwA[SomeBusinessException]
  }

  "Example of Exception.nonFatalCatch in a chaining pattern" in {
    val recoverFromArithmeticException = Exception.failAsValue(classOf[ArithmeticException])(-1)
    val recoverFromNumberFormatException = Exception.failAsValue(classOf[NumberFormatException])(-2)
    val recoverFromNullPointerException = Exception.failAsValue(classOf[NullPointerException])(-3)
    val recoverFromAllOtherNonFatalExceptions = nonFatalCatch withApply { _ => -4 }

    val recoveringFromAllNonFatalExceptions =
      recoverFromArithmeticException or recoverFromNumberFormatException or recoverFromNullPointerException or recoverFromAllOtherNonFatalExceptions

    recoveringFromAllNonFatalExceptions(1/0) must be_===(-1)
    recoveringFromAllNonFatalExceptions(1/"zero".toInt) must be_===(-2)
    recoveringFromAllNonFatalExceptions(throw new NullPointerException) must be_===(-3)
    recoveringFromAllNonFatalExceptions(throw MathError(new Exception())) must be_===(-4)
  }

  "Examples of Exception.allCatch - catching all exceptions with our without recover function" in {
    Exception.allCatch(10/0) must throwAn[ArithmeticException]
    
    Exception.allCatch.withTry(10/0) must beFailedTry.withThrowable[ArithmeticException]

    Exception.allCatch.opt(10/0) must beNone
    Exception.allCatch.opt(-1) must beSome(-1)
    
  }


}
