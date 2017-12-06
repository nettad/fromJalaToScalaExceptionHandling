package com.wix.errorhandling.scala_util_control_exception

import java.io.File

import com.wix.errorhandling.{FileReader, Logger}

import scala.io.{BufferedSource, Source}
import scala.util.control.Exception.{Catch, handling, ultimately}

class ScalaUtilControlExceptionFileReader(logger: Logger) extends FileReader {
  //Notice how logExceptionsAndThrow can be reused

  override def read(file: String): String =
    logExceptionsAndThrow {
      val fileResource = new File(getClass.getClassLoader.getResource(file).getPath)
      process(file = Source.fromFile(fileResource))
    }

  private def process(file: BufferedSource) =
    closeFileWhenDone(file) {
      logExceptionsAndThrow {
        file.mkString
      }
    }

  private def closeFileWhenDone[T](input: BufferedSource): Catch[T] =
    ultimately[T] {
      if (input != null) input.close
    }

  private def logExceptionsAndThrow = handling(classOf[Exception]).by { e =>
    logger.log("Where is my file?")
    throw e
  }

}
