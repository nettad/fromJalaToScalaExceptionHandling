package com.wix.errorhandling

import java.io.File

import scala.io.{BufferedSource, Source}
import scala.util.Try
import scala.util.control.Exception._

trait FileReader {

  def read(file: String): String

}

class PlainOldJalaFileReader(logger: Logger) extends FileReader {
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

case class FileNotFoundException(file: String) extends RuntimeException
