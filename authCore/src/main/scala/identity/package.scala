package com.irka.authCore

import errors.{IdentityParseError, ParseError}

package object identity:

  // making Identity a package private sealed trait
  private[identity] sealed trait Seal

  private[identity] given Seal with {}

  // parsing constants todo extract from config
  val MinUsernameLength: Int = 3
  val MinPasswordLength: Int = 6
  val MaxUsernameLength: Int = 40
  val MaxPasswordLength: Int = 40
  val MaxEmailLength: Int = 100
  val MinEmailLength: Int = 3

  private val EmailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".r
  private val UsernameRegex = "^[a-zA-Z0-9._-]+".r

  // Validation utilities - accessible throughout the package
  private[identity] def validateUsername(username: String): Either[IdentityParseError, String] =
    if (username.length < MinUsernameLength)
      Left(IdentityParseError.InvalidUsername(ParseError.TooShort))
    else if (username.length > MaxUsernameLength)
      Left(IdentityParseError.InvalidUsername(ParseError.TooLong))
    else if (!UsernameRegex.matches(username))
      Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter))
    else
      Right(username)

  private[identity] def validateEmail(email: String): Either[IdentityParseError, String] =
    if (email.length < MinEmailLength)
      Left(IdentityParseError.InvalidEmail(ParseError.TooShort))
    else if (email.length > MaxEmailLength)
      Left(IdentityParseError.InvalidEmail(ParseError.TooLong))
    else if (!EmailRegex.matches(email))
      Left(IdentityParseError.InvalidEmail(ParseError.BadFormat))
    else
      Right(email)

  private[identity] def validatePassword(password: String): Either[IdentityParseError, String] =
    if (password.length < MinPasswordLength)
      Left(IdentityParseError.InvalidPassword(ParseError.TooShort))
    else if (password.length > MaxPasswordLength)
      Left(IdentityParseError.InvalidPassword(ParseError.TooLong))
    else
      Right(password)

  // Factory methods for public use
  def fromUsername(username: String, password: String): Either[IdentityParseError, Identity] =
    UsernameIdentity(username, password).validate

  def fromEmail(email: String, password: String): Either[IdentityParseError, Identity] =
    EmailIdentity(email, password).validate
