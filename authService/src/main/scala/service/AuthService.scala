package com.irka.authService
package service

import domain.model.UserId

import com.irka.authCore.identity.{EmailIdentity, Identity, UsernameIdentity}
import com.irka.authCore.model.{AuthUserDto, Role}
import persistence.AuthRepository

import domain.errors.{AccessDeniedException, NotImplementedException}
import zio.{ZIO, ZLayer}

// Strategy pattern for authenticating users
trait AuthService:
  def authenticate(identity: Identity): ZIO[HashingUtilsService, Throwable, AuthUserDto]

  def create(identity: Identity): ZIO[Any, Throwable, AuthUserDto]

  def changeRole(userId: UserId, role: Role): ZIO[Any, Throwable, AuthUserDto]

object AuthService:
  def authenticate(identity: Identity): ZIO[AuthService & HashingUtilsService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.authenticate(identity))

  def authenticateAdmin(identity: Identity): ZIO[AuthService & HashingUtilsService, Throwable, AuthUserDto] =
    authenticate(identity)
      .flatMap:
        case auth@AuthUserDto(_, _, _, role) if role == Role.Admin => ZIO.succeed(auth)
        case _ => ZIO.fail(new AccessDeniedException)

  def changeRole(userId: UserId, role: Role): ZIO[AuthService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.changeRole(userId, role))

  def create(identity: Identity): ZIO[AuthService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthService](_.create(identity))

  private final class DatabaseAuthService(
                                           usernameRepo: AuthRepository[UsernameIdentity],
                                           emailRepo: AuthRepository[EmailIdentity]
                                         ) extends AuthService:
    override def authenticate(identity: Identity): ZIO[HashingUtilsService, Throwable, AuthUserDto] =
      identity match
        case i: EmailIdentity => ZIO.fail(new NotImplementedException("Email authentication"))
        case i: UsernameIdentity => usernameRepo.authenticate(i)

    override def create(identity: Identity): ZIO[Any, Throwable, AuthUserDto] =
      identity match
        case i: EmailIdentity => ZIO.fail(new NotImplementedException("Email creation"))
        case i: UsernameIdentity => ZIO.fail(new NotImplementedException("Username creation")) //usernameRepo.create(i)

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
