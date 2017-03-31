name := "cucumber-runner"

scalaVersion := "2.11.8"

organization := "com.waioeka.sbt"

version := "0.0.6"

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.5",
        "info.cukes" %% "cucumber-scala" % "1.2.5",
        "info.cukes" % "cucumber-jvm" % "1.2.5",
        "info.cukes" % "cucumber-junit" % "1.2.5",
        "org.scala-tools.testing" % "test-interface" % "0.5")

pomIncludeRepository := { _ => false }

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
  </developers>)


publishMavenStyle := true


publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
