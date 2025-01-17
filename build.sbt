import uk.gov.hmrc.DefaultBuildSettings

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "2.13.12"

val strictBuilding: SettingKey[Boolean] = StrictBuilding.strictBuilding
StrictBuilding.strictBuildingSetting

lazy val microservice = Project("ngr-stub", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += "-Wconf:src=routes/.*:s",
    scalacOptions ++= {
      if (StrictBuilding.strictBuilding.value) ScalaCompilerFlags.strictScalaCompilerOptions else Nil
    },
    PlayKeys.playDefaultPort := 1501,
  )
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(WartRemoverSettings.wartRemoverSettings:_*)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(SbtUpdatesSettings.sbtUpdatesSettings *)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(DefaultBuildSettings.itSettings())
  .settings(libraryDependencies ++= AppDependencies.it)

