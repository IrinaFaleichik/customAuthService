lazy val root = (project in file("."))
  .settings(
    name := "auth",
    idePackagePrefix := Some("com.irka.auth")
  )

lazy val all = (project in file("."))
  .aggregate(authCore)//, authService)

lazy val authCore =
  project
    .in(file("authCore"))
    .settings(
      name := "core",
      version := "0.1",
      // other project settings
    )
//
//lazy val authService =
//  project
//    .in(file("authService"))
//    .settings(
//      name := "service",
//      version := "0.1",
//    )