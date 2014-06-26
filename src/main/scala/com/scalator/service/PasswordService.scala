package com.scalator.service

import com.scalator.model.{PasswordChange, User}
import com.scalator.repository.UserRepository
import com.scalator.util.Password
import com.scalator.util.mail.{Mailer, Mail}

class PasswordService(mailer: Mailer, userRepository: UserRepository, val password: Password) {
  def changePossible(user: User, passwordChange: PasswordChange): Boolean = {
    userRepository.findUserByLogin(user.login) match {
      case None => false
      case Some(foundUser) => password.matches(passwordChange.currentPassword, foundUser.passwordHash)
    }
  }

  def changePassword(user: User, passwordChange: PasswordChange) = {
    userRepository.updatePasswordHash(user.login, password.hash(passwordChange.newPassword))
  }

  def resetPassword(user: User) = {
    val newPassword = password.newRandom
    val newPasswordHash = password.hash(newPassword)

    user.email.map { email =>
      val resetMail = Mail(
        from = "info@noreply.com" -> "Password Service",
        to = Seq(email),
        subject = "Password reset",
        message = s"Your new password is $newPassword")

      mailer.send(resetMail)

      userRepository.updatePasswordHash(user.login, newPasswordHash)
    }
  }
}
