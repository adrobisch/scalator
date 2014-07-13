import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.SbtNativePackager.packageArchetype
import com.typesafe.sbt.SbtNativePackager.Debian
import sbt.Keys._
import sbt.ScalaVersion
import scala.Some
import spray.revolver.RevolverPlugin.Revolver
import spray.revolver.RevolverPlugin.Revolver._
import net.virtualvoid.sbt.graph.Plugin
import com.typesafe.sbt.packager.debian.Keys.packageDescription
import com.typesafe.sbt.packager.debian.Keys.maintainer
import com.typesafe.sbt.packager.debian.Keys.serverLoading
import com.typesafe.sbt.packager.archetypes.ServerLoader.Upstart
import scoverage.ScoverageSbtPlugin

Plugin.graphSettings

SbtNativePackager.packagerSettings

packageArchetype.java_application

Revolver.settings.settings

name := """scalator"""

packageDescription in Debian := "scalator package"

maintainer in Debian := "Andreas D."

serverLoading := Upstart

version := "0.1"

organization  := "com.scalator"

scalaVersion  := "2.11.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val dbDependencies = Seq(
  "org.flywaydb" % "flyway-core" % "3.0",
  "com.h2database" % "h2" % "1.3.175",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "com.zaxxer" % "HikariCP" % "1.3.8"
)

val scalaTesting = Seq(
  "org.specs2"          %%  "specs2"        % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "com.typesafe.akka"   %%  "akka-testkit"  % "2.3.2" % "test",
  "io.spray"            %%   "spray-testkit" % "1.3.1" % "test"
)

val javaTesting = Seq(
  "org.mockito" % "mockito-all" % "1.9.5",
  "org.seleniumhq.selenium" % "selenium-java" % "2.40.0" % "test",
  "org.subethamail" % "subethasmtp" % "3.1.7" % "test"
)

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-email" % "1.3.1",
  "commons-codec" % "commons-codec" % "1.9",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.scaldi" %% "scaldi" % "0.3.2",
  "com.typesafe.slick" %% "slick" % "2.1.0-M2",
  "org.json4s" %% "json4s-native" % "3.2.9",
  "io.spray"            %%   "spray-can"     % "1.3.1",
  "io.spray"            %%   "spray-httpx"     % "1.3.1",
  "io.spray"            %%   "spray-routing" % "1.3.1",
  "io.spray"            %%   "spray-caching" % "1.3.1",
  "com.typesafe.akka"   %%  "akka-actor"    % "2.3.2"
) ++ dbDependencies ++ scalaTesting ++ javaTesting

mainClass in Compile := Some("com.scalator.DefaultApp")

selectMainClass in Compile := Some("com.scalator.DefaultApp")

mainClass in Revolver.reStart := Some("com.scalator.DevBoot")

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + artifact.extension
}

val gruntBuild = taskKey[Unit]("grunt build task")

val gruntWatch = taskKey[Unit]("grunt watch task")

gruntBuild := Grunt.runBuild

gruntWatch := Grunt.runWatch

(compile in Compile) <<= (compile in Compile) dependsOn gruntBuild

reStart <<= reStart dependsOn gruntWatch

parallelExecution in Test := false

ScoverageSbtPlugin.instrumentSettings

ScoverageKeys.highlighting := true

addCommandAlias("dist", ";clean;universal:package-bin")

addCommandAlias("watch", "~reStart")
