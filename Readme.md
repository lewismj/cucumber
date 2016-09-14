# An SBT Plugin For BDD Testing (Cucumber And Scala)


## Update

The plugin & runner (described below) should now be available from the [central repository] (http://search.maven.org/#search%7Cga%7C1%7Cwaioeka)
 
The latest version of the cucumber plugin is 0.0.5, the runner is 0.0.3

## Introduction

Many Scala projects will use the FlatSpec for their BDD like testing. Some teams prefer the separation of Feature files from the code. 

The support for Cucumber with Scala is quite mixed. There is an existing plugin [xsbt-cucumber-plugin](https://github.com/skipoleschris/xsbt-cucumber-plugin) but this plugin has a few issues. 

Firstly, it does not work on Windows (Classpath separator is incorrect). It uses the old SBT plugin interface and hasn’t been updated for some time. The author hasn’t responded to pull requests.


There are two core projects, each has an example proejct illustrating the usage. 

- cucumber-plugin
- cucumber-runner

The first provides a Cucumber plugin. This allow you to write ```sbt cucumber``` and invoke the Cucumber tests in a standalone JVM.

The second allows you to run ```sbt test``` and have the supplied test framework run the Cucumber tests. 

Both are effectively wrappers for Cucumber with different launch mechanisms.

The code is very early in development, just a few hours were spent putting together the core functionality we required. 

I would greatly welcome anyone that wants to help create a more fully featured (and bug free) version.

## Contact

Michael Lewis,  lewismj at waioeka.com

## Details

### cucumber-plugin

Note: You only need to do the following if you want to build the plugin from source yourself. The plugin is
now available from the Central Repository. 
i.e. In your end application just have

```
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.0.3")
```

in your plugins.sbt file.

If building from source locally, from your local Nexus repo, run the following.

	sbt compile
	sbt publishLocal

You are now ready to use the plugin to run feature file tests.
Start by looking at the test project cucumber-plugin-example.

### cucumber-plugin-example

After you have built and published the plugin. You can view an example use case by building and running the plugin example.

sbt compile
sbt cucumber

If you follow this example you can use the plugin for your test project. To do this, update your ```build.sbt``` as follows:

```	
	name := "cucumber-test"
	
	organization := "com.waioeka.sbt"
	
	version := "0.0.3"
	
	libraryDependencies ++= Seq (
	"info.cukes" % "cucumber-core" % "1.2.4" % "test",
	"info.cukes" %% "cucumber-scala" % "1.2.4" % "test",
	"info.cukes" % "cucumber-jvm" % "1.2.4" % "test",
	"info.cukes" % "cucumber-junit" % "1.2.4" % "test",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test")
	
	enablePlugins(CucumberPlugin)
	
	CucumberPlugin.glue := "com/waioeka/sbt/"
```

Remember to set the ```CucumberPlugin.glue``` parameter to the sub directory in ```test```
that contains your Scala step definitions.

For example, in your ```resources``` directory, put your feature file. For example:

```	
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

Note, if you need to generate the stubs, just run ```sbt cucumber``` and you will get an
error complaining about missing stubs, you can copy and paste the stub functions into your
step implementation.

You can now put in your stub implementation. For example:

```
	package com.waioeka.sbt
	
	import cucumber.api.scala.{ScalaDsl, EN}
	import org.scalatest.Matchers
	
	/**
	MultiplicationSteps
	  *
	  */
	class MultiplicationSteps extends ScalaDsl with EN with Matchers {
	  var x : Int = 0
	  var y : Int = 0
	  var z : Int = 0
	
	  Given("""^a variable x with value (\d+)$""") { (arg0: Int) =>
	x = arg0
	  }
	
	  Given("""^a variable y with value (\d+)$""") { (arg0: Int) =>
	y = arg0
	  }
	
	  When("""^I multiply x \* y$""") { () =>
	z = x * y
	  }
	
	 Then("""^I get (\d+)$""") { (arg0: Int) =>
	   z should be (arg0)
	 }
	}
```
You can now run the tests in standalone mode:
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

The result formats will be written:

- cucumber-html, standard Cucumber html output.
- cucumber.json, standard Cucumber json output.
- cucumber-junit-report.xml, a Junit style rest report.

### cucumber-runner-example

 If you want to run cucumber tests as part of an ```sbt test``` (unit test) run then, in addition to the plugin, first compile and publish the **cucumber-runner** project. The next step is to simply update your ```build.sbt``` file to reference the new test framework (Essentially a slightly different hook into running Cucumber).

 The ***cucumber-runner-example*** illustrates how to do this, and integrate BDD testing into unit test framework.

See below, you can now run ```sbt test``` in addition to ```sbt cucumber```. 

```
	mlewis@LEWISMJ-WINDEV MINGW64 /e/Dev/Plugins/upa-plugins/cucumber-runner-test (feature/test-runner)
	$ sbt cucumber
	[info] Loading project definition from E:\Dev\Plugins\upa-plugins\cucumber-runner-test\project
	[info] Set current project to cucumber-test2 (in build file:/E:/Dev/Plugins/upa-plugins/cucumber-runner-test/)
	[info] Compiling 1 Scala source to E:\Dev\Plugins\upa-plugins\cucumber-runner-test\target\scala-2.10\test-classes...
	[info] Feature: Multiplication
	[info]   In order to avoid making mistakes
	[info]   As a dummy
	[info]   I want to multiply numbers
	[info]
	[info]   Scenario: Multiply two variables  # Multiplication.feature:6
	[info]     Given a variable x with value 2 # MultiplicationSteps.scala:19
	[info]     And a variable y with value 3   # MultiplicationSteps.scala:23
	[info]     When I multiply x * y           # MultiplicationSteps.scala:27
	[info]     Then I get 6                    # MultiplicationSteps.scala:31
	[info]
	[info] 1 Scenarios (1 passed)
	[info] 4 Steps (4 passed)
	[info] 0m0.152s
	[info]
	[success] Total time: 5 s, completed 29-Dec-2015 15:28:51
	
	mlewis@LEWISMJ-WINDEV MINGW64 /e/Dev/Plugins/upa-plugins/cucumber-runner-test (feature/test-runner)
	$ sbt test
	[info] Loading project definition from E:\Dev\Plugins\upa-plugins\cucumber-runner-test\project
	[info] Set current project to cucumber-test2 (in build file:/E:/Dev/Plugins/upa-plugins/cucumber-runner-test/)
	Feature: Multiplication
	  In order to avoid making mistakes
	  As a dummy
	  I want to multiply numbers
	
	  Scenario: Multiply two variables  # Multiplication.feature:6
	Given a variable x with value 2 # MultiplicationSteps.scala:19
	And a variable y with value 3   # MultiplicationSteps.scala:23
	When I multiply x * y           # MultiplicationSteps.scala:27
	Then I get 6                    # MultiplicationSteps.scala:31
	
	1 Scenarios (1 passed)
	4 Steps (4 passed)
	0m0.170s
	
	[info] ScalaTest
	[info] Run completed in 672 milliseconds.
	[info] Total number of tests run: 0
	[info] Suites: completed 0, aborted 0
	[info] Tests: succeeded 0, failed 0, canceled 0, ignored 0, pending 0
	[info] No tests were executed.
	[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
	[success] Total time: 1 s, completed 29-Dec-2015 15:29:11
	
	mlewis@LEWISMJ-WINDEV MINGW64 /e/Dev/Plugins/upa-plugins/cucumber-runner-test (feature/test-runner)
```
In order to run ```sbt test``` you must add the following hook to your ```build.sbt``` file.

```
testFrameworks += new TestFramework("com.waioeka.sbt.runner")
```

### Cucumber arguments

You can now (v0.0.3) supply cucumber arguments. For example, ``` sbt "cucumber --tags ~@my-tag" ``` will
now correctly filter tagged feature files.

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
