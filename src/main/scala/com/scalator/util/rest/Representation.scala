package com.scalator.util.rest

import org.json4s.FieldSerializer
import org.json4s.FieldSerializer._

class Representation(var links: Map[String, String] = Map()) {
  def withLinks(l: (String, String)*): Representation = {
    links ++= l
    this
  }
}

object Representation {
  def apply(links: Map[String, String] = Map()) = {
    new Representation(links)
  }

  def serializer = new FieldSerializer[Representation](renameTo("links","_links"), renameFrom("_links", "links"))
}
