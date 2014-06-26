package com.scalator.repository.slick.tables

import scala.slick.driver.JdbcProfile
import com.scalator.model.User

trait Users {
  val driver: JdbcProfile
  import driver.simple._

  class Users(tag: Tag) extends Table[User](tag, "T_USERS") {
    def login = column[String]("LOGIN", O.PrimaryKey) // This is the primary key column
    def displayName = column[Option[String]]("DISPLAY_NAME")
    def passwordHash = column[String]("PASSWORD_HASH")
    def email = column[Option[String]]("EMAIL")
    def about = column[Option[String]]("ABOUT")

    def * = (login, passwordHash, displayName, email, about) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]
}
