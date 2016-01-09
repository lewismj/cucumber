name := "cucumber-runner"

organization := "com.waioeka.sbt"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.4",
        "info.cukes" % "cucumber-scala_2.10" % "1.2.4",
        "info.cukes" % "cucumber-jvm" % "1.2.4",
        "info.cukes" % "cucumber-junit" % "1.2.4",
        "org.scala-tools.testing" % "test-interface" % "0.5")
