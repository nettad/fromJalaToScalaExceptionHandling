package com.wix.errorhandling

import java.io.File

import com.wix.errorhandling.GreaterThanEvaluator.howMuchGreater
import org.specs2.mutable.SpecWithJUnit

import scala.io.{BufferedSource, Source}
import scala.util.control.Exception
import scala.util.control.Exception.{Catch, catching, handling, nonFatalCatch, ultimately}

class ScalaUtilControlExampleTest extends SpecWithJUnit {
  sequential

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
    val divideOrReturnError = Exception.failAsValue(classOf[NumberFormatException])("ERROR")

    divideOrReturnError(howMuchGreater(isX = "10", thanY = "5")) must be_===(5)

    divideOrReturnError(howMuchGreater(isX = "one", thanY = "1")) must be_===("ERROR")
  }

  "Example of Exception.catching and chaining of Exception.catching" in {
    def handleFormatErrorBySkipOrThrow(x: String) = catching(classOf[NumberFormatException]).
      withApply { e: Throwable => if (x == "skip") -1 else throw MathError(e) }

    val handleXIsLessThanYException = catching(classOf[XIsLessThanYException]) withApply { e: Throwable => throw MathError(e) }

    (handleFormatErrorBySkipOrThrow("10") or handleXIsLessThanYException)(howMuchGreater(isX = "10", thanY = "5")) must be_===(5)

    (handleFormatErrorBySkipOrThrow("skip") or handleXIsLessThanYException)(howMuchGreater(isX = "skip", thanY = "1")) must be_===(-1)

    (handleFormatErrorBySkipOrThrow("10") or handleXIsLessThanYException)(howMuchGreater(isX = "1", thanY = "10")) must throwA[MathError]
  }

  "Example of Exception.nonFatalCatch recovering with some other Exception" in {
    //catches all non fatal exceptions
    val divideOrThrowBusinessException = nonFatalCatch withApply { e: Throwable => throw SomeBusinessException(e) }

    divideOrThrowBusinessException(howMuchGreater(isX = "10", thanY = "5")) must be_===(5)

    divideOrThrowBusinessException(howMuchGreater(isX = "one", thanY = "1")) must throwA[SomeBusinessException]
  }


  "Example of Exception.nonFatalCatch in a chaining pattern" in {
    val recoverFromXIsLessThanYException = Exception.failAsValue(classOf[XIsLessThanYException])(-1)
    val recoverFromNumberFormatException = Exception.failAsValue(classOf[NumberFormatException])(-2)
    val recoverFromNullPointerException = Exception.failAsValue(classOf[NullPointerException])(-3)
    val recoverFromAllOtherNonFatalExceptions = nonFatalCatch withApply { _ => -4 }

    val recoveringFromAllNonFatalExceptions =
      recoverFromXIsLessThanYException or recoverFromNumberFormatException or recoverFromNullPointerException or recoverFromAllOtherNonFatalExceptions

    recoveringFromAllNonFatalExceptions(howMuchGreater(isX = "1", thanY = "10")) must be_===(-1)
    recoveringFromAllNonFatalExceptions(howMuchGreater(isX = "one", thanY = "1")) must be_===(-2)
    recoveringFromAllNonFatalExceptions(throw new NullPointerException) must be_===(-3)
    recoveringFromAllNonFatalExceptions(throw MathError(new Exception())) must be_===(-4)
  }

  "Examples of Exception.allCatch - catching all exceptions with our without recover function" in {
    Exception.allCatch(howMuchGreater(isX = "one", thanY = "1")) must throwAn[NumberFormatException]
    
    Exception.allCatch.withTry(howMuchGreater(isX = "one", thanY = "1")) must beFailedTry.withThrowable[NumberFormatException]

    Exception.allCatch.opt(howMuchGreater(isX = "one", thanY = "1")) must beNone
    Exception.allCatch.opt(howMuchGreater(isX = "10", thanY = "1")) must beSome(9)
    
  }


}
