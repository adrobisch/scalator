package com.scalator.repository.slick

import com.scalator.repository.UserRepository
import com.scalator.model.User

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend.Database
import com.scalator.repository.slick.tables.Users

class SlickUserRepository(val driver: JdbcProfile, val database: Database) extends UserRepository with Users {
  import driver.simple._

  override def updatePasswordHash(login: String, newPasswordHash: String): Unit = database withSession { implicit session =>
    (for(user <- users if user.login === login) yield user.passwordHash).update(newPasswordHash)
  }

  override def updatePersonalInformation(login: String, email: String, about: String): Unit = database withSession { implicit session =>
    users.filter(_.login === login).map(user => (user.email, user.about)).update((Some(email), Some(about)))
  }

  override def addUser(user: User): Unit = database withSession { implicit session =>
    users.insert(user)
  }

  override def findUserByLogin(login: String): Option[User] = database withSession { implicit session =>
    (for(user <- users if user.login === login) yield user).firstOption
  }

  override def findUserByMail(email: String): Option[User] = database withSession { implicit session =>
    (for(user <- users if user.email === email) yield user).firstOption
  }
}
