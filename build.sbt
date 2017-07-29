name := "cucumber-runner"
organization  := "com.waioeka.sbt"
scalaVersion := "2.12.2"
version := "0.0.9"

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

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

publishMavenStyle := true

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
       	</developers>
      )
