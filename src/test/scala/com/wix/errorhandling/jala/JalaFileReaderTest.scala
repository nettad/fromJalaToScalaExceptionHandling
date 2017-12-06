package com.wix.errorhandling.jala

import com.wix.errorhandling.contexts.FileReaderBaseTest
import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class JalaFileReaderTest extends SpecWithJUnit with JMock with FileReaderBaseTest {

  "JalaFileReader" should {
    "read file successfully" in new ctx {
      jalaFileReader.read(file) must be_===(fileContent)
    }

    "log unsuccessful attempts to read file" in new ctx {
      expectingToLogUnsuccessfulFileRead()
      jalaFileReader.read(missingFile) must throwAn[Exception]
    }
  }


  trait ctx extends Scope {
    val jalaFileReader = new JalaFileReader(logger)
    
  }

}
