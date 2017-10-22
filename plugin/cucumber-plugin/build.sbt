import sbt.{Credentials, ScmInfo}

lazy val buildSettings = Seq(
  name := "cucumber-plugin",
  organization in Global := "com.waioeka.sbt",
  scalaVersion in Global := "2.12.3",
  sbtPlugin := true,
  sbtVersion in Global := "1.0.2",
  version := "0.1.7"
)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  homepage := Some(url("https://github.com/lewismj/kea")),
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  scmInfo := Some(ScmInfo(url("https://github.com/lewismj/kea"), "scm:git:git@github.com:lewismj/kea.git")),
  autoAPIMappings := true,
  pomExtra := (
    <developers>
      <developer>
        <name>Michael Lewis</name>
        <url>@lewismj</url>
      </developer>
    </developers>
    )
) ++ credentialSettings

lazy val commonSettings = Seq(
  scalacOptions += "-target:jvm-1.8",
  libraryDependencies ++=  Seq (
    "io.cucumber" % "cucumber-core" % "2.0.1",
    "io.cucumber" %% "cucumber-scala" % "2.0.1",
    "io.cucumber" % "cucumber-jvm" % "2.0.1" artifacts Artifact("cucumber-jvm", `type`="pom", extension="pom"),
    "io.cucumber" % "cucumber-junit" % "2.0.1",
    "org.apache.commons" % "commons-lang3" % "3.5"),
    fork in test := true
)

lazy val pluginSettings = buildSettings ++ commonSettings

lazy val plugin = project.in(file("."))
  .settings(moduleName := "cucumber-plugin")
  .settings(pluginSettings:_*)
  .settings(publishSettings:_*)
