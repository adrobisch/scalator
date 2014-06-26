package com.scalator.util

import scala.util.Random
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKeyFactory
import java.math.BigInteger
import java.security.SecureRandom
import org.apache.commons.codec.binary.Hex

class Password {

  val pbkdf2Iterations = 2000
  val keyLength = 192

  def hash(plainPassword: String) = {
    randomSaltedHash(plainPassword)
  }

  def matches(plainPassword: String, hashAsHex: String): Boolean = {
    val salt = Hex.decodeHex(hashAsHex.toCharArray).take(8)
    hashWithSalt(plainPassword, salt).equals(hashAsHex)
  }

  def newRandom = {
    Random.alphanumeric.take(10).mkString
  }

  def randomSaltedHash(plainPassword: String): String = {
    val saltBytes = new Array[Byte](8)
    getSecureRandom.nextBytes(saltBytes)

    hashWithSalt(plainPassword, saltBytes)
  }

  protected def getSecureRandom = {
    new SecureRandom()
  }

  protected def hashWithSalt(plainPassword: String, salt: Array[Byte]) = {
    val passwordChars = plainPassword.toCharArray

    val pbkdf2Spec: PBEKeySpec = new PBEKeySpec(passwordChars, salt, pbkdf2Iterations, keyLength)

    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val hashedPassword = secretKeyFactory.generateSecret(pbkdf2Spec).getEncoded

    Hex.encodeHexString(salt).concat(Hex.encodeHexString(hashedPassword))
  }

}
