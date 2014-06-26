package com.scalator.configuration

import com.typesafe.config.ConfigFactory
import java.io._
import com.scalator.util.{IoUtil, Logging}
import java.security.KeyStore

case class DatabaseConfig(jdbcUrl: String, driverClass: String, username: String, password: String)
case class KeyStoreProvider(keystore: KeyStore, keystorePassword: String)

class Configuration extends Logging {
  def config = ConfigFactory.parseFile(configFile)

  def cacheDurationString = if(config.hasPath("cache.duration"))
    config.getString("cache.duration") else "6 h"

  def databaseConfig = DatabaseConfig(jdbcUrl = config.getString("database.jdbcUrl"),
    driverClass = config.getString("database.driverClass"),
    username = config.getString("database.username"),
    password = config.getString("database.password"))

  def keyStoreFile: Option[String] = if (config.hasPath("ssl.keystore"))
    Some(config.getString("ssl.keystore")) else None

  def keyStorePassword: Option[String] = if (config.hasPath("ssl.keystorepassword"))
    Some(config.getString("ssl.keystorepassword")) else Some(classPathKeyStorePassword)

  def classPathKeyStoreResource = "keystore.jks"
  def classPathKeyStorePassword = "password"

  def httpPort: Int = config.getInt("http.port")
  def httpInterface: String = config.getString("http.interface")

  def smtpHost: String = config.getString("mail.host")
  def smtpPort: Int = config.getInt("mail.port")
  def smtpUsername: String = config.getString("mail.username")
  def smtpPassword: String = config.getString("mail.password")

  def getOrCreateConfigFolder : File = {
    val configFolder = new File(System.getProperty("user.home") + File.separatorChar + ".scalator")
    configFolder.mkdir()
    configFolder
  }

  def configFile = {
    val file = new File(getOrCreateConfigFolder.getAbsolutePath + File.separatorChar + "application.conf")
    if (!file.exists()) {
      file.createNewFile()
      IoUtil.writeInputToOutput(getClass.getClassLoader.getResourceAsStream("config/initial.conf"), new FileOutputStream(file))
    }
    file
  }

}