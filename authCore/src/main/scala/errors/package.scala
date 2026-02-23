package com.irka.authCore

package object errors:

  class AuthError(err: String) extends Exception(s"Authentication error: $err")

  class InvalidCredentials extends AuthError("Invalid credentials")

  enum ParseError(err: String) extends Exception(err):
    case TooLong extends ParseError(s"too long")
    case TooShort extends ParseError(s"too short")
    case InvalidCharacter extends ParseError(s"invalid character")
    case BadFormat extends ParseError(s"bad format")

  enum IdentityParseError(err: String) extends Exception(s"Identity error: $err"):
    case InvalidPassword(err: ParseError) extends IdentityParseError(s"password is invalid, reason: $err")
    case InvalidEmail(err: ParseError) extends IdentityParseError(s"email is invalid, reason: $err")
    case InvalidUsername(err: ParseError) extends IdentityParseError(s"username is invalid, reason: $err")

  class SecretNotConfigured extends AuthError("Secret is not configured")
