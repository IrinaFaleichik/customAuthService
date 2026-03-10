package com.irka.authService
package api

import com.irka.authCore.model.AuthUserDto
import api.AuthHandler.basicAuthWithUserContext
import service.AuthService
import service.HashingUtilsService
import zio.*
import zio.http.*

object AppRoutes {

  val routes: Routes[AuthService & HashingUtilsService, Response] =
    Routes(greetRoute) @@ Middleware.debug ++ test
      ++ AdminRoutes.routes ++ AuthRoutes.routes @@ Middleware.debug

  private lazy val test: Routes[AuthService, Response] = Routes(
    Method.GET / "test" -> handler:
      Response.text("Welcome to my service!") // todo add sandbox middleware
  )

  private lazy val greetRoute: Route[AuthService & HashingUtilsService, Response] =
    Method.POST / "account" / "me" -> handler: (_: Request) =>
      ZIO.serviceWith[AuthUserDto](i =>
        Response.text(s"Welcome ${i.username}!"),
      )
    @@ basicAuthWithUserContext

}
