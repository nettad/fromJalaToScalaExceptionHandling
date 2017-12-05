package com.wix.errorhandling

trait DatabaseWriter {
  def write(id: Int, data: String): Unit
}
