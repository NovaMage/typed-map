import sbtrelease.ReleaseStateTransformations.*

import java.time.Instant

organization := "com.github.novamage"

name := "Typed Map"

description := "Utility package providing a Map with typed keys for type-safe storage and retrievals without casts"

scalaVersion := "3.7.4"

licenses := Seq("MIT" -> url("https://github.com/NovaMage/typed-map/blob/main/LICENSE.txt"))

homepage := Some(url("https://github.com/NovaMage/typed-map"))

scalacOptions ++= Seq(
  "--explain-types",
  "--deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Wunused:all",
  "-Werror",
)

scalafixOnCompile := true

inThisBuild(
  List(
    semanticdbEnabled := true, // enable SemanticDB
  )
)

scmInfo := Some(
  ScmInfo(
    browseUrl = url("https://github.com/NovaMage/typed-map"),
    connection = "scm:git@github.com:NovaMage/typed-map.git"
  )
)

developers := List(
  Developer(
    id = "NovaMage",
    name = "Ángel Felipe Blanco Guzmán",
    email = "angel.softworks@gmail.com",
    url = url("https://github.com/NovaMage")
  )
)

releaseUseGlobalVersion := false

ThisBuild / versionScheme := Some("semver-spec")

publishMavenStyle := true

publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

publishConfiguration := publishConfiguration.value.withOverwrite(false)

ThisBuild / publishTo := {
  if (isSnapshot.value) {
    val timestamp = Instant.now().toEpochMilli
    Some("Artifactory Realm" at s"https://magaran.jfrog.io/artifactory/magaran-sbt-dev;build.timestamp=$timestamp")
  } else Some("Artifactory Realm" at "https://magaran.jfrog.io/artifactory/magaran-sbt-release")
}

Test / test := {
  (Test / test).dependsOn(Test / scalafmt).value
}

Test / publishArtifact := false

exportJars := true

Test / parallelExecution := false

libraryDependencies += "org.scalatest"     %% "scalatest"         % "3.2.19"   % Test
libraryDependencies += "org.scalatest"     %% "scalatest-funspec" % "3.2.19"   % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-4-11"      % "3.2.18.0" % "test"

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := publish.value

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
