package com.wix.errorhandling

import java.io.File

import org.specs2.mutable.SpecWithJUnit

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}
import GreaterThanEvaluator._

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
      Try(howMuchGreater(isX = "one", thanY = "1")) recoverWith { case _: NumberFormatException => Success(-1) } must beSuccessfulTry(-1)
    }

    "option 2 recoverWith to change Failure to another Failure" in {
      Try(howMuchGreater(isX = "one", thanY = "1")) recoverWith { case e: NumberFormatException => Failure(SomeBusinessException(e)) } must beFailedTry.withThrowable[SomeBusinessException]
    }

    "option 3 recover to change Failure to Success" in {
      Try(howMuchGreater(isX = "one", thanY = "1")) recover { case _: NumberFormatException => -1 } must beSuccessfulTry(-1)
    }

    "option 4 recoverWith to change Failure to another Failure" in {
      Try(howMuchGreater(isX = "one", thanY = "1")) recover { case e: NumberFormatException => throw SomeBusinessException(e) } must beFailedTry.withThrowable[SomeBusinessException]
    }

  }

  "Example Try in a for-loop comprehension" in {
    val result = for {
      x <- Try(howMuchGreater(isX = "10", thanY = "2"))
      y <- Try(howMuchGreater(isX = "6", thanY = "3"))
    } yield x + y

    result must beSuccessfulTry(11)

  }

  "Example matching on a Try" >> {
    "matching SuccessfulTry" in {
      val result = Try(howMuchGreater(isX = "10", thanY = "2")) match {
        case Success(x) => x
        case Failure(_) => -1
      }

      result must be_===(8)
    }

    "matching Failure" in {
      val result = Try(howMuchGreater(isX = "one", thanY = "1")) match {
        case Success(x) => x
        case Failure(_) => -1
      }

      result must be_===(-1)
    }
  }

  "Example Try using get" in {
    Try(howMuchGreater(isX = "one", thanY = "1")).get must throwAn[NumberFormatException]
  }

  "Example Try using get or else for default value" in {

    Try(howMuchGreater(isX = "one", thanY = "1")) getOrElse -1 must be_===(-1)
  }



}


