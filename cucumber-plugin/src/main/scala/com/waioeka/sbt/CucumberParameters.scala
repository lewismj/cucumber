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


/**
  * Cucumber Options
  *
  *   This case class defines the Cucumber options.
  *
  * @param dryRun         true, if run should be a dry run.
  * @param features       the path(s) to the features.
  * @param monochrome     whether or not to use monochrome output.
  * @param plugins        what plugin(s) to use.
  * @param glue           where glue code is loaded from.
  * @param additionalArgs additional arguments to pass through to Cucumber.
  */
case class CucumberParameters(
                            dryRun      : Boolean,
                            features    : List[String],
                            monochrome  : Boolean,
                            plugins     : List[Plugin],
                            glue        : String,
                            additionalArgs: List[String]) {

  /**
    * Create a list of one element
    *
    * @param parameter        the boolean parameter.
    * @param parameterName    the name of the parameter.
    * @return a list containing the parameter if true, false otherwise.
    */
  def boolToParameter(
                       parameter: Boolean,
                       parameterName: String)
  = parameter match {
    case true => List(s"--$parameterName")
    case _ => List()
  }

  /**
    * Return the Cucumber parameters as a list of strings.
    *
    * @return a list of string parameters.
    */
  def toList : List[String] = {
    val featureOpts = features mkString " "

    boolToParameter(dryRun,"dry-run") :::
      boolToParameter(monochrome,"monochrome") :::
      List("--glue",s"$glue") :::
      plugins.map(_.toCucumberPlugin).flatMap(plugin => Seq("--plugin", plugin)) :::
      additionalArgs :::
      List(s"$featureOpts")
  }

}

sealed trait Plugin {
  def toCucumberPlugin : String
}

sealed abstract class FilePlugin(name: String, file: File) extends Plugin {
  override def toCucumberPlugin: String = s"$name:${file.getAbsolutePath}"
}

object Plugin {

  case object PrettyPlugin extends Plugin {
    override def toCucumberPlugin: String = "pretty"
  }

  case class HtmlPlugin(file: File) extends FilePlugin("html", file)

  case class JsonPlugin(file: File) extends FilePlugin("json", file)

  case class JunitPlugin(file: File) extends FilePlugin("junit", file)

}
