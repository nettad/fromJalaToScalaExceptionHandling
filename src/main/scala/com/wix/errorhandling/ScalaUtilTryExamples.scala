package com.wix.errorhandling

import scala.util.Try

class ScalaUtilTryExamples(fileReader: FileReader, userIdExtractor: UserIdExtractor, databaseWriter: DatabaseWriter) {

  def saveFileContentsToDatabase(fileName: String): Unit =
    (for {
      fileContents <- Try(fileReader.read(file = fileName))
      userId <- Try(userIdExtractor.extractFrom(fileContents))
    } yield databaseWriter.write(id = userId, data = fileContents)).get

}
