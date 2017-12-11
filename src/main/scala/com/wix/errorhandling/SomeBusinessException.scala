package com.wix.errorhandling

case class SomeBusinessException(e: Throwable) extends RuntimeException
