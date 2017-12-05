package com.wix.errorhandling

import scala.util.{Failure, Try}

class ScalaUtilTryExamples(fileReader: FileReader, userIdExtractor: UserIdExtractor, databaseWriter: DatabaseWriter) {

  def saveFileContentsToDatabase(fileName: String): Unit =
    (for {
      fileContents <- Try(fileReader.read(file = fileName))
      userId <- Try(userIdExtractor.extractFrom(fileContents))
    } yield databaseWriter.write(id = userId, data = fileContents)).get

  def saveFileContentsToDatabaseWithRecovery(fileName: String): Try[Unit] = {
    Try[Unit](fileReader.read(file = fileName))
      .recoverWith { case e: Exception => Failure(MissingFileException(e.toString)) }
  }

}


case class MissingFileException(msg: String) extends RuntimeException
