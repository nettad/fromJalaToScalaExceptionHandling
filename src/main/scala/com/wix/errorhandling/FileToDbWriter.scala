package com.wix.errorhandling

trait FileToDbWriter {

  def saveFileContentsToDatabase(fileName: String): Unit
}


case class MissingFileException(msg: String) extends RuntimeException

