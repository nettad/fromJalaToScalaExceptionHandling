package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling._

import scala.util.{Failure, Try}


class ScalaUtilTryFileToDbWriter(fileReader: FileReader, userIdExtractor: UserIdExtractor, databaseWriter: DatabaseWriter) extends FileToDbWriter {

  override def saveFileContentsToDatabase(fileName: String): Unit =
    (for {
      fileContents <- Try(fileReader.read(file = fileName))
      userId <- Try(userIdExtractor.extractFrom(fileContents))
    } yield databaseWriter.write(id = userId, data = fileContents)).get

  def saveFileContentsToDatabaseWithRecovery(fileName: String): Try[Unit] = {
    Try[Unit](fileReader.read(file = fileName))
      .recoverWith { case e: Exception => Failure(MissingFileException(e.toString)) }
  }

}
