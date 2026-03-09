package com.irka.authCore
package identity

import errors.IdentityParseError
import errors.ParseError

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.*

class EmailIdentityValidateSpec extends AnyFunSpec:

  describe("EmailIdentity validation"):

    describe("Email format validation"):
      it("should accept valid email address"):
        val identity = EmailIdentity("user@example.com", "password123")
        val result = identity.validate
        result should be(Right(identity))

      it("should reject invalid email format"):
        val invalidEmails = List(
          "userexample.com",  // missing @
          "user@",            // missing domain
          "@example.com",     // missing local part
          "user@example",     // missing TLD
          "user@.com",        // missing domain name
          "user@example."     // incomplete TLD
        )
        val results = invalidEmails.map(email =>
          EmailIdentity(email, "password123").validate
        )
        results should not be empty
        results.foreach: result =>
          result should be(Left(IdentityParseError.InvalidEmail(ParseError.BadFormat)))

    describe("Password validation"):
      it("should accept valid password"):
        val identity = EmailIdentity("user@example.com", "password123")
        val result = identity.validate
        result should be(Right(identity))

      it("should reject password that is too short"):
        val identity = EmailIdentity("user@example.com", "12345") // Less than MinPasswordLength (6)
        val result = identity.validate
        result should be(Left(IdentityParseError.InvalidPassword(ParseError.TooShort)))

      it("should reject empty password"):
        val identity = EmailIdentity("user@example.com", "")
        val result = identity.validate
        result.isLeft should be(true)
        result should be(Left(IdentityParseError.InvalidPassword(ParseError.TooShort)))

    describe("Edge cases"):
      it("should accept email with plus sign in local part"):
        val identity = EmailIdentity("user+tag@example.com", "password123")
        val result = identity.validate
        result.isRight should be(true)

      it("should accept email with subdomain"):
        val identity = EmailIdentity("user@sub.example.com", "password123")
        val result = identity.validate
        result.isRight should be(true)

      it("should accept email with numbers"):
        val identity = EmailIdentity("user123@example123.com", "password123")
        val result = identity.validate
        result.isRight should be(true)