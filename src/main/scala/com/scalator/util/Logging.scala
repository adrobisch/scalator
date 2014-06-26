package com.scalator.util

import org.slf4j.{LoggerFactory, Logger}

trait Logging {
  val log: Logger = LoggerFactory.getLogger(this.getClass)
}
