package com.irka.authCore
package token

import model.{AuthUserDto, UserId}

import errors.TokenError

import java.time.Instant
import java.util.{Base64, UUID}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/** Represents an issued authentication token */
case class AuthToken(
                      value: String,
                      userId: UserId,
                      issuedAt: Instant,
                      expiresAt: Instant
                    ):
  def isExpired: Boolean = Instant.now().isAfter(expiresAt)

object AuthToken:

  private val Algorithm = "HmacSHA256"

  /** Creates a signed token from an AuthUserDto using a secret key.
   *  Format: base64(userId.issuedAt.expiresAt).signature */
  def issue(user: AuthUserDto, secret: String, ttlSeconds: Long = 3600): AuthToken =
    val issuedAt  = Instant.now()
    val expiresAt = issuedAt.plusSeconds(ttlSeconds)
    val payload   = buildPayload(user.id, issuedAt, expiresAt)
    val signature = sign(payload, secret)
    val value     = s"$payload.$signature"
    AuthToken(value, user.id, issuedAt, expiresAt)

  /** Returns a token only if the signature is valid and it has not expired */
  def validate(raw: String, secret: String): Either[TokenError, AuthToken] =
    raw.split("\\.").toList match
      case List(payload, signature) =>
        if sign(payload, secret) != signature then Left(TokenError.InvalidSignature)
        else
          parsePayload(payload) match
            case None        => Left(TokenError.MalformedToken)
            case Some(token) =>
              if token.isExpired then Left(TokenError.TokenExpired)
              else Right(token)
      case _ => Left(TokenError.MalformedToken)

  // --- private helpers ---

  private def buildPayload(userId: UserId, issuedAt: Instant, expiresAt: Instant): String =
    val raw = s"$userId|${issuedAt.toEpochMilli}|${expiresAt.toEpochMilli}"
    Base64.getUrlEncoder.withoutPadding.encodeToString(raw.getBytes("UTF-8"))

  private def parsePayload(payload: String): Option[AuthToken] =
    val decoded = new String(Base64.getUrlDecoder.decode(payload), "UTF-8")
    decoded.split("\\|").toList match
      case List(userId, issuedAtMs, expiresAtMs) =>
        scala.util.Try:
          val issuedAt  = Instant.ofEpochMilli(issuedAtMs.toLong)
          val expiresAt = Instant.ofEpochMilli(expiresAtMs.toLong)
          AuthToken(s"$payload.<signature>", userId, issuedAt, expiresAt)
        .toOption
      case _ => None

  private def sign(payload: String, secret: String): String =
    val mac = Mac.getInstance(Algorithm)
    mac.init(SecretKeySpec(secret.getBytes("UTF-8"), Algorithm))
    Base64.getUrlEncoder.withoutPadding.encodeToString(mac.doFinal(payload.getBytes("UTF-8")))