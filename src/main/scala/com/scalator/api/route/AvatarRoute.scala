package com.scalator.api.route

import akka.actor.ActorRefFactory
import com.scalator.util.rest.CachedResource
import scala.concurrent.duration.Duration
import com.scalator.configuration.Configuration

class AvatarRoute(configuration: Configuration)(implicit actorRefFactory: ActorRefFactory) extends CachedResource {
  import spray.routing.directives.PathDirectives._

  def cacheDuration = Duration(configuration.cacheDurationString)

  def route = {
    path("avatar" / Segment) { profileId =>
      cachedResource("img/avatar.png")
    }
  }

}
