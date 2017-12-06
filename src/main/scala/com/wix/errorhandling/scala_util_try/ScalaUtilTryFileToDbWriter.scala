package com.wix.errorhandling.scala_util_try

import com.wix.errorhandling._

import scala.util.{Failure, Try}


class ScalaUtilTryFileToDbWriter(fileReader: FileReader, userIdExtractor: UserIdExtractor, databaseWriter: DatabaseWriter) extends FileToDbWriter {

  override def saveFileContentsToDatabase(fileName: String): Unit =
    (for {
      fileContents <- Try(fileReader.read(file = fileName))
      userId <- Try(userIdExtractor.extractFrom(fileContents))
    } yield databaseWriter.write(id = userId, data = fileContents)).get

}
