package com.wix.errorhandling.contexts

import com.wix.errorhandling.Logger
import com.wixpress.common.specs2.JMock

trait FileReaderBaseTest { this: JMock =>

  protected val logger = mock[Logger]

  val file = "file.txt"
  val missingFile = "missing-file.txt"
  val fileContent = "This file contains some random text. It's really not important what's written here."

  def expectingToLogUnsuccessfulFileRead() =
    checking {
      oneOf(logger).log("Where is my file?")
    }

}
