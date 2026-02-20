package com.irka.authService
package api

import domain.model.UserId

import com.irka.authCore.model.Role
import com.irka.authCore.password.HashingUtils
import service.AuthService
import zio.*
import zio.http.*
import api.codecs.*
import api.codecs.identity.*
import com.irka.authService.logging.LoggingExtensions.*

object AdminRoutes {
//  val routes: Routes[AuthService & HashingUtils, Response] = Routes(changeUserRole) @@ Middleware.debug //, deleteUser) @@ Middleware.debug

//  private lazy val changeUserRole: Route[AuthService & HashingUtils, Response] =
//    Method.POST / "role" / "change" -> handler: (request: Request) =>
//      for
//        _ <- ZIO.logInfo("Entering route /role/change")
//        authResult <- parseRequestBody[UserId](request)
//          .zip(parseRequestBody[Role](request))
//          .flatMap:
//            case (userId: UserId, role: Role) =>
//              AuthService.changeRole(userId, role)
//                .map(_ => Response.text(s"Role successfully changed for user $userId"))
//          .logErrorWithoutTrace(_.getMessage)
//        authUser <- ZIO.service[AuthUserDto]
//      yield Response.text(s"User ${authUser.username} made a privileged action: change user role")
//    .mapError(dbErrors + jsonParsingError + anyError) @@ Auth.adminAuth

  lazy val deleteUser: Route[AuthService, Response] = ???

}
