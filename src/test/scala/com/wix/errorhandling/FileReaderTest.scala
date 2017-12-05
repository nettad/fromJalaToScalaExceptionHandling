package com.wix.errorhandling

import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class FileReaderTest extends SpecificationWithJUnit with JMock {

  "PlainOldJalaFileReader" should {
    "read file successfully" in new ctx {
      plainOldJalaFileReader.read("file.txt") must be_===(fileContents)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      plainOldJalaFileReader.read("missing-file.txt") must throwA[Exception]
    }
  }

  "ScalaUtilTryFileReader" should {
    "read file successfully" in new ctx {
      scalaUtilTryFileReader.read("file.txt") must be_===(fileContents)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      scalaUtilTryFileReader.read("missing-file.txt") must throwA[Exception]
    }
  }

  "ScalaUtilControlExceptionFileReader" should {
    "read file successfully" in new ctx {
      scalaUtilControlExceptionFileReader.read("file.txt") must be_===(fileContents)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      scalaUtilControlExceptionFileReader.read("missing-file.txt") must throwA[Exception]
    }
  }

  trait ctx extends Scope {
    private val logger = mock[Logger]

    val fileContents = "This file contains some random text. It's really not important what's written here."

    val plainOldJalaFileReader = new PlainOldJalaFileReader(logger)

    val scalaUtilTryFileReader = new ScalaUtilTryFileReader(logger)

    val scalaUtilControlExceptionFileReader = new ScalaUtilControlExceptionFileReader(logger)

    def expectingToLogUnsuccessfulFileRead() =
      checking {
        oneOf(logger).log("Where is my file?")
      }
  }
}
