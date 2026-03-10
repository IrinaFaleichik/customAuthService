package com.irka.authService

import api.AppRoutes
import logging.Logger
import persistence.{AuthRepositoryByEmail, AuthRepositoryByUsername, entities}
import service.{AuthService, HashingUtilsService, SecretService}
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.*
import zio.http.*

import javax.sql.DataSource

object AuthServer extends ZIOAppDefault {

  lazy val ctx: ZLayer[DataSource, Nothing, Quill.Sqlite[SnakeCase.type]] = Quill.Sqlite.fromNamingStrategy(entities.DBContext.namingStrategy) // context to write queries
  lazy val con: ZLayer[Any, Throwable, DataSource] = Quill.DataSource.fromPrefix("myDatabaseConfig")

  //todo move to zio-config?
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = Logger.live
  val appLayerAuth: ZLayer[Any, Throwable, AuthService] =
    con >>> ctx >>>
      AuthRepositoryByUsername.live ++
        AuthRepositoryByEmail.live >>>
      AuthService.live
      
  val appLayerSecret: ZLayer[Any, Throwable, HashingUtilsService] = SecretService.live >>> HashingUtilsService.live
  val appLayer: ZLayer[Any, Throwable, AuthService & HashingUtilsService] =
    appLayerAuth ++ appLayerSecret

  // Serving the routes using the default server layer on port 8080
  def run: ZIO[Any, Throwable, Unit] = for {
    _ <- ZIO.logInfo("Logging started")
    _ <- Server.serve(AppRoutes.routes).provide(Server.defaultWithPort(8083), appLayer)
  } yield ()
}
