package com.irka.authService
package persistence

import domain.model.{CardboardId, SquareId, UserId}

import com.irka.authCore.model.{AuthUserDto, Role}
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill

// DB entities (what maps to tables)
package object entities:
  object TableEntity:
    case class AuthUser(
                         id: UserId,
                         username: String,
                         passwordHash: String,
                         role: Int = Role.User.ordinal,
                         email: Option[String] = None
                       ):
      def toDto: AuthUserDto = AuthUserDto(id, username, email, Role.fromOrdinal(role))

  type DBContext = Quill.Sqlite[SnakeCase]

  object DBContext:
    val namingStrategy: SnakeCase.type = SnakeCase
