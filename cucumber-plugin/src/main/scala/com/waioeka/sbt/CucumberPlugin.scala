/*
 * Copyright (c) 2015, Michael Lewis
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.waioeka.sbt

import sbt._
import Keys._
import sbt.complete.DefaultParsers._

import scala.util.Try

/**
  * CucumberPlugin
  *   This class implements the AutoPlugin interface. The Cucumber plugin
  *   will invoke Cucumber for Scala.
  */
object CucumberPlugin extends AutoPlugin {

  /** TaskKey for the cucumber plugin.                            */
  lazy val cucumber = InputKey[Unit]("cucumber", "[Cucumber] Runs Scala Cucumber.")

  /** The main class of the Cucumber test runner.                 */
  val mainClass = SettingKey[String]("cucumber-main-class")

  /** True, if run should be a dry run.                           */
  val dryRun = SettingKey[Boolean]("cucumber-dry-run")

  /** The path(s) to the features.                                */
  val features = SettingKey[List[String]]("cucumber-features")

  val systemProperties = settingKey[Map[String, String]]("system properties")

  /** Whether or not to use monochrome output.                    */
  val monochrome = SettingKey[Boolean]("cucumber-monochrome")

  /** What plugin(s) to use.                                      */
  val plugin = SettingKey[List[Plugin]]("cucumber-plugins")

  val cucumberTestReports = settingKey[File]("The location for test reports")

  /**
    * Where glue code (step definitions, hooks and plugins)
    * are loaded from.
    */
  val glue = SettingKey[String]("cucumber-glue")

  /** A beforeAll hook for Cucumber tests.                      */
  val beforeAll = SettingKey[()=>Unit]("cucumber-before")

  /** An afterAll hook for Cucumber tests.                      */
  val afterAll = SettingKey[()=>Unit]("cucumber-after")

  /** Default hook for beforeAll, afterAll.                     */
  private def noOp() : Unit = {}

  /**
    * Defines the project settings for this plugin.
    *
    * @return a Sequence of SBT settings.
    */
  override def projectSettings : Seq[Setting[_]] = Seq (

    testFrameworks +=
      new TestFramework("com.waioeka.sbt.runner.CucumberFramework"),

    cucumber := {

      val args: Seq[String] = spaceDelimited("<arg>").parsed

      val outputStrategy = LoggedOutput(streams.value.log)

      val p1 = ((fullClasspath in Test)
                map { cp => cp.toList.map(_.data)}).value

      /* TODO - Add support for it:test */

      //val p2 = ((fullClasspath in IntegrationTest)
      //           map { cp => cp.toList.map(_.data)}).value

      val p = CucumberParameters(
                                  dryRun.value,
                                  features.value,
                                  monochrome.value,
                                  plugin.value,
                                  glue.value,
                                  args.toList)

      val j = JvmParameters(
        mainClass = mainClass.value,
        classPath = p1,
        systemProperties = systemProperties.value
      )
 //::: p2)

      beforeAll.value()
      runCucumber(j,p)(outputStrategy)
      afterAll.value()
    },

    mainClass := "cucumber.api.cli.Main",
    dryRun := false,
    features := List("classpath:"),
    monochrome := false,
    cucumberTestReports := new File(new File(target.value, "test-reports"), "cucumber"),
    systemProperties := Map(),
    plugin := {
      import Plugin._
      val cucumberDir = cucumberTestReports.value
      IO.createDirectory(cucumberDir)
      List(PrettyPlugin,
        HtmlPlugin(cucumberDir),
        JsonPlugin(new File(cucumberDir, "cucumber.json")),
        JunitPlugin(new File(cucumberDir, "junit-report.xml"))
      )
    },
    beforeAll := noOp,
    afterAll := noOp
  )


  /**
    * Run Cucumber with the given parameters.
    *
    * @param jParams the Jvm parameters.
    * @param cParams the Cucumber parameters
    */
  def runCucumber(  jParams : JvmParameters,
                    cParams: CucumberParameters)(
                    outputStrategy: OutputStrategy
                  ) : Int = {
    val cucumber = Cucumber(jParams,cParams)
    Try {
       cucumber.run(outputStrategy)
    }.recover {
      case t: Throwable =>
        println(s"[CucumberPlugin] Caught exception: ${t.getMessage}")
        -1
    }.get
  }

}
