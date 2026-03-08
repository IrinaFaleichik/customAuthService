package com.irka.authCore

package object model:

  enum Role:
    case User, Admin

  case class AuthUserDto(
                          id: UserId, 
                          username: String,
                          email: Option[String],
                          role: Role
                        )

  final type UserId = String
