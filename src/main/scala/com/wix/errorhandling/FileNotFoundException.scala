package com.wix.errorhandling

case class FileNotFoundException(file: String) extends RuntimeException
