package com.scalator.repository.slick

import com.scalator.repository.CredentialRepository
import com.scalator.model.Credential

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend.Database
import com.scalator.model.CredentialType.CredentialType
import com.scalator.repository.slick.tables.Credentials

class SlickCredentialRepository(val driver: JdbcProfile, val database: Database) extends CredentialRepository with Credentials {
  import driver.simple._

  override def findByLoginAndToken(login: String, token: String, credentialType: CredentialType): Option[Credential] = database withSession { implicit session =>
    (for (credential <- credentials if credential.login === login) yield credential).firstOption
  }

  override def saveCredential(credential: Credential): Unit = database withSession { implicit session =>
    credentials.insert(credential)
  }
}
