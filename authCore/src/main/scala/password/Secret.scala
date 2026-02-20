package com.irka.authCore
package password

import errors.SecretNotConfigured

import scala.util.Try

trait Secret:
  val salt: String
  val iterations: Int

object Secret:

  private final class SecretLive(override val salt: String, override val iterations: Int) extends Secret

  def make(salt: String, iterations: Int): Secret =
    SecretLive(salt, iterations)

//  def live: ZLayer[Any, Throwable, Secret] = ZLayer.fromZIO:
//    (
//      for
//        _ <- ZIO.logInfo("Creating a secret")
//        env <- ZIO.attempt(System.getenv)
//        envSalt <- ZIO.attempt(env.get("PASSWORD_SALT"))
//        envIterations <- ZIO.attempt(env.get("PASSWORD_ITERATIONS"))
//        envIterationsInt <- ZIO.attempt(envIterations.toInt)
//        secret = make(envSalt, envIterationsInt)
//        _ <- ZIO.logInfo("Secret is created")
//      yield secret
//      ).catchAll:
//      error =>
//        val shadingError = new SecretNotConfigured
//        ZIO.logError(shadingError.getMessage) *>
//          ZIO.fail(shadingError)
