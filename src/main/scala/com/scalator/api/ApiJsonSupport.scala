package com.scalator.api

import spray.httpx.Json4sSupport
import org.json4s.{FieldSerializer, Formats}
import com.scalator.util.rest.Representation
import com.scalator.model.User
import org.json4s.FieldSerializer._
import com.scalator.model.User

trait ApiJsonSupport extends Json4sSupport {
  override implicit def json4sFormats: Formats = org.json4s.DefaultFormats +
    Representation.serializer + new FieldSerializer[User](ignore("passwordHash"))
}
