package com.scalator.api.route

import com.scalator.api.ApiJsonSupport
import com.scalator.repository.UserRepository
import spray.http.StatusCodes
import com.scalator.authentication.Authentication
import spray.routing.directives.MarshallingDirectives._
import com.scalator.model.{PersonalInformationUpdate, PasswordReset, PasswordChange, User}
import com.scalator.service.PasswordService

class AdminRoute(val passwordService: PasswordService, val userRepository: UserRepository, val auth: Authentication) extends ApiJsonSupport {

  import spray.routing.directives.PathDirectives._
  import spray.routing.directives.MethodDirectives.post
  import spray.routing.directives.RouteDirectives.complete
  import spray.routing.RouteConcatenation._

  def adminRoute = pathPrefix("admin") {
    path("user" / "password") {
      auth.requireUser {
        user: User =>
          post {
            entity(as[PasswordChange]) {
              change =>
                complete {
                  if (passwordService.changePossible(user, change)) {
                    passwordService.changePassword(user, change)
                    StatusCodes.NoContent
                  } else StatusCodes.Conflict
                }
            }
          }
      }
    } ~ path("reset_password") {
      post {
        entity(as[PasswordReset]) { reset =>
          userRepository.findUserByMail(reset.email) match {
            case None => complete(StatusCodes.Conflict)
            case Some(user) => {
              passwordService.resetPassword(user)
              complete(StatusCodes.NoContent)
            }
          }
        }
      }
    } ~ path("personal_information") {
      post {
        auth.requireUser { user =>
          entity(as[PersonalInformationUpdate]) {
            update =>
              userRepository.updatePersonalInformation(user.login, update.email.getOrElse(null), update.about.getOrElse(null))
              complete(StatusCodes.OK)
          }
        }
      }
    }
  }
}
