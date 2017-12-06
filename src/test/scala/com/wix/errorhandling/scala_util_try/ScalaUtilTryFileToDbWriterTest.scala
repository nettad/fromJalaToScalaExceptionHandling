package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling._
import com.wixpress.common.specs2.JMock
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ScalaUtilTryFileToDbWriterTest extends SpecWithJUnit with JMock {

  //for-loop comprehension example
  "ScalaUtilTryFileToDbWriter" should {
    "save files contents to db" in new ctx {
      expectingToSaveFileContentsToDatabase(input = file)
      scalaUtilTryExamples.saveFileContentsToDatabase(file)
    }

    "throw FileNotFoundException when file is missing" in new ctx {
      givenMissingFile(file)
      scalaUtilTryExamples.saveFileContentsToDatabase(file) must throwA[FileNotFoundException]
    }
  }


  trait ctx extends Scope {
    private val databaseWriter = mock[DatabaseWriter]
    private val fileReader = mock[FileReader]
    private val userIdExtractor = mock[UserIdExtractor]
    val file = "file.txt"
    val userId = 1
    val fileContents = s"userId=$userId"

    val scalaUtilTryExamples = new ScalaUtilTryFileToDbWriter(fileReader, userIdExtractor, databaseWriter)

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
                                                         