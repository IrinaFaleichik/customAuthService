package com.irka.authCore
package identity

import model.AuthUser
import password.HashingUtils
import zio.ZIO

trait Identity(using Seal):
  val password: String

  def validate: Either[String, Identity]

object Identity:

  import UsernameIdentity.*
  import EmailIdentity.*

  def fromCredential(emailOrUsername: String, password: String): Either[String, Identity] =
    EmailIdentity(emailOrUsername, password).validate
      .orElse(UsernameIdentity(emailOrUsername, password).validate)

