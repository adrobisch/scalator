package com.scalator.repository

import scaldi.Module
import com.scalator.model.{Credential, CredentialType, User}
import org.specs2.mock.Mockito

class MockedRepositoryModule extends Mockito with Module {
  val userRepository = mock[UserRepository]
  val credentialRepository = mock[CredentialRepository]

  val testUser = User("test", "passwordHash", None, None)

  userRepository.findUserByLogin(any[String]) returns Some(testUser)

  credentialRepository.findByLoginAndToken("test", "token", CredentialType.Login) returns
    Some(Credential("test", "token", CredentialType.Login))

  bind[CredentialRepository] to credentialRepository
  bind[UserRepository] to userRepository
}
