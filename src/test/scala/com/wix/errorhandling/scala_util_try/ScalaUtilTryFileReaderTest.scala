package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling.contexts.FileReaderBaseTest
import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryFileReaderTest extends SpecWithJUnit with JMock with FileReaderBaseTest {


  "ScalaUtilTryFileReader" should {
    "read file successfully" in new ctx {
      scalaUtilTryFileReader.read(file) must be_===(fileContent)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      scalaUtilTryFileReader.read(missingFile) must throwAn[Exception]
    }
  }

  trait ctx extends Scope {
    val scalaUtilTryFileReader = new ScalaUtilTryFileReader(logger)
  }

}
