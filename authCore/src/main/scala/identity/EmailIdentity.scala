package com.irka.authCore
package identity

import errors.IdentityParseError

final case class EmailIdentity(email: String, password: String) extends Identity:
  def validate: Either[IdentityParseError, EmailIdentity] =
    for
      validEmail <- validateEmail(email)
      validPassword <- validatePassword(password)
    yield EmailIdentity(email = email, password = validPassword)
