package com.wix.errorhandling

trait Logger {
  def log(msg: String): Unit
}

class ConsoleLogger() extends Logger {

  override def log(msg: String): Unit =
    println(msg)

}
