package com.scalator.repository.slick.tables

import scala.slick.driver.JdbcProfile
import com.scalator.model.{CredentialType, Credential}
import com.scalator.model.CredentialType.CredentialType

trait Credentials {
  val driver: JdbcProfile
  import driver.simple._
  import driver.MappedJdbcType

  class Credentials(tag: Tag) extends Table[Credential](tag, "T_CREDENTIALS") {
    def login = column[String]("LOGIN")
    def token = column[String]("TOKEN")
    def credentialType = column[CredentialType]("CREDENTIAL_TYPE")
    def validUntil = column[Long]("VALID_UNTIL")

    def * = (login, token, credentialType, validUntil) <> (Credential.tupled, Credential.unapply)
  }

  implicit val credentialTypeMapper = MappedJdbcType.base[CredentialType.Value, String](_.toString, CredentialType.withName)

  val credentials = TableQuery[Credentials]
}
