name := "cucumber-test2"

organization := "com.waioeka.sbt"

scalaVersion := "2.12.2"

version := "0.0.7"

libraryDependencies ++= Seq (
        "io.cucumber" % "cucumber-core" % "2.0.0" % "test",
        "io.cucumber" %% "cucumber-scala" % "2.0.0" % "test",
        "io.cucumber" % "cucumber-jvm" % "2.0.0" % "test",
        "io.cucumber" % "cucumber-junit" % "2.0.0" % "test",
        "com.waioeka.sbt" %% "cucumber-runner" % "0.1.5",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test",
        "org.scalacheck" %% "scalacheck" % "1.13.4" % "test")



val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
//testOptions in Test += Tests.Argument(framework,"--monochrome")
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")

/** can remove pretty printing if running in parallel. */
parallelExecution in Test := true