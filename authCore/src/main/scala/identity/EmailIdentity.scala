package com.irka.authCore
package identity

final case class EmailIdentity(email: String, password: String) extends Identity:
  def validate: Either[String, EmailIdentity] =
    for
      validEmail <- validateEmail(email)
      validPassword <- validatePassword(password)
    yield EmailIdentity(email = email, password = validPassword)
