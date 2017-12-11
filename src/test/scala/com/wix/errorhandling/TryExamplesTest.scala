package com.wix.errorhandling

import java.io.File

import org.specs2.mutable.SpecWithJUnit

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

class TryExamplesTest extends SpecWithJUnit {
  sequential

  "Example of how to use the basic Try construct to read a file" in {
    val file = "file.txt"
    var input: BufferedSource = null

    val triedFileAsString = Try {
      val f = new File(getClass.getClassLoader.getResource(file).getPath)
      input = Source.fromFile(f)
      input.mkString
    }

    if (input != null) input.close()

    triedFileAsString must beSuccessfulTry("This file contains some random text. It's really not important what's written here.")
  }

  "Example of how to use the basic Try construct to read a missing file" in {
    val file = "missing.txt"
    var input: BufferedSource = null

    val triedFileAsString = Try {
      val resource = getClass.getClassLoader.getResource(file)
      if (resource == null) throw FileNotFoundException(file)
      val f = new File(resource.getPath)
      input = Source.fromFile(f)
      input.mkString
    }

    if (input != null) input.close()

    triedFileAsString must beFailedTry.withThrowable[FileNotFoundException]
  }

  "Example of how to recover from a Failed Try" >> {
    "option 1 recoverWith to change Failure to Success" in {

      Try(1 / 0).recoverWith { case _: ArithmeticException => Success(-1) } must beSuccessfulTry
    }

    "option 2 recoverWith to change Failure to another Failure" in {
      Try(1 / 0).recoverWith { case _: ArithmeticException => Failure(SomeMathBusinessException()) } must beFailedTry.withThrowable[SomeMathBusinessException]
    }

    "option 3 recover to change Failure to Success" in {

      Try(1 / 0).recover { case _: ArithmeticException => -1 } must beSuccessfulTry
    }

    "option 4 recoverWith to change Failure to another Failure" in {
      Try(1 / 0).recover { case _: ArithmeticException => throw SomeMathBusinessException() } must beFailedTry.withThrowable[SomeMathBusinessException]
    }


  }



}


