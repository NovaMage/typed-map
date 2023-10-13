import sbtrelease.ReleaseStateTransformations.*

organization := "com.github.novamage"

name := "Typed Map"

description := "Utility package providing a Map with typed keys for type-safe sotrage and retrievals without casts"

scalaVersion := "2.13.12"

licenses := Seq("MIT" -> url("https://github.com/NovaMage/typed-map/blob/main/LICENSE.txt"))

homepage := Some(url("https://github.com/NovaMage/typed-map"))

javacOptions := Seq("-source", "1.8", "-target", "1.8")

scalacOptions ++= Seq(
  "-Xsource:3",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-explaintypes",
  "-Xlint",
  "-Wunused",
  "-Wdead-code",
  "-Werror",
)

scalafixOnCompile := true

inThisBuild(
  List(
    semanticdbEnabled          := true,                        // enable SemanticDB
    semanticdbVersion          := scalafixSemanticdb.revision, // only required for Scala 2.x
    scalafixScalaBinaryVersion := "2.13"
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

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

Test / test := {
  (Test / test).dependsOn(Test / scalafmt).value
}

Test / publishArtifact := false

exportJars := true

Test / parallelExecution := false

libraryDependencies += "org.scalatest"     %% "scalatest"         % "3.2.16"   % Test
libraryDependencies += "org.scalatest"     %% "scalatest-funspec" % "3.2.16"   % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-4-11"      % "3.2.16.0" % "test"

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := PgpKeys.publishSigned.value

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
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
