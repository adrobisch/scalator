package com.scalator.api.route

import akka.actor.ActorRefFactory
import com.scalator.util.rest.CachedResource
import scala.concurrent.duration.Duration
import com.scalator.configuration.Configuration
import spray.http.StatusCodes

class AssetRoute(configuration: Configuration)(implicit actorRefFactory: ActorRefFactory) extends CachedResource {
  import spray.routing.directives.PathDirectives._
  import spray.routing.RouteConcatenation._
  import spray.routing.directives.MethodDirectives._
  import spray.routing.directives.RouteDirectives._

  val indexPage = "index.html"

  def cacheDuration = Duration(configuration.cacheDurationString)

  def route = {
    path("") {
      get(redirect(s"/web/$indexPage", StatusCodes.MovedPermanently))
    } ~ path("web" / Rest)(path => cachedResource(path)) ~
      path("vendor" / Rest)(path => cachedResource(path, "vendor"))
  }

}
