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

import sbt.OutputStrategy


/**
  * Cucumber
  *   Companion object for the Cucumber class. Provides apply method.
  */
object Cucumber {
  /**
    * Creates a new Cucumber object.
    *
    * @param jvmParameters        the JVM parameters.
    * @param cucumberParameters   the Cucumber parameters.
    * @return
    */
  def apply(
             jvmParameters: JvmParameters,
             cucumberParameters: CucumberParameters) : Cucumber = {
    new Cucumber(jvmParameters)(cucumberParameters)
  }
}

/**
  * Cucumber
  *   This class is responsible for running Cucumber.
  *
  * @param jvmParameters  the JVM parameters
  * @param options        the Cucumber parameters.
  */
class Cucumber(
              jvmParameters: JvmParameters)(
              options: CucumberParameters
              ) {

  /** Standalone JVM that will run Cucumber. */
  val jvm : Jvm = Jvm(jvmParameters.classPath, jvmParameters.systemProperties)

  /**
    * Run Cucumber, within the JVM.
    */
  def run(output: OutputStrategy) : Int = 
  	jvm.run(jvmParameters.mainClass,options.toList)(output)

}
