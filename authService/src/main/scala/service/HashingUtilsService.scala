package com.irka.authService
package service

import com.irka.authCore.password.{HashedPassword, HashingUtils, Secret}
import zio.{ZIO, ZLayer}

trait HashingUtilsService(constructWithSecret: Secret):
  val hashingUtils = new HashingUtils(constructWithSecret)

object HashingUtilsService:
  private final class HashingUtilsLive(secret: Secret) extends HashingUtilsService(secret)

  private def make(secret: Secret) = HashingUtilsLive(secret)

  def live: ZLayer[Secret, Throwable, HashingUtilsService] =
    ZLayer.fromFunction(HashingUtilsService.make(_))

  def fromPlainText(plainPassword: String): ZIO[HashingUtilsService, Throwable, HashedPassword] =
    for
      hs <- ZIO.service[HashingUtilsService]
      _ <- ZIO.logInfo("Hashing password")
      hashedProtectedPassword = hs.hashingUtils.fromPlainText(plainPassword)
    yield hashedProtectedPassword
