name := "cucumber-test2"

organization := "com.waioeka"

version := "0.0.1"

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.4" % "test",
        "info.cukes" %% "cucumber-scala" % "1.2.4" % "test",
        "info.cukes" % "cucumber-jvm" % "1.2.4" % "test",
        "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
        "com.waioeka" %% "cucumber-runner" % "0.0.1",
        "org.scalatest" % "scalatest_2.10" % "2.2.4" % "test")

enablePlugins(CucumberPlugin)

CucumberPlugin.glue := "com/waioeka/sbt/"

testFrameworks += new TestFramework("com.waioeka.sbt.runner")
