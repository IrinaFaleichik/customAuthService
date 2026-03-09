package com.irka.authCore
package identity

import errors.IdentityParseError
import errors.ParseError

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.*

class IdentitySpec extends AnyFunSpec:

  describe("Factory methods"):
    describe("fromUsername"):
      describe("succeeds with valid inputs"):
        val result = identity.fromUsername("validUser", "password123")
        result should be(Right(UsernameIdentity(username = "validUser", password = "password123")))
      describe("fails with short username"):
        val result = identity.fromUsername("ab", "password123")
        result should be(Left(IdentityParseError.InvalidUsername(ParseError.TooShort)))
      describe("fails with invalid characters"):
        val result = identity.fromUsername("user name", "password123")
        result should be(Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter)))
      describe("fails with short password"):
        val result = identity.fromUsername("validUser", "pass")
        result should be(Left(IdentityParseError.InvalidPassword(ParseError.TooShort)))
    describe("fromEmail"):
      describe("succeeds with valid inputs"):
        val result = identity.fromEmail("user@example.com", "password123")
        assert(result.isRight)
        assert(result.toOption.get.isInstanceOf[EmailIdentity])
        result should be(Right(EmailIdentity("user@example.com", "password123")))
      describe("fails with invalid email format"):
        identity.fromEmail("not-an-email@", "password123") should be
        Left(IdentityParseError.InvalidEmail(ParseError.BadFormat))
      describe("fails with short password"):
        identity.fromEmail("user@example.com", "pass") should be
        Left(IdentityParseError.InvalidPassword(ParseError.TooShort))

  describe("Validation rules"):
    describe("username minimum length edge"):
      val minValidLength = "a" * identity.MinUsernameLength
      val tooShort = "a" * (identity.MinUsernameLength - 1)
      assert(identity.fromUsername(minValidLength, "password123").isRight)
      assert(identity.fromUsername(tooShort, "password123").isLeft)
    describe("username allowed characters"):
      val validChars = List("user123", "user_name", "uSeR-nAmE", "user.name8")
      val invalidChars = List("user name", "user@name", "user#name", "user+name", "user‿name")
      assert(
        validChars.forall(u => identity.fromUsername(u, "password123").isRight) &&
          invalidChars.forall(u => identity.fromUsername(u, "password123").isLeft)
      )
    describe("password minimum length"):
      val minValidLength = "a" * identity.MinPasswordLength
      val tooShort = "a" * (identity.MinPasswordLength - 1)
      assert(identity.fromUsername("validUser", minValidLength).isRight)
      assert(identity.fromUsername("validUser", tooShort).isLeft)
    describe("email format validation"):
      val validEmails = List("user@example.com", "user.name@example.co.uk", "user+tag@example.com")
      val invalidEmails = List("not-an-email", "user@", "@example.com", "user@.com", "user@example.  ", "user@example...")
      assert(
        validEmails.forall(e => identity.fromEmail(e, "password123").isRight) &&
          invalidEmails.forall(e => identity.fromEmail(e, "password123").isLeft)
      )