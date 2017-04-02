# An SBT Plugin For BDD Testing

<p align="left">
<img src="https://travis-ci.org/lewismj/cucumber.svg?branch=master"/>
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-plugin/badge.svg"/>
</p>

## Summary

Two SBT plugins are provided for running Cucumber tests.

A _plugin_ that provides a new command `sbt cucumber`. It allows you to run Cucumber tests independently of unit tests. 

The other (_runner_) will run Cucumber tests as part of a unit test run (i.e. `sbt test`).


## Issues

Waffle board [here](https://waffle.io/lewismj/cucumber)

## Dependency Information

_Plugin_
```scala
libraryDependencies += "com.waioeka.sbt" % "cucumber-plugin" % "0.1.3"
```

_Runner_
```scala
libraryDependencies += "com.waioeka.sbt" %% "cucumber-runner" % "0.0.7"
```

## Introduction

Many Scala projects will use FlatSpec for their BDD like testing. Some teams prefer the separation of Feature files from the code. 
There are two core projects, each has an example project illustrating the usage. 

- cucumber-plugin
- cucumber-runner

The first provides an sbt plugin. This allow you to write `sbt cucumber` and invoke the Cucumber tests in a standalone JVM.
The second allows you to run `sbt test` and have the supplied test framework run the Cucumber tests. 

## Contact

Michael Lewis: lewismj@waioeka.com


## Cucumber Plugin 

The Cucumber plugin provides a new sbt command, allowing you to run just your Cucumber tests using `sbt cucumber`.
You need to add the following to your `plugins.sbt` file.

```scala
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.1.3")
```

### Cucumber Plugin Example

The project _cucumber-plugin-example_ highlights how to use the plugin. You will need to ensure that your `build.sbt` file defines both the dependencies and the 'glue' setting (i.e. where to find the step definitions).


```scala

    name := "cucumber-test"
 
    organization := "com.waioeka.sbt"
 
    version := "0.1.0"
 
    libraryDependencies ++= Seq (
    "info.cukes" % "cucumber-core" % "1.2.4" % "test",
    "info.cukes" %% "cucumber-scala" % "1.2.4" % "test",
    "info.cukes" % "cucumber-jvm" % "1.2.4" % "test",
    "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test")
 
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

## Cucumber Runner

The _runner_ is a library that you can add as a dependency, if you want the Cucumber tests to run as part of a normal unit test run. That is, when you run `sbt test`. Your `build.sbt` file must reference the test framework as follows:

```scala
testFrameworks += new TestFramework("com.waioeka.sbt.runner")
```

Note, the runner will expect feature files in the `test/resources` directory. If your feature files are stored elsewhere, add that location to the 'unmanagedClasspath', e.g.


```scala
unmanagedClasspath in Test += baseDirectory.value / "src/test/features"
```

### Cucumber Runner Example

The project _cucumber-runner-example_ illustrates how to do this, to integrate BDD testing into your unit test framework.

As shown below, using the runner and plugin, you can now run `sbt test` in addition to `sbt cucumber`.


```
> cucumber
[info] @my-test
[info] Feature: Multiplication
[info]   In order to avoid making mistakes
[info]   As a dummy
[info]   I want to multiply numbers
[info] 
[info]   Scenario: Multiply two variables  # Multiplication.feature:7
[info]     Given a variable x with value 2 # MultiplicationSteps.scala:44
[info]     And a variable y with value 3   # MultiplicationSteps.scala:48
[info]     When I multiply x * y           # MultiplicationSteps.scala:52
[info]     Then I get 6                    # MultiplicationSteps.scala:56
[info] 
[info] 1 Scenarios (1 passed)
[info] 4 Steps (4 passed)
[info] 0m0.117s
[info] 
[success] Total time: 1 s, completed 02-Apr-2017 23:16:51
> test
[info] ExampleSpec:
[info] - An empty Set should have size 0
@my-test
Feature: Multiplication
  In order to avoid making mistakes
  As a dummy
  I want to multiply numbers

  Scenario: Multiply two variables  # Multiplication.feature:7
    Given a variable x with value 2 # MultiplicationSteps.scala:44
    And a variable y with value 3   # MultiplicationSteps.scala:48
    When I multiply x * y           # MultiplicationSteps.scala:52
    Then I get 6                    # MultiplicationSteps.scala:56

1 Scenarios (1 passed)
4 Steps (4 passed)
0m0.081s


1 Scenarios (1 passed)
4 Steps (4 passed)
0m0.081s

[info] CucumberTestSuite .. passed
[info] ScalaTest
[info] Run completed in 422 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] CucumberTest
[info] Tests: succeeded 1, failed 0
[info] Passed: Total 2, Failed 0, Errors 0, Passed 2
[success] Total time: 1 s, completed 02-Apr-2017 23:16:53
>
```

## Cucumber Arguments


### Plugin

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
