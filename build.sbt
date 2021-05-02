ThisBuild / organization := "org.daviscale"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.13"

lazy val aggregate = (project in file("."))
  .settings(
    name := "aggregate"
  )
  .aggregate(core, commandLineApp, restApiApp)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.7" % "test"
    )
  )

lazy val commandLineApp = (project in file("command-line-app"))
  .settings(
    name := "command-line-app"
  )
  .dependsOn(core)

lazy val restApiApp = (project in file("rest-api-app"))
  .settings(
    name := "rest-api-app"
  )
  .dependsOn(core)
