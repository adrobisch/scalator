package com.scalator.api

import spray.routing._
import com.scalator.api.route._
import com.scalator.configuration.Configuration

abstract class Api(val routes: Routes, configuration: Configuration) extends HttpService {

  lazy val paths = new AssetRoute(configuration).route ~ new AvatarRoute(configuration).route ~ pathPrefix("api") {
    routes.authRoute.route ~
    routes.adminRoute.adminRoute
  }

  def route: Route = paths
}
