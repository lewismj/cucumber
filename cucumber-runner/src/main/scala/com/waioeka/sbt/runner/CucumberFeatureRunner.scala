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

package com.waioeka.sbt.runner

import cucumber.runtime.{Runtime, RuntimeOptions}
import cucumber.runtime.io.{ResourceLoaderClassFinder, MultiLoader}
import org.scalatools.testing.{Fingerprint, EventHandler, Runner2, Logger}

import scala.util.Try

/**
  * CucumberFeatureRunner
  *   This class implements the Runner2 interface for running Cucumber tests.
  */
class CucumberFeatureRunner(classLoader: ClassLoader, loggers: Array[Logger])
  extends Runner2 {

  def logDebug(s: String) : Unit = loggers foreach(_ debug s)
  def logError(s: String) : Unit = loggers foreach(_ debug s)

  /**
    * Run a Cucumber test.
    *
    * @param testName         the name of the test.
    * @param fingerprint      the identification of the test (CucumberRunner).
    * @param eventHandler     the event handler.
    * @param args             the test arguments.
    */
  def run(
            testName      : String,
            fingerprint   : Fingerprint,
            eventHandler  : EventHandler,
            args          : Array[String]) : Unit = {

    Try {

      /* TODO Take configurable arguments configurable. */
      val arguments =
        List("--glue","") :::
          List("--plugin", "pretty") :::
            List("--plugin", "html:html") :::
              List("--plugin", "json:json") :::
                List("classpath:")

      execute(arguments, classLoader) match {
        case 0 =>
          logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName " +
            " completed successfully.")
          eventHandler.handle(SuccessEvent(testName))
        case _ =>
          logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName " +
            " failed.")
          eventHandler.handle(FailureEvent(testName))
      }
    }.recover {
      case t: Throwable =>
          eventHandler.handle(ErrorEvent(testName,t))
    }.get
  }

  /**
    * Create the Cucumber Runtime and execute the test.

    * @param arguments	 the test and Cucumber arguments.
    * @param classLoader the class loader for the Runtime.
    * @return the exit status of the Cucumber Runtime.
    */
  def execute(
           arguments: List[String],
           classLoader: ClassLoader
         )
  : Int = {

    import scala.collection.JavaConverters._
    val arrayList : java.util.ArrayList[String] =
      new java.util.ArrayList[String](arguments.asJava)
    val runtimeOptions = new RuntimeOptions(arrayList)
    val resourceLoader = new MultiLoader(classLoader)
    val classFinder = new ResourceLoaderClassFinder(resourceLoader,classLoader)
    val runtime = new Runtime(
                              resourceLoader,
                              classFinder,
                              classLoader,
                              runtimeOptions)
    runtime.run()
    runtime.printSummary()
    runtime.exitStatus()
  }

}
