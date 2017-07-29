name := "cucumber-runner"
organization  := "com.waioeka.sbt"
scalaVersion := "2.12.2"
version := "0.1.0"
sbtPlugin := true

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

// For version 0.0.9, allow Scala 2.12
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq (
   	"io.cucumber" % "cucumber-core" % "2.0.0-SNAPSHOT",
  	"io.cucumber" %% "cucumber-scala" % "2.0.0-SNAPSHOT",
       	"io.cucumber" % "cucumber-jvm" % "2.0.0-SNAPSHOT",
       	"io.cucumber" % "cucumber-junit" % "2.0.0-SNAPSHOT",
       	"org.scala-sbt" % "test-interface" % "1.0")


pomIncludeRepository := Function.const(false)

publishArtifact in Test := false
sonatypeProfileName := "com.waioeka.sbt"

publishMavenStyle := true

// License of your choice
licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
homepage := Some(url("https://github.com/lewismj/cucumber"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/lewismj/cucmber"),
    "scm:git@github.com:lewismj/cucumber.git"
  )
)
developers := List(
  Developer(id="lewismj", name="Michael Lewis", email="lewismj@waioeka.com", url=url("https://www.waioeka.com"))
)
