/*
 * Copyright (c) 2017, Michael Lewis
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

import java.util.concurrent.atomic.AtomicInteger

import cucumber.runtime.io.{MultiLoader, ResourceLoaderClassFinder}
import cucumber.runtime.{Runtime, RuntimeOptions}
import sbt.testing._

import scala.util.Try


/** Cucumber test runner. */
case class CucumberRunner(args: Array[String],
                          remoteArgs: Array[String],
                          testClassLoader: ClassLoader)
  extends Runner {

  val numSuccess = new AtomicInteger(0)
  val numFailures = new AtomicInteger(0)

  def runTest(selectors: Seq[String],
              loggers: Seq[Logger],
              name: String,
              eventHandler: EventHandler): Unit = {

    def info(s: String) = loggers foreach (_ info s)

    def handle(op: OptionalThrowable, st: Status) = {
      eventHandler.handle(new Event {
        def fullyQualifiedName(): String = name
        def throwable(): OptionalThrowable = op
        def status(): Status = st
        def selector() = new TestSelector(fullyQualifiedName())

        def fingerprint() = new SubclassFingerprint {
          def superclassName: String = classOf[CucumberSpec].getName
          def isModule = false
          def requireNoArgConstructor = false
        }

        def duration() = 0
      })
    }

    /* if test inherit from CucumberTestSuite and specify features, we can run just those given. */
    val maybe = Try {
      val instance = Class.forName(name).newInstance.asInstanceOf[CucumberTestSuite]
      val adjArgs = args.map(x=> {
        /** should be a neater way. */
        if (x.startsWith("html:") ||
            x.startsWith("json:")) s"$x/${instance.outputSubDir}"
        else x
      })
      (instance.features,adjArgs)
    }.toOption

    val arguments = maybe match {
      case Some((features,adjArgs)) =>
        val ys = features.map(f => s"classpath:$f")
        adjArgs.toList ::: ys
      case _ => args.toList ::: List("classpath:")
    }
    val cArgs = if (arguments.contains("--glue")) arguments else arguments ::: List("--glue", "")


    val result = invokeCucumber(cArgs, testClassLoader).recover {
      case t: Throwable => handle(new OptionalThrowable(t), Status.Failure)
    }.get

    val index = name.lastIndexOf(".")
    val shortName = if (index > 0 && index < name.length - 1) name.substring(index + 1) else name
    result match {
      case 0 =>
        info(Console.GREEN + s"$shortName .. passed")
        numSuccess.incrementAndGet()
        handle(new OptionalThrowable(), Status.Success)
      case 1 =>
        info(Console.RED + s"$shortName .. failed")
        numFailures.incrementAndGet()
        handle(new OptionalThrowable(), Status.Failure)
    }
  }


  /** Runs the actual cucumber arguments. */
  def invokeCucumber(args: List[String], cl: ClassLoader) = Try {
    import scala.collection.JavaConverters._
    val opts = new RuntimeOptions(args.asJava)
    val rl = new MultiLoader(cl)
    val cf = new ResourceLoaderClassFinder(rl,cl)
    val runtime = new Runtime(rl, cf, cl, opts)
    runtime.run()
    runtime.exitStatus()
  }

  /** Output summary details. */
  override def done(): String = Console.CYAN + s"Tests: succeeded ${numSuccess.get()}, failed ${numFailures.get()}"


  override def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map(createTask)

  /** Create task from `TaskDef` */
  def createTask(t: TaskDef): Task = {
    new Task {
      override def taskDef(): TaskDef = t
      override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
        runTest(Seq.empty,loggers,t.fullyQualifiedName,eventHandler)
        Array.empty
      }
      override def tags(): Array[String] = Array.empty
    }
  }

}
