package com.scalator.authentication

import spray.routing.directives.CookieDirectives
import spray.routing._
import spray.http.StatusCodes
import spray.routing.directives.RouteDirectives._
import com.scalator.repository.{CredentialRepository, UserRepository}
import spray.routing.directives.BasicDirectives._
import com.scalator.model.{Credential, CredentialType, AuthenticationInfo, User}
import com.scalator.util.Password
import scala.util.Random

class Authentication(val credentialRepository: CredentialRepository, val userRepository: UserRepository, val passwordHasher: Password) {
  import CookieDirectives._
  import Authentication.{authCookieName, authFromCookieValue}

  def createCredentialForPassword(login: String, password: String): Option[Credential] = {
    userRepository.findUserByLogin(login) match {
      case None => None

      case Some(user) => if(passwordHasher.matches(password, user.passwordHash)) {
        val newToken = Random.alphanumeric.take(10).mkString

        val credential = Credential(login, newToken, CredentialType.Login)
        credentialRepository.saveCredential(credential)

        Some(credential)
      } else None
    }
  }

  def requireUser: Directive1[User] = {
    user.flatMap {
      case None => complete(StatusCodes.Unauthorized)
      case Some(user) => provide(user)
    }
  }

  def user: Directive1[Option[User]] = {
    optionalCookie(authCookieName).flatMap {
      case None => provide(None)
      case Some(cookie) =>
        val auth = authFromCookieValue(cookie.content)
        credentialRepository.findByLoginAndToken(auth.login, auth.token, CredentialType.Login) match {
          case None => provide(None)
          case Some(credential) => provide(userRepository.findUserByLogin(auth.login))
        }
    }
  }

}

object Authentication {
  val authCookieName = "X-scalator-auth"
  val seperator = "#"

  def authToCookieValue(auth: AuthenticationInfo) = s"${auth.login}$seperator${auth.token}"

  def authFromCookieValue(cookieValue:String) = {
    val loginAndToken = cookieValue.split(seperator)
    AuthenticationInfo(loginAndToken(0), loginAndToken(1))
  }
}
