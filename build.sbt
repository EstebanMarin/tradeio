ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.0.1"
ThisBuild / organization     := "dev.tradex"
ThisBuild / organizationName := "tradex"

ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalafixDependencies += Dependencies.organizeImports
ThisBuild / fork in run := true

resolvers += Resolver.sonatypeRepo("snapshots")
val scalafixCommonSettings = inConfig(IntegrationTest)(scalafixConfigSettings(IntegrationTest))

lazy val root = (project in file("."))
  .settings(
    name := "tradeio"
  )
  .aggregate(core, tests)

lazy val core = (project in file("modules/core")).settings(
  name := "tradeio-core",
  commonSettings,
  consoleSettings,
  dependencies
)

lazy val tests = (project in file("modules/tests"))
  .configs(IntegrationTest)
  .settings(
    name := "tradeio-test-suite",
    commonSettings,
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    Defaults.itSettings,
    scalafixCommonSettings,
    testDependencies
  )
  .dependsOn(core)

lazy val commonSettings = Seq(
  scalafmtOnCompile := true,
  scalacOptions ++= List("-Ymacro-annotations", "-Yrangepos", "-Wconf:cat=unused:info"),
  resolvers += Resolver.sonatypeRepo("snapshots")
)

lazy val consoleSettings = Seq(
  Compile / console / scalacOptions --= Seq("-Ywarn-unused", "-Ywarn-unused-import")
)

lazy val compilerOptions = {
  val commonOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-encoding",
    "utf8",
    "-target:jvm-1.8",
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps",
    "-Ywarn-value-discard",
    "-Ymacro-annotations",
    "-Ywarn-unused:imports"
  )

  scalacOptions ++= commonOptions
}

lazy val dependencies =
  libraryDependencies ++= Dependencies.tradeioDependencies

lazy val testDependencies =
  libraryDependencies ++= Dependencies.testDependencies

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")
