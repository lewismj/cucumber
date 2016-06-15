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


/**
  * Cucumber Options
  *
  *   This case class defines the Cucumber options.
  *
  * @param dryRun         true, if run should be a dry run.
  * @param features       the path(s) to the features.
  * @param monochrome     whether or not to use monochrome output.
  * @param plugin         what plugin(s) to use.
  * @param glue           where glue code is loaded from.
  */
case class CucumberParameters(
                            dryRun      : Boolean,
                            features    : List[String],
                            monochrome  : Boolean,
                            plugin      : List[String],
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

    /* TODO Make the output directories of the default plugins configurable. */
    boolToParameter(dryRun,"dry-run") :::
      boolToParameter(monochrome,"monochrome") :::
      List("--glue",s"$glue") :::
      List("--plugin","pretty") :::
      List("--plugin","html:cucumber-html") :::
      List("--plugin","json:cucumber.json") :::
      List("--plugin","junit:cucumber-junit-report.xml") :::
      additionalArgs :::
      List(s"$featureOpts")
  }

}
