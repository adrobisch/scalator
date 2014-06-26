package com.scalator.model

case class User(login:String, passwordHash:String, displayName: Option[String], email: Option[String], about: Option[String] = None)
