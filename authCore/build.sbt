import scala.collection.Seq

ThisBuild / organization := "com.irka"
idePackagePrefix := Some("com.irka.authCore")
name := "authCore"
publishTo := Some(Resolver.mavenLocal)

// ZIO
libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.21",
  "dev.zio" %% "zio-streams" % "2.1.21",
)

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % "2.1.23" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.23" % Test,
  "dev.zio" %% "zio-http-testkit" % "3.3.3" % Test
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

//libraryDependencies += "dev.zio" %% "zio-jdbc" % "0.1.2" // Replace with the latest version
