package com.irka.authCore
package identity

trait Identity(using Seal):
  val password: String

  def validate: Either[String, Identity]

object Identity:

  def fromCredential(emailOrUsername: String, password: String): Either[String, Identity] =
    EmailIdentity(emailOrUsername, password).validate
      .orElse(UsernameIdentity(emailOrUsername, password).validate)

