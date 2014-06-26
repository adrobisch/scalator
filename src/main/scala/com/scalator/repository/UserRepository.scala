package com.scalator.repository

import com.scalator.model.User

trait UserRepository extends Repository {
  def findUserByLogin(login: String): Option[User]
  def findUserByMail(email: String): Option[User]
  def addUser(user: User)
  def updatePasswordHash(login: String, newPasswordHash: String)
  def updatePersonalInformation(login: String, email: String, about: String)
}
