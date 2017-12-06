package com.wix.errorhandling.scala_util_try

import java.io.File

import com.wix.errorhandling.{FileReader, Logger}

import scala.io.{BufferedSource, Source}
import scala.util.Try

class ScalaUtilTryFileReader(logger: Logger) extends FileReader {

  override def read(file: String): String = {
    var input: BufferedSource = null

    Try {
      val f = new File(getClass.getClassLoader.getResource(file).getPath)
      input = Source.fromFile(f)
      input.mkString
    } recover { case e: Exception =>
      logger.log("Where is my file?")
      if (input != null) input.close()
      throw e
    } get
  }
}
