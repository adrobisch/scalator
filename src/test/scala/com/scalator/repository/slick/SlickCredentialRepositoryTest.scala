package com.scalator.repository.slick

import org.specs2.mutable.Specification
import com.scalator.TestConfiguration
import com.scalator.model.{User, CredentialType, Credential}

class SlickCredentialRepositoryTest extends Specification {
  "Credential Repository" should {
    "save credentials and find it" in new InMemoryDb(TestConfiguration()) {
      val credential = Credential("test", "token", CredentialType.Login)

      credentialRepository.saveCredential(credential)

      credentialRepository.findByLoginAndToken("test", "token", CredentialType.Login).isDefined
    }
  }
}
