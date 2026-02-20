package com.irka.authCore
package identity

final case class UsernameIdentity(username: String, password: String) extends Identity:
  def validate: Either[String, UsernameIdentity] =
    for
      validUsername <- validateUsername(username)
      validPassword <- validatePassword(password)
    yield UsernameIdentity(username = validUsername, password = validPassword)
