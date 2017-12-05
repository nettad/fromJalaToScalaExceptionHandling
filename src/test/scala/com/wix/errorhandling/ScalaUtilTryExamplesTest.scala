package com.wix.errorhandling

import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryExamplesTest extends SpecWithJUnit with JMock {

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


/*  "Tries can be transformed successfully" in new ctx {
    given(fileContents, forFile = file)
    scalaUtilTryExamples.mapUserToFile(userId, file) must be_===(Map(userId -> fileContents))
  }

  "Tries can be transformed in the event of a failure" in new ctx {
    givenMissingFile(file)
    scalaUtilTryExamples.mapUserToFile(userId, file) must beEmpty
  }*/





  trait ctx extends Scope {
    private val databaseWriter = mock[DatabaseWriter]
    private val fileReader = mock[FileReader]
    private val userIdExtractor = mock[UserIdExtractor]
    val file = "file.txt"
    val userId = 1
    val fileContents = s"userId=$userId"

    val scalaUtilTryExamples = new ScalaUtilTryExamples(fileReader, userIdExtractor, databaseWriter)

    def expectingToSaveFileContentsToDatabase(input: String) = {

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

    def given(fileContents: String, forFile: String) =
      checking {
        allowing(fileReader).read(forFile) willReturn fileContents
      }
  }

}
                                                         