package com.wix.errorhandling

case class SomeMathBusinessException(e: Throwable) extends RuntimeException
