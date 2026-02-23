package com.irka.authCore
package identity

import errors.IdentityParseError

trait Identity(using Seal):
  val password: String

  def validate: Either[IdentityParseError, Identity]

object Identity:

  def fromCredential(emailOrUsername: String, password: String): Either[IdentityParseError, Identity] =
    EmailIdentity(emailOrUsername, password).validate
      .orElse(UsernameIdentity(emailOrUsername, password).validate)

