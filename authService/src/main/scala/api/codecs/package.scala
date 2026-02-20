package com.irka.authService
package api

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import com.irka.authCore.model.{Role, UserId}
import com.irka.authCore.password.HashedPassword

package object codecs:
  import identity.*

  // JSON codecs for PasswordHash
  implicit val hashedPasswordEncoder: JsonEncoder[HashedPassword] = JsonEncoder.string.contramap(_.toString) // hides password

  implicit val hashedPasswordDecoder: JsonDecoder[HashedPassword] = JsonDecoder.string.map(HashedPassword.create)

  implicit val roleEncoder: JsonEncoder[Role] = JsonEncoder[String].contramap:
    case Role.Admin => "Admin"
    case Role.User => "User"

  implicit val roleDecoder: JsonDecoder[Role] = JsonDecoder[String].map:
    case "Admin" => Role.Admin
    case "User" => Role.User

  // Response DTO with auth info
  case class AuthUserDto(id: UserId, username: String, email: Option[String], role: Role)

  object AuthUserDto:
    implicit val encoder: JsonEncoder[AuthUserDto] = DeriveJsonEncoder.gen[AuthUserDto]
    implicit val decoder: JsonDecoder[AuthUserDto] = DeriveJsonDecoder.gen[AuthUserDto]
