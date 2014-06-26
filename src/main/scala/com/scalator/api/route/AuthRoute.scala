package com.scalator.api.route

import com.scalator.api.ApiJsonSupport
import spray.http.{HttpCookie, StatusCodes}
import com.scalator.authentication.Authentication
import spray.routing.directives.MethodDirectives._
import com.scalator.model.{AuthenticationInfo, User, LoginRequest}
import scala.Some

class AuthRoute(val authentication: Authentication) extends ApiJsonSupport {
  import spray.routing.directives.PathDirectives._
  import spray.routing.directives.MethodDirectives.post
  import spray.routing.directives.MarshallingDirectives.{entity, as}
  import spray.routing.directives.RouteDirectives.complete

  def route = pathPrefix("auth") {
    import Authentication.{authCookieName, authToCookieValue}
    import spray.routing.directives.CookieDirectives.{setCookie, deleteCookie}
    import spray.routing.RouteConcatenation._

    path("login") {
      post { entity(as[LoginRequest]) { loginRequest =>
          authentication.createCredentialForPassword(loginRequest.login, loginRequest.password).map { credential =>
            val authCookie = HttpCookie(authCookieName,
              path = Some("/"),
              content = authToCookieValue(AuthenticationInfo(loginRequest.login, credential.token)),
              maxAge = Some(1000),
              httpOnly = true)

            setCookie(authCookie)(complete(StatusCodes.NoContent))

          }.getOrElse(complete(StatusCodes.Unauthorized))
        }
      }
    } ~ path("logout") {
      post {
        deleteCookie(HttpCookie(name = authCookieName, path = Some("/"), content = "", maxAge = Some(0), httpOnly = true)) {
          complete {
            StatusCodes.NoContent
          }
        }
      }
    } ~ path("user") {
      authentication.requireUser {
        user: User =>
          get {
            complete {
              user
            }
          }
      }
    }
  }
}
