name := "cucumber-test"

organization := "com.waioeka.sbt"

version := "0.0.4"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq (
        "io.cucumber" % "cucumber-core" % "2.0.0" % "test",
        "io.cucumber" %% "cucumber-scala" % "2.0.0" % "test",
        "io.cucumber" % "cucumber-jvm" % "2.0.0" % "test",
        "io.cucumber" % "cucumber-junit" % "2.0.0" % "test",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test")

enablePlugins(CucumberPlugin)


CucumberPlugin.envProperties := Map("K"->"2049")

CucumberPlugin.monochrome := false
CucumberPlugin.glue := "com/waioeka/sbt/"

def before() : Unit = { println("beforeAll") }
def after() : Unit = { println("afterAll") }

CucumberPlugin.beforeAll := before
CucumberPlugin.afterAll := after
