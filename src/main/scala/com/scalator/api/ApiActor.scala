package com.scalator.api

import akka.actor.{ActorLogging, Actor}
import com.scalator.api.route._
import com.scalator.configuration.Configuration

class ApiActor(routes: Routes, configuration: Configuration) extends Api(routes, configuration: Configuration) with Actor with ActorLogging {
  implicit def actorRefFactory = context

  def receive = runRoute(route)
}
