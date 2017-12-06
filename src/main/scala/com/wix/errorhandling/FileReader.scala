package com.wix.errorhandling


trait FileReader {

  def read(file: String): String

}

case class FileNotFoundException(file: String) extends RuntimeException
