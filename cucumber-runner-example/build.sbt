name := "cucumber-test2"

organization := "com.waioeka.sbt"

version := "0.0.6"

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.5" % "test",
        "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
        "info.cukes" % "cucumber-jvm" % "1.2.5" % "test",
        "info.cukes" % "cucumber-junit" % "1.2.5" % "test",
        "com.waioeka.sbt" %% "cucumber-runner" % "0.0.5",
        "org.scalatest" % "scalatest_2.10" % "3.0.0" % "test")

enablePlugins(CucumberPlugin)

CucumberPlugin.glue := "com/waioeka/sbt/"

testFrameworks += new TestFramework("com.waioeka.sbt.runner")
