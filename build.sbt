name := "cucumber-runner"
organization  := "com.waioeka"
crossScalaVersions :=  Seq("2.11.8","2.12.1")
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
   	"info.cukes" % "cucumber-core" % "1.2.6-SNAPSHOT",
  	"info.cukes" %% "cucumber-scala" % "1.2.6-SNAPSHOT",
       	"info.cukes" % "cucumber-jvm" % "1.2.6-SNAPSHOT",
       	"info.cukes" % "cucumber-junit" % "1.2.6-SNAPSHOT",
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
