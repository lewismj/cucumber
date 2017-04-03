name := "cucumber-test2"

organization := "com.waioeka.sbt"

scalaVersion := "2.11.8"

version := "0.0.6"

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.5" % "test",
        "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
        "info.cukes" % "cucumber-jvm" % "1.2.5" % "test",
        "info.cukes" % "cucumber-junit" % "1.2.5" % "test",
        "com.waioeka.sbt" %% "cucumber-runner" % "0.0.8",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",
        "org.scalacheck" %% "scalacheck" % "1.13.4" % "test")



val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
testOptions in Test += Tests.Argument(framework,"--monochrome")
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
