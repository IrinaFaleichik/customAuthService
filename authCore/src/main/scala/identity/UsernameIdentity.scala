package com.irka.authCore
package identity

import errors.{AuthError, IdentityParseError, InvalidCredentials}

final case class UsernameIdentity(username: String, password: String) extends Identity:
  def validate: Either[IdentityParseError, UsernameIdentity] =
    for
      validUsername <- validateUsername(username)
      validPassword <- validatePassword(password)
    yield UsernameIdentity(username = validUsername, password = validPassword)
