# Cucumber Plugin

The Cucumber plugin provides a new sbt command, allowing you to run just your Cucumber tests using `sbt cucumber`.
You need to add the following to your `plugins.sbt` file.


```scala
 addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.1.7")
```

### Cucumber Plugin Example

The project _cucumber-plugin-example_ highlights how to use the plugin. You will need to ensure that your `build.sbt` file defines both the dependencies and the 'glue' setting (i.e. where to find the step definitions).


```scala

    name := "cucumber-test"
 
    organization := "com.waioeka.sbt"
 
    version := "0.1.0"
 
    libraryDependencies ++= Seq (
    "io.cucumber" % "cucumber-core" % "2.0.0" % "test",
    "io.cucumber" %% "cucumber-scala" % "2.0.0" % "test",
    "io.cucumber" % "cucumber-jvm" % "2.0.0" % "test",
    "io.cucumber" % "cucumber-junit" % "2.0.0" % "test",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test")
 
    enablePlugins(CucumberPlugin)
 
    CucumberPlugin.glue := "com/waioeka/sbt/"
```

Remember to set the `CucumberPlugin.glue` parameter to the sub directory in *test* that contains your Scala step definitions.
In your *resources* directory, put in your feature files. 

```cucumber 
    @my-tag
    Feature: Multiplication
      In order to avoid making mistakes
      As a dummy
      I want to multiply numbers
 
    Scenario: Multiply two variables
    Given a variable x with value 2
    And a variable y with value 3
    When I multiply x * y
    Then I get 6
``` 

If you need to generate the stubs, just run `sbt cucumber` and you will get an
error complaining about missing stubs. You can copy and paste the stub functions into your
step implementation.

You can now put in your stub implementation:

```scala
    package com.waioeka.sbt
 
    import cucumber.api.scala.{ScalaDsl, EN}
    import org.scalatest.Matchers
 
    /** MultiplicationSteps */
    class MultiplicationSteps extends ScalaDsl with EN with Matchers {
      var x : Int = 0
      var y : Int = 0
      var z : Int = 0
 
      Given("""^a variable x with value (\d+)$""") { (arg0: Int) => x = arg0 }
 
      Given("""^a variable y with value (\d+)$""") { (arg0: Int) => y = arg0 }
 
      When("""^I multiply x \* y$""") { () => z = x * y }
 
      Then("""^I get (\d+)$""") { (arg0: Int) => z should be (arg0) }
    }
```
To run the tests in standalone mode:
```
sbt compile
sbt cucumber
```
You should see some output as follows:

```
paeroa:cucumber-test lewismj$ sbt cucumber
[info] Loading project definition from /Users/lewismj/waioeka/upa-plugins/cucumber-test/project
[info] Set current project to cucumber-test (in build file:/Users/lewismj/waioeka/upa-plugins/cucumber-test/)
[info] Feature: Multiplication
[info]   In order to avoid making mistakes
[info]   As a dummy
[info]   I want to multiply numbers
[info]
[info]   Scenario: Multiply two variables  # Multiplication.feature:6
[info]     Given a variable x with value 2 # MultiplicationSteps.scala:17
[info]     And a variable y with value 3   # MultiplicationSteps.scala:21
[info]     When I multiply x * y           # MultiplicationSteps.scala:25
[info]     Then I get 6                    # MultiplicationSteps.scala:29
[info]
[info] 1 Scenarios (1 passed)
[info] 4 Steps (4 passed)
[info] 0m0.106s
[info]
[success] Total time: 1 s, completed 28-Dec-2015 22:16:19
 ```

The results will be output in the following formats:

- cucumber-html, standard Cucumber html output.
- cucumber.json, standard Cucumber json output.
- cucumber-junit-report.xml, a Junit style rest report.

## Cucumber Plugin Arguments

Cucumber arguments may be supplied. For example, `sbt "cucumber --tags ~@my-tag"` will filter tagged feature files.

```
[success] Total time: 1 s, completed 15-Jun-2016 09:23:22
paeroa:cucumber-plugin-example lewismj$ sbt "cucumber --tags ~@my-tag"
[info] Loading global plugins from /Users/lewismj/.sbt/0.13/plugins
[info] Loading project definition from /Users/lewismj/github/cucumber/cucumber-plugin-example/project
[info] Set current project to cucumber-test (in build file:/Users/lewismj/github/cucumber/cucumber-plugin-example/)
** hello **
[info] None of the features at [classpath:] matched the filters: [~@my-tag]
[info] 
[info] 0 Scenarios
[info] 0 Steps
[info] 0m0.000s
[info] 
** goodbye **
[success] Total time: 1 s, completed 15-Jun-2016 09:23:41
```
