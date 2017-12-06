package com.wix.errorhandling.jala

import java.io.File

import com.wix.errorhandling.{FileReader, Logger}

import scala.io.{BufferedSource, Source}

class JalaFileReader(logger: Logger) extends FileReader {
  override def read(file: String): String = {
    var input: BufferedSource = null
    try {
      val f = new File(getClass.getClassLoader.getResource(file).getPath)
      input = Source.fromFile(f)
      input.mkString
    } catch {
      case _: Exception =>
        logger.log("Where is my file?")
        throw new Exception(file)
    } finally {
      if (input != null) input.close()
    }
  }
}
