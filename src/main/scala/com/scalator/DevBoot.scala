package com.scalator

import com.scalator.util.Logging
import scaldi.{Module, Injectable}
import com.typesafe.config.ConfigFactory
import com.scalator.configuration.Configuration

class DevConfig extends Configuration {
  override def config = ConfigFactory.load("config/dev.conf")
}

object DevBoot extends App with Logging with Injectable {
  implicit val modules = new Module {
    bind[Configuration] to new DevConfig
  } :: Modules.defaultModules

  new Boot().start()
}