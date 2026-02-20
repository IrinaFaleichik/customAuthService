ThisBuild / version := "0.1"
ThisBuild / organization := "com.irka"
ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .aggregate(authCore, authService)
  .settings(
    name := "customAuthService",
    idePackagePrefix := Some("com.irka.auth")
  )

//lazy val all = (project in file("."))
//  .aggregate(authCore, authService)

lazy val authCore = project
    .in(file("authCore"))
    .settings(
      name := "authCore",
      idePackagePrefix := Some("com.irka.authCore"),
//      version := "0.1",
      // other project settings
    )

lazy val authService = project
  .in(file("authService"))
  .settings(
    name := "authService",
    idePackagePrefix := Some("com.irka.authService")
  )
  .dependsOn(authCore)