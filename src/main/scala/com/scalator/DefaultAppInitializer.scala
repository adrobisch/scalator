package com.scalator

import scaldi.{Injectable, Injector}
import org.slf4j.LoggerFactory
import com.scalator.model.User
import com.scalator.util.Password
import com.scalator.repository.{Migrations, UserRepository}
import com.scalator.configuration.{DatabaseConfig, Configuration}

class DefaultAppInitializer(implicit injector: Injector) extends AppInitializer with Injectable {
  val log = LoggerFactory.getLogger("AppInitializer")
  val passwordGenerator = inject[Password]

  val adminUser: User = User("admin", passwordGenerator.hash("admin"), Some("admin"), None)
  val demoUser: User = User("demo", passwordGenerator.hash("demo"), Some("demo"), None)

  override def init(): Unit = {
    val configuration = inject[Configuration]
    val userRepository = inject[UserRepository]

    applyDbMigrations(configuration.databaseConfig)
    addInitialUsers(userRepository)
  }

  def applyDbMigrations(dbConfig: DatabaseConfig) {
    Migrations.migrate(dbConfig)
  }

  def addInitialUsers(userRepository: UserRepository) = {
    if (userRepository.findUserByLogin(adminUser.login).isEmpty) {
      log.info(s"adding initial user: $adminUser")
      userRepository.addUser(adminUser)
      userRepository.addUser(demoUser)
    }
  }

}
