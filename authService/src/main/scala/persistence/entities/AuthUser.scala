package com.irka.authService
package persistence.entities

import service.HashingUtilsService

import com.irka.authCore.identity.{EmailIdentity, Identity, UsernameIdentity}
import com.irka.authCore.model.{Role, UserId}
import com.irka.authCore.password.{HashedPassword, HashingUtils}
import zio.ZIO

case class AuthUser(
                     id: UserId,
                     username: String,
                     password: HashedPassword,
                     email: Option[String] = None,
                     role: Role = Role.User
                   ):

    def toTableEntity: TableEntity.AuthUser = //todo tableEntity
      TableEntity.AuthUser(
        id = this.id,
        username = this.username,
        passwordHash = this.password.hashUnsafe, // Note: need to expose this for db storage
        role = this.role.ordinal,
        email = this.email
      )

object AuthUser:
  def generateId: UserId = java.util.UUID.randomUUID().toString

  def randomUsername(email: String): String =
    val prefix = email.split("@")(0)
    val randomSuffix = java.util.UUID.randomUUID().toString.take(8)
    s"$prefix-$randomSuffix"

  def createFromIdentity(identity: Identity): ZIO[HashingUtils, Throwable, AuthUser] =
    for
      hashedPassword <- HashingUtilsService.fromPlainText(identity.password)//ZIO
      user = identity match
        case email: EmailIdentity =>
          AuthUser(
            id = generateId,
            username = randomUsername(email.email),
            password = hashedPassword,
            email = Some(email.email)
          )
        case username: UsernameIdentity =>
          AuthUser(
            id = generateId,
            username = username.username,
            password = hashedPassword,
            email = None
          )
    yield user


