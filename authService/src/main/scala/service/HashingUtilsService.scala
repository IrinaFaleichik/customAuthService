package com.irka.authService
package service

import com.irka.authCore.password.{HashedPassword, HashingUtils, Secret}
import zio.{ZIO, ZLayer}

object HashingUtilsService:
  private final class HashingUtilsLive(override val secret: Secret) extends HashingUtils

  private def make(secret: Secret) = HashingUtilsLive(secret)

  def live: ZLayer[Secret, Throwable, HashingUtils] =
    ZLayer.fromFunction(HashingUtilsService.make(_))

  def fromPlainText(plainPassword: String): ZIO[HashingUtils, Throwable, HashedPassword] =
    for
      hs <- ZIO.service[HashingUtils]
      _ <- ZIO.logInfo("Hashing password")
      hashedProtectedPassword = hs.fromPlainText(plainPassword)
    yield hashedProtectedPassword
