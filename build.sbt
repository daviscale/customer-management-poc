val thisProjectVersion = "0.1.0-SNAPSHOT"
val coreScalaVersion = "2.12.13"
val scalaTestVersion = "3.2.7"

ThisBuild / organization := "org.daviscale"
ThisBuild / version      := thisProjectVersion
ThisBuild / scalaVersion := coreScalaVersion

lazy val aggregate = (project in file("."))
  .settings(
    name := "aggregate"
  )
  .aggregate(core, recordSorterCmd, restApiApp)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
      "org.scalatest" %% "scalatest-flatspec" % scalaTestVersion % "test"
    )
  )

// Command line application that sorts records
lazy val recordSorterCmd = (project in file("record-sorter-cmd"))
  .settings(
    name := "record-sorter-cmd"
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(core)

lazy val restApiApp = (project in file("rest-api-app"))
  .settings(
    name := "rest-api-app"
  )
  .dependsOn(core)
