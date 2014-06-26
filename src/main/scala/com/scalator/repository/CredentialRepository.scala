package com.scalator.repository

import com.scalator.model.CredentialType.CredentialType
import com.scalator.model.Credential

trait CredentialRepository {
  def saveCredential(credential: Credential)
  def findByLoginAndToken(login: String, token: String, credentialType: CredentialType): Option[Credential]
}