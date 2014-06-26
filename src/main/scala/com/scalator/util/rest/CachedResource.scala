package com.scalator.util.rest

import spray.routing.Route
import scala.concurrent.duration.Duration
import akka.actor.ActorRefFactory

trait CachedResource {
  import spray.routing.directives.CachingDirectives._
  import spray.routing.directives.FileAndResourceDirectives._
  import spray.routing.directives.EncodingDirectives._

  def cacheDuration: Duration

  def cachedResource(path: String, webResourcePrefix: String = "web")(implicit actorRefFactory: ActorRefFactory): Route = {
    val staticPagesCache = routeCache(maxCapacity = 1000, timeToIdle = cacheDuration)

    cache(staticPagesCache) {
      compressResponse() {
        getFromResource(webResourcePrefix + java.io.File.separator + path)
      }
    }
  }
}
