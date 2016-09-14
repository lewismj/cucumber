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

import java.io.File
import org.apache.commons.lang3.SystemUtils
import sbt._


/**
  * JvmRunner
  *   Provide functions for launching JVM.
  */
case class Jvm(classPath: List[File], systemProperties : Map[String, String]) {

  /** Classpath separator, must be ';' for Windows, otherwise : */
  private val sep = if (SystemUtils.IS_OS_WINDOWS) ";" else ":"

  /** The Jvm parameters. */
  private val jvmArgs : Seq[String]
          = Seq("-classpath", classPath map(_.toPath) mkString sep) ++ systemProperties.toList.map{case (key, value) => s"-D$key=$value"}

  /**
    * Invoke the main class.
    *
    * @param mainClass        the class name containing the main method.
    * @param parameters       the parameters to pass to the main method.
    * @param outputStrategy   the SBT output strategy.
    * @return  the return code of the Jvm.
    */
  def run(  mainClass : String,
            parameters : List[String])(
            outputStrategy: OutputStrategy) : Int = {

    val logger : Logger = outputStrategy.asInstanceOf[LoggedOutput].logger

    val args  = jvmArgs ++  (mainClass :: parameters)
    val debug = args mkString " "
    logger.debug(s"[Jvm.run] Args $debug")

    Fork.java(ForkOptions(None,Some(outputStrategy)),args)
  }


}
