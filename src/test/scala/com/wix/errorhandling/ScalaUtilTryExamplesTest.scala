package com.wix.errorhandling

import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryExamplesTest extends SpecificationWithJUnit with JMock {

  "Tries can be used in for-loop comprehension" >> {
    "successfully" in new ctx {
      expectingToSaveFileContentsToDatabase(input = file)
      scalaUtilTryExamples.saveFileContentsToDatabase(file)
    }

    "to handle failures" in new ctx {
      givenMissingFile(file)
      scalaUtilTryExamples.saveFileContentsToDatabase(file) must throwA[FileNotFoundException]
    }
  }

  "Tries can be used replace one failure with another" in new ctx {
    givenMissingFile(file)
    scalaUtilTryExamples.saveFileContentsToDatabaseWithRecovery(file) must beAFailedTry.withThrowable[MissingFileException]
  }



  trait ctx extends Scope {
    private val databaseWriter = mock[DatabaseWriter]
    private val fileReader = mock[FileReader]
    private val userIdExtractor = mock[UserIdExtractor]
    val file = "file.txt"

    val scalaUtilTryExamples = new ScalaUtilTryExamples(fileReader, userIdExtractor, databaseWriter)

    def expectingToSaveFileContentsToDatabase(input: String) = {
      val userId = 1
      val fileContents = s"userId=$userId"

      checking {
        allowing(fileReader).read(input) willReturn fileContents
        allowing(userIdExtractor).extractFrom(fileContents) willReturn userId
        oneOf(databaseWriter).write(userId, data = fileContents)
      }
    }

    def givenMissingFile(file: String) =
      checking {
        allowing(fileReader).read(file) willThrow FileNotFoundException(file)
      }
  }

}
                                                         