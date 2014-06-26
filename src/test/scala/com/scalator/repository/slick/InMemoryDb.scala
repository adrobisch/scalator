package com.scalator.repository.slick

import org.specs2.specification.Scope
import org.specs2.mutable.Before
import scala.util.Random
import org.slf4j.LoggerFactory
import com.scalator.configuration.Configuration
import com.scalator.model.User
import org.flywaydb.core.Flyway

class InMemoryDb(val configuration: Configuration) extends Scope with Before {
  import scala.slick.jdbc.JdbcBackend.Database
  import scala.slick.driver.H2Driver

  lazy val jdbcUrl = s"jdbc:h2:mem:${Random.nextInt()};DB_CLOSE_DELAY=-1"

  lazy val database = Database.forURL(jdbcUrl, driver = configuration.databaseConfig.driverClass)
  lazy val userRepository = new SlickUserRepository(H2Driver, database)
  lazy val credentialRepository = new SlickCredentialRepository(H2Driver, database)

  lazy val log = LoggerFactory.getLogger(classOf[InMemoryDb])

  lazy val testUser = User("test", "test-hash", Some("Test User"), Some("mailHash"))
  lazy val demoUser = User("demo", "demo-hash", Some("Demo User"), Some("mailHash"))

  def before = {
    val flyway = new Flyway
    flyway.setDataSource(jdbcUrl, "", "")
    flyway.migrate()

    userRepository.addUser(testUser)
    userRepository.addUser(demoUser)
  }
}
