package com.scalator

import scaldi.{Injector, Module}
import com.scalator.api.route._
import com.scalator.repository._
import com.scalator.repository.slick._
import scala.slick.jdbc.JdbcBackend._
import scala.slick.driver.JdbcDriver
import com.scalator.authentication.Authentication
import com.scalator.configuration.Configuration
import java.security.KeyStore
import java.io.FileInputStream
import com.scalator.service.PasswordService
import com.scalator.util.mail.Mailer
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import com.scalator.util.Password
import com.scalator.configuration.KeyStoreProvider

object Modules {
  val defaultModules: Injector = new ConfigModule :: new DefaultRepositoryModule :: new HttpModule :: new SslModule
}

class SslModule extends Module {
  lazy val config = inject[Configuration]

  bind[KeyStoreProvider] to {
    val keyStore = KeyStore.getInstance("jks")
    if (config.keyStoreFile.isDefined) {
      keyStore.load(new FileInputStream(config.keyStoreFile.get), config.keyStorePassword.get.toCharArray)
      KeyStoreProvider(keyStore, config.keyStorePassword.get)
    } else {
      keyStore.load(getClass.getClassLoader.getResourceAsStream(config.classPathKeyStoreResource), config.classPathKeyStorePassword.toCharArray)
      KeyStoreProvider(keyStore, config.classPathKeyStorePassword)
    }
  }
}

class ConfigModule extends Module {
  bind[Configuration] to new Configuration
  bind[AppInitializer] to new DefaultAppInitializer
  bind[Mailer] to new Mailer(inject[Configuration])
  bind[Password] to new Password()
}

class HttpModule extends Module {
  bind[PasswordService] to new PasswordService(inject[Mailer], inject[UserRepository], inject[Password])

  bind[Authentication] to new Authentication(inject[CredentialRepository], inject[UserRepository], inject[Password])
  bind[AuthRoute] to new AuthRoute(inject[Authentication])
  bind[AdminRoute] to new AdminRoute(inject[PasswordService], inject[UserRepository], inject[Authentication])

  bind[Routes] to new Routes(
    inject[AuthRoute],
    inject[AdminRoute])
}

class DefaultRepositoryModule extends Module {
  lazy val config = inject[Configuration]
  lazy val databaseConfig = config.databaseConfig

  lazy val database = {

    val config = new HikariConfig()
    config.setMaximumPoolSize(100);
    // TODO: setDatasourceClassname is supposed to be better
    config.setDriverClassName(databaseConfig.driverClass)
    config.setJdbcUrl(databaseConfig.jdbcUrl)
    config.setUsername(databaseConfig.username)
    config.setPassword(databaseConfig.password)

    val ds = new HikariDataSource(config);

    Database.forDataSource(ds)
  }

  lazy val driver: JdbcDriver = driverFromClassName(databaseConfig.driverClass)

  def driverFromClassName(driverClass: String): JdbcDriver = {
    if (config.databaseConfig.driverClass.contains("h2")) {
      import scala.slick.driver.H2Driver
      H2Driver
    } else if (config.databaseConfig.driverClass.contains("mysql")) {
      import scala.slick.driver.MySQLDriver
      MySQLDriver
    } else throw new RuntimeException("unknown driver")
  }

  lazy val userRepository: UserRepository = new SlickUserRepository(driver, database)
  lazy val credentialRepository: CredentialRepository = new SlickCredentialRepository(driver, database)

  bind[UserRepository] to userRepository
  bind[CredentialRepository] to credentialRepository
}