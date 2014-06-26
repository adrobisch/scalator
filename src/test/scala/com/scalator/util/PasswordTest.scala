package com.scalator.util

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import java.security.SecureRandom

class PasswordTest extends Specification with Mockito {
  "Password" should {
    "calculate hash with PBKDF2" in {
      passwordWithRandomMock.hash("password") must beEqualTo("73616c7473616c74c133b3c880c20901da525d0803af3e85ed9b55f0890a8143")
    }

    "check if password matches hash" in {
      passwordWithRandomMock.matches("password", passwordWithRandomMock.hash("password")) must beTrue
    }
  }

  val passwordWithRandomMock = {
    new Password() {
      override def getSecureRandom = {
        val mockedRandom = mock[SecureRandom]
        mockedRandom.nextBytes(any[Array[Byte]]) answers { array =>
          "saltsalt".getBytes.copyToArray(array.asInstanceOf[Array[Byte]])
        }
        mockedRandom
      }
    }
  }
}
