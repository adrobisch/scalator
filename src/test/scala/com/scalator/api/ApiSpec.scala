package com.scalator.api

import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.scalator.authentication.Authentication
import com.scalator.{HttpModule, TestModule}
import com.scalator.model._
import org.specs2.mutable.Specification
import scaldi.Injectable
import akka.actor.ActorRefFactory
import com.scalator.repository.{MockedRepositoryModule, UserRepository}
import org.specs2.mock.Mockito
import com.scalator.model.User
import com.scalator.api.route.Routes
import com.scalator.configuration.Configuration
import spray.http.HttpHeaders.Location

class ApiSpec extends Specification with Specs2RouteTest with Injectable with Mockito with ApiJsonSupport {
  implicit val modules =  new TestModule :: new HttpModule :: new MockedRepositoryModule

  val api: Api = new Api(inject[Routes], inject[Configuration]) {
    override implicit def actorRefFactory: ActorRefFactory = system
  }

  val userRepository = inject[UserRepository]

  val testUser = User("test", "passwordHash", None, None)

  def withAuthentication: RequestTransformer = {
    import Authentication.{authCookieName, authToCookieValue}

    val authCookieValue = authToCookieValue(AuthenticationInfo(testUser.login, "token"))
    val cookieHeader = s"$authCookieName=$authCookieValue"

    addHeader("Cookie", cookieHeader)
  }

  "Api" should {
    "redirect to the index page for GET requests to the root path" in {
      Get("/") ~> api.route ~> check {
        status === MovedPermanently
        response.headers must contain(Location("/web/index.html"))
      }
    }
  }
}
