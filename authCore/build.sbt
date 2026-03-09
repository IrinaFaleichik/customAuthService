import scala.collection.Seq

ThisBuild / organization := "com.irka"
idePackagePrefix := Some("com.irka.authCore")
name := "authCore"
publishTo := Some(Resolver.mavenLocal)

//libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
