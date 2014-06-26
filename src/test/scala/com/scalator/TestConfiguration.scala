package com.scalator

import com.typesafe.config.{ConfigValueFactory, ConfigFactory}
import scala.util.Random
import com.scalator.configuration.Configuration

object TestConfiguration {
  def apply() = {
    new TestConfiguration
  }
}

class TestConfiguration extends Configuration {
  val dbName = Random.nextInt()

  override def config = {
    val fromFile = ConfigFactory.load("test.conf")
    fromFile.withValue("database.jdbcUrl",
      ConfigValueFactory.fromAnyRef(s"jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1"))
  }
}
