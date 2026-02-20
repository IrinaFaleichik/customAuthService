package com.irka.authCore

import identity.{EmailIdentity, Identity, UsernameIdentity}
import password.{HashedPassword, HashingUtils}

//import infrastructure.db.entities.TableEntity
//import zio.ZIO

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
