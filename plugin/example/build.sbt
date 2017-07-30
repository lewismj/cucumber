name := "cucumber-test"

organization := "com.waioeka.sbt"

version := "0.0.3"

scalaVersion := "2.12.2"

// need snapshots for 2.0.0 cucumber that supports Scala 2.12
resolvers += Resolver.sonatypeRepo("snapshots")


libraryDependencies ++= Seq (
        "io.cucumber" % "cucumber-core" % "2.0.0-SNAPSHOT" % "test",
        "io.cucumber" %% "cucumber-scala" % "2.0.0-SNAPSHOT" % "test",
        "io.cucumber" % "cucumber-jvm" % "2.0.0-SNAPSHOT" % "test",
        "io.cucumber" % "cucumber-junit" % "2.0.0-SNAPSHOT" % "test",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test")

enablePlugins(CucumberPlugin)

CucumberPlugin.monochrome := false
CucumberPlugin.glue := "com/waioeka/sbt/"

def beforeAll() : Unit = { println("** hello **") }
def afterAll() : Unit = { println("** goodbye **") }

CucumberPlugin.beforeAll := beforeAll
CucumberPlugin.afterAll := afterAll

