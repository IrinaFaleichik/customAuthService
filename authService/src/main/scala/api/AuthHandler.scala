
package com.irka.authService
package api

import service.AuthService

import com.irka.authCore.identity.Identity
import com.irka.authCore.model.AuthUserDto
import com.irka.authCore.password.HashingUtils

object AuthHandler:

  import zio.*
  import zio.http.*

  /** Creates a basic auth handler aspect that extracts credentials from HTTP headers
   * and processes them using the provided authentication function */
  // todo redo to JWT? write token in a db (mb redis? for cache), and return it in the response
  private def basicAuthHandler(
                                authenticate: Identity => ZIO[AuthService & HashingUtils, Throwable, AuthUserDto]
                              ): HandlerAspect[AuthService & HashingUtils, AuthUserDto] =
    HandlerAspect.interceptIncomingHandler(Handler.fromFunctionZIO[Request]: request =>
      request.header(Header.Authorization) match
        case Some(Header.Authorization.Basic(emailOrUsername, password)) =>
          for
            _ <- ZIO.logInfo(s"Authenticating user: $emailOrUsername")
            authResult <- ZIO.fromEither(
                Identity.fromCredential(emailOrUsername, password.stringValue)
              )
              .flatMap(identity => authenticate(identity))
              .mapError(errorMsg => Response
                .unauthorized(s"Invalid credentials: $errorMsg")
                .addHeaders(Headers(Header.WWWAuthenticate.Basic(realm = Some("Protected API"))))
              )
          yield (request, authResult)

        case _ =>
          ZIO.fail(
            Response
              .unauthorized("Authentication required")
              .addHeaders(Headers(Header.WWWAuthenticate.Basic(realm = Some("Protected API")))),
          )
    )

  /** Basic authentication handler for normal user access */
  val basicAuthWithUserContext: HandlerAspect[AuthService & HashingUtils, AuthUserDto] =
    basicAuthHandler(AuthService.authenticate)

  /** Basic authentication handler requiring admin privileges */
  val adminAuth: HandlerAspect[AuthService & HashingUtils, AuthUserDto] =
    basicAuthHandler(AuthService.authenticateAdmin)