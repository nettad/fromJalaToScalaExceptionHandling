package com.wix.errorhandling

case class MathError(e: Throwable) extends RuntimeException
