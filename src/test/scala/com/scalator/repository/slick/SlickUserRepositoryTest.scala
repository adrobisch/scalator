package com.scalator.repository.slick

import org.specs2.mutable.Specification

import com.scalator.TestConfiguration

class SlickUserRepositoryTest extends Specification {

  "Slick user repository" should {
    "add user" in new InMemoryDb(TestConfiguration()) {
      userRepository.findUserByLogin(testUser.login) mustEqual Some(testUser)
    }

    "update password hash for user" in new InMemoryDb(TestConfiguration()) {
      userRepository.updatePasswordHash(testUser.login, "newHash")
      userRepository.findUserByLogin(testUser.login).get.passwordHash mustEqual "newHash"
    }

    "update personal information for user" in new InMemoryDb(TestConfiguration()) {
      userRepository.updatePersonalInformation(testUser.login, "mailAddress", "about")

      val updatedUser = userRepository.findUserByLogin(testUser.login).get
      updatedUser.email mustEqual Some("mailAddress")
      updatedUser.about mustEqual Some("about")
    }
  }

}


