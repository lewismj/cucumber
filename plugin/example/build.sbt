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

CucumberPlugin.monochrome := false
CucumberPlugin.glue := "com/waioeka/sbt/"

def beforeAll() : Unit = { println("** hello **") }
def afterAll() : Unit = { println("** goodbye **") }

CucumberPlugin.beforeAll := beforeAll
CucumberPlugin.afterAll := afterAll

