package com.irka.authService
package service

import domain.model.UserId

import com.irka.authCore.identity.{EmailIdentity, Identity, UsernameIdentity}
import com.irka.authCore.model.{AuthUserDto, Role}
import com.irka.authCore.password.HashingUtils
import persistence.AuthRepository

import zio.{ZIO, ZLayer}

// Strategy pattern for authenticating users
trait AuthService:
  def authenticate(identity: Identity): ZIO[HashingUtils, Throwable, AuthUserDto]

  def create(identity: Identity): ZIO[Any, Throwable, AuthUserDto]

  def changeRole(userId: UserId, role: Role): ZIO[Any, Throwable, AuthUserDto]

object AuthService:
  def authenticate(identity: Identity): ZIO[AuthService & HashingUtils, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.authenticate(identity))

  def authenticateAdmin(identity: Identity): ZIO[AuthService & HashingUtils, Throwable, AuthUserDto] =
    authenticate(identity)
      .flatMap:
        case auth@AuthUserDto(_, _, _, role) if role == Role.Admin => ZIO.succeed(auth)
        case _ => ZIO.fail(new Exception("Access restricted: you should be admin to access this resource"))

  def changeRole(userId: UserId, role: Role): ZIO[AuthService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.changeRole(userId, role))

  def create(identity: Identity): ZIO[AuthService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.create(identity))

  private final class DatabaseAuthService(
                                           usernameRepo: AuthRepository[UsernameIdentity],
                                           emailRepo: AuthRepository[EmailIdentity]
                                         ) extends AuthService:
    override def authenticate(identity: Identity): ZIO[HashingUtils, Throwable, AuthUserDto] =
      identity match
        case i: EmailIdentity => ZIO.fail(new Exception("Email authentication is not implemented yet"))
        case i: UsernameIdentity => usernameRepo.authenticate(i)

    override def create(identity: Identity): ZIO[Any, Throwable, AuthUserDto] =
      identity match
        case i: EmailIdentity => ZIO.fail(new Exception("Email creation is not implemented yet"))
        case i: UsernameIdentity => ZIO.fail(new Exception("Username creation is not implemented yet")) //usernameRepo.create(i)

    def changeRole(userId: UserId, role: Role): ZIO[Any, Throwable, AuthUserDto] = ???

  // Live layer that combines repositories
  val live: ZLayer[
    AuthRepository[UsernameIdentity]
      & AuthRepository[EmailIdentity]
    ,
    Nothing,
    AuthService
  ] =
    ZLayer {
      for {
        usernameRepo <- ZIO.service[AuthRepository[UsernameIdentity]]
        emailRepo <- ZIO.service[AuthRepository[EmailIdentity]]
      } yield new DatabaseAuthService(usernameRepo, emailRepo)
    }
