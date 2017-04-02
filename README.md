# Cucumber Test Framework & Plugin for SBT

<p align="left">
<img src="https://travis-ci.org/lewismj/cucumber.svg?branch=master"/>
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-runner_2.11/badge.svg"/>
</p>

## Summary

This project contains a Cucumber test framework (runner) for sbt. There is also a plugin that provides a new sbt command.

1. **_CucumberRunner_** A new *test framework* for sbt. It runs Cucumber tests as part of a unit test run (i.e. `sbt test`). 

2. _CucumberPlugin_ A _plugin_  provides a new command `sbt cucumber`. It allows you to run Cucumber tests independently of unit tests. 

Unless you have a specific requirement to run Cucumber tests outside of unit test framework, use the _CucumberRunner_.

**You don't need to use the _plugin_ if you just want to run Cucumber tests with `sbt test`, use the _runner_ test framework only.**

Waffle board [here][1]

## Notes

**0.0.8+** Cucumber Test Framework (_runner_)

**nb** Use **CucumberSpec** as the base class for your Cucumber test suite now. See the cucumber runner example.

In this version of the runner you can specify the Cucumber arguments via your `build.sbt` file, as follows:

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

testOptions in Test += Tests.Argument(framework,"--monochrome")
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
```

In your class definition, use:

```scala
class MyCucumberTestSuite extends CucumberSpec
```

## Dependency Information

_Plugin_
```scala
libraryDependencies += "com.waioeka.sbt" % "cucumber-plugin" % "0.1.4"
```

**_Runner_**
```scala
libraryDependencies += "com.waioeka.sbt" %% "cucumber-runner" % "0.0.8"
```

## Background

Many Scala projects will use FlatSpec for their BDD like testing. Some teams prefer the separation of Feature files from the code. 
There are two core projects, each has an example project illustrating the usage. 

## Contact

Michael Lewis: lewismj@waioeka.com

## Cucumber Runner

The _runner_ is a library that you can add as a dependency, if you want the Cucumber tests to run as part of a normal unit test run. That is, when you run `sbt test`. Your `build.sbt` file must reference the test framework as follows:

```scala
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")
testFrameworks += framework

// Configure the arguments.
testOptions in Test += Tests.Argument(framework,"--glue","")
testOptions in Test += Tests.Argument(framework,"--plugin","pretty")
testOptions in Test += Tests.Argument(framework,"--plugin","html:/tmp/html")
testOptions in Test += Tests.Argument(framework,"--plugin","json:/tmp/json")
```

Note, the runner will expect feature files in the `test/resources` directory. If your feature files are stored elsewhere, add that location to the 'unmanagedClasspath', e.g.

```scala
unmanagedClasspath in Test += baseDirectory.value / "src/test/features"
```

### Cucumber Runner Example

The project _cucumber-runner-example_ illustrates how to setup and use the _runner_. To integrate BDD testing into your unit test framework.
As shown below, using the runner and plugin, you can now run `sbt test`.


```
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


## Cucumber Plugin

The Cucumber plugin provides a new sbt command, allowing you to run just your Cucumber tests using `sbt cucumber`.
You need to add the following to your `plugins.sbt` file.

```scala
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.1.4")
```

### Cucumber Plugin Example

The project _cucumber-plugin-example_ highlights how to use the plugin. You will need to ensure that your `build.sbt` file defines both the dependencies and the 'glue' setting (i.e. where to find the step definitions).


```scala

    name := "cucumber-test"
 
    organization := "com.waioeka.sbt"
 
    version := "0.1.0"
 
    libraryDependencies ++= Seq (
    "info.cukes" % "cucumber-core" % "1.2.5" % "test",
    "info.cukes" %% "cucumber-scala" % "1.2.5" % "test",
    "info.cukes" % "cucumber-jvm" % "1.2.5" % "test",
    "info.cukes" % "cucumber-junit" % "1.2.5" % "test",
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

[1]:	https://waffle.io/lewismj/cucumber
