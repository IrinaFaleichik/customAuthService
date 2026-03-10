package com.irka.authService
package api

import domain.model.UserId

import com.irka.authCore.model.Role
import service.AuthService
import service.HashingUtilsService
import zio.*
import zio.http.*
import com.irka.authService.logging.LoggingExtensions.*
import com.irka.authCore.model.AuthUserDto
import domain.codecs.*

object AdminRoutes {
  val routes: Routes[AuthService & HashingUtilsService, Response] = Routes(changeUserRole) @@ Middleware.debug //, deleteUser) @@ Middleware.debug

  private lazy val changeUserRole: Route[AuthService & HashingUtilsService, Response] =
    Method.POST / "role" / "change" -> handler: (request: Request) =>
      for
        _ <- ZIO.logInfo("Entering route /role/change")
        authResult <- parseRequestBody[UserId](request)
          .zip(parseRequestBody[Role](request))
          .flatMap:
            case (userId: UserId, role: Role) =>
              AuthService.changeRole(userId, role)
                .map(_ => Response.text(s"Role successfully changed for user $userId"))
          .logErrorWithoutTrace(_.getMessage)
        authUser <- ZIO.service[AuthUserDto]
      yield Response.text(s"User ${authUser.username} made a privileged action: change user role")
    .mapError(dbErrors + jsonParsingError + anyError) @@ AuthHandler.adminAuth

  lazy val deleteUser: Route[AuthService, Response] = ???

}
