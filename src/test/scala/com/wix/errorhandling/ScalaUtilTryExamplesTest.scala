package com.wix.errorhandling

import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryExamplesTest extends SpecificationWithJUnit with JMock {

  "Tries can be used in for-loop comprehension" in new ctx {
    scalaUtilTryExamples.saveFileContentsToDatabase(file)
  }

  trait ctx extends Scope {
    val databaseWriter = mock[DatabaseWriter]
    val fileReader = mock[FileReader]
    val file = "file.txt"
    val fileContents = "blah blah blah"

    val scalaUtilTryExamples = new ScalaUtilTryExamples(fileReader, databaseWriter)

    private def expectingToSaveFileContentsToDatabase(file: String) =
      checking {
        allowing(fileReader).read(file) willReturn fileContents
        oneOf(databaseWriter).write(id = 1, data = fileContents)
      }
  }

}
