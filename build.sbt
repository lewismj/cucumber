//crossScalaVersions := Seq("2.11.8","2.12.1")

lazy val publishSettings = Seq(
 pomIncludeRepository := Function.const(false),
 pomExtra := (
  <url>https://github.com/lewismj/cucumber</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:lewismj/cucumber.git</url>
    <connection>scm:git:git@github.com:lewismj/cucumber.git</connection>
  </scm>
  <developers>
    <developer>
      <id>lewismj</id>
      <name>Michael Lewis</name>
      <url>http://www.waioeka.com</url>
    </developer>
  </developers>),
 publishMavenStyle := true,
 publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
 }
)

// Version 0.0.9 - use 1.2.6-SNAPSHOT for Scala 2.12, until 1.2.6 is available.
lazy val cucumber = project.in(file("cucumber"))
			.settings(
			name := "cucumber-runner",
			organization := "com.waioeka.sbt",
			version := "0.0.9",
			scalaVersion := "2.12.1",
			resolvers += Resolver.sonatypeRepo("snapshots"), 
			libraryDependencies ++= Seq (
        			"info.cukes" % "cucumber-core" % "1.2.6-SNAPSHOT",
        			"info.cukes" %% "cucumber-scala" % "1.2.6-SNAPSHOT",
        			"info.cukes" % "cucumber-jvm" % "1.2.6-SNAPSHOT",
        			"info.cukes" % "cucumber-junit" % "1.2.6-SNAPSHOT",
        			"org.scala-sbt" % "test-interface" % "1.0")
			)
			.settings(publishSettings:_*)
