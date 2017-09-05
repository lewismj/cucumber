name := "cucumber-runner"
organization  := "com.waioeka.sbt"
scalaVersion := "2.12.3"
version := "0.1.3"

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)


libraryDependencies ++= Seq (
   	"io.cucumber" % "cucumber-core" % "2.0.0",
  	"io.cucumber" %% "cucumber-scala" % "2.0.0",
       	"io.cucumber" % "cucumber-jvm" % "2.0.0",
       	"io.cucumber" % "cucumber-junit" % "2.0.0",
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
