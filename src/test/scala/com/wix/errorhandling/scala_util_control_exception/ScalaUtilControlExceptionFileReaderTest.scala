package com.wix.errorhandling.scala_util_control_exception

import com.wix.errorhandling.contexts.FileReaderBaseTest
import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilControlExceptionFileReaderTest extends SpecWithJUnit with JMock with FileReaderBaseTest {

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
    val scalaUtilControlExceptionFileReader = new ScalaUtilControlExceptionFileReader(logger)
  }

}
