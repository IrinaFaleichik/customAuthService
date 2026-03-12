package com.irka.authService
package domain

import persistence.entities.DBContext
import zio.json.*

package object errors:
  class InvalidJsonException(err: String)(expectedJson: String) extends Exception(s"Invalid JSON: $err; try a JSON like ${expectedJson.toJson}")

  class DbError(err: String) extends Exception(s"For db type: $DBContext")

  class AuthError(err: String) extends Exception(s"Authentication error: $err")

  class SecretNotConfiguredException extends AuthError("Secret is not configured")

  class NotImplementedException(name: String) extends Exception(s"$name not implemented yet")

  class InvalidCredentialsException extends AuthError("Invalid credentials")

  class AccessDeniedException extends AuthError("Access denied: you should be admin to access this resource")