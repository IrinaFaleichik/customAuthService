
package com.irka.authCore
package identity

import errors.IdentityParseError
import errors.ParseError

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.*

class UsernameIdentityValidateSpec extends AnyFunSpec:

  describe("UsernameIdentity validation"):
    it("should accept valid username and password"):
      val identity = UsernameIdentity("validUser", "validPassword")
      val result = identity.validate
      result.isRight should be(true)
      result should be(Right(identity))

    it("should reject username that is too short"):
      val identity = UsernameIdentity("ab", "validPassword") // Less than MinUsernameLength (3)
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidUsername(ParseError.TooShort)))

    it("should reject username with invalid characters: space"):
      val identity = UsernameIdentity("invalid user", "validPassword") // Contains space
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter)))

    it("should reject username with invalid characters: %"):
      val identity = UsernameIdentity("invalid%user", "validPassword") // Contains %
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter)))

    it("should reject username with invalid characters: \r"):
      val identity = UsernameIdentity("invalid user", "validPassword") // Contains \r
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter)))

    it("should reject username with invalid characters: unicode character (ascii only)"):
      val identity = UsernameIdentity("user‿name", "validPassword") // Contains unicode
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidUsername(ParseError.InvalidCharacter)))

    it("should reject empty password"):
      val identity = UsernameIdentity("validUser", "")
      val result = identity.validate
      result.isLeft should be(true)
      result should be(Left(IdentityParseError.InvalidPassword(ParseError.TooShort)))

    //    it("should successfully decode valid JSON"):
    //      val json = """{"username":"validUser","password":"validPassword"}"""
    //      val result = json.fromJson[UsernameIdentity]
    //      result.isRight should be(true)
    //      result should be(Right(UsernameIdentity("validUser", "validPassword")))
    //
    //    it("should fail to decode invalid JSON with error message"):
    //      val json = """{"username":"ab","password":"validPassword"}"""
    //      val result = json.fromJson[UsernameIdentity]
    //      result.isLeft should be(true)
    //      result.left.getOrElse("") should include("Username")
    //
    //    it("should encode to JSON and decode back"):
    //      val original = UsernameIdentity("validUser", "validPassword")
    //      val json = original.toJson
    //      val decoded = json.fromJson[UsernameIdentity]
    //      decoded.isRight should be(true)
    //      decoded should be(Right(original))

    it("should validate username with special characters correctly"):
      val validIdentity = UsernameIdentity("valid_user-123", "validPassword")
      val invalidIdentity = UsernameIdentity("invalid@user", "validPassword")
      validIdentity.validate.isRight should be(true)
      invalidIdentity.validate.isLeft should be(true)