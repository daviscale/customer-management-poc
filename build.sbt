val thisProjectVersion = "0.1.0-SNAPSHOT"
val coreScalaVersion = "2.12.13"
val scalaTestVersion = "3.2.7"
val akkaVersion = "2.6.14"
val akkaHttpVersion = "10.2.4"

ThisBuild / organization := "org.daviscale"
ThisBuild / version      := thisProjectVersion
ThisBuild / scalaVersion := coreScalaVersion

lazy val aggregate = (project in file("."))
  .settings(
    name := "aggregate"
  )
  .aggregate(core, recordSorterCmd, customerRestManagement)

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

// A RESTful API app that can update and view customer data
lazy val customerRestManagement = (project in file("customer-rest-management"))
  .settings(
    name := "customer-rest-management",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    )
  )
  .dependsOn(core)
