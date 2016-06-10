name := "cucumber-plugin"

organization := "com.waioeka"

sbtPlugin := true

version := "0.0.1"

libraryDependencies ++= Seq (
	"info.cukes" % "cucumber-core" % "1.2.4",
	"info.cukes" %% "cucumber-scala" % "1.2.4",
	"info.cukes" % "cucumber-jvm" % "1.2.4",
	"info.cukes" % "cucumber-junit" % "1.2.4",
	"org.apache.commons" % "commons-lang3" % "3.4")
