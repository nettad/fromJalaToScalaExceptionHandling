package com.wix.errorhandling

import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class FileReaderTest extends SpecificationWithJUnit with JMock {

  "PlainOldJalaFileReader" should {
    "read file successfully" in new ctx {
      plainOldJalaFileReader.read(file) must be_===(fileContent)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      plainOldJalaFileReader.read(missingFile) must throwAn[Exception]
    }
  }

  "ScalaUtilTryFileReader" should {
    "read file successfully" in new ctx {
      scalaUtilTryFileReader.read(file) must be_===(fileContent)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      scalaUtilTryFileReader.read(missingFile) must throwAn[Exception]
    }
  }

  "ScalaUtilControlExceptionFileReader" should {
    "read file successfully" in new ctx {
      scalaUtilControlExceptionFileReader.read(file) must be_===(fileContent)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      scalaUtilControlExceptionFileReader.read(missingFile) must throwAn[Exception]
    }
  }

  trait ctx extends Scope {
    private val logger = mock[Logger]

    val plainOldJalaFileReader = new PlainOldJalaFileReader(logger)
    val scalaUtilTryFileReader = new ScalaUtilTryFileReader(logger)
    val scalaUtilControlExceptionFileReader = new ScalaUtilControlExceptionFileReader(logger)

    val file = "file.txt"
    val missingFile = "missing-file.txt"
    val fileContent = "This file contains some random text. It's really not important what's written here."

    def expectingToLogUnsuccessfulFileRead() =
      checking {
        oneOf(logger).log("Where is my file?")
      }
  }
}
