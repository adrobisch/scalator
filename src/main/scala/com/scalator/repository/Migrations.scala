package com.scalator.repository

import org.flywaydb.core.Flyway
import com.scalator.configuration.DatabaseConfig

object Migrations {

  def migrate(dbConfig: DatabaseConfig) = {
    val flyway = new Flyway
    flyway.setDataSource(dbConfig.jdbcUrl, dbConfig.username, dbConfig.password)
    flyway.migrate()
  }

}
