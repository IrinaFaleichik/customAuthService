package com.irka.authService
package persistence

import domain.model.UserId

import com.irka.authCore.identity.Identity
import com.irka.authCore.model.AuthUserDto
import zio.ZIO
import com.irka.authCore.model

import service.HashingUtilsService

// Interface for auth operations
trait AuthRepository[I <: Identity]:
  def authenticate(identity: I): ZIO[HashingUtilsService, Throwable, AuthUserDto]

  def findById(id: UserId): ZIO[Any, Throwable, Option[AuthUserDto]]

  def create(identity: I): ZIO[HashingUtilsService, Throwable, List[(Long, AuthUserDto)]]

// Companion with accessors
object AuthRepository:
  def authenticate(identity: Identity): ZIO[AuthRepository[Identity] & HashingUtilsService, Throwable, AuthUserDto] =
    ZIO.serviceWithZIO[AuthRepository[Identity]](_.authenticate(identity))

  def findById(id: UserId): ZIO[AuthRepository[_], Throwable, Option[AuthUserDto]] =
    ZIO.serviceWithZIO[AuthRepository[_]](_.findById(id))

  def create(identity: Identity):
  ZIO[AuthRepository[Identity] & HashingUtilsService, Throwable, List[(Long, AuthUserDto)]] =
    ZIO.serviceWithZIO[AuthRepository[Identity]](_.create(identity))

