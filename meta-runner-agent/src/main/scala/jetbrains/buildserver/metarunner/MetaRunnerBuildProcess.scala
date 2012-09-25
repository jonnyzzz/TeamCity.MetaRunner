package jetbrains.buildserver.metarunner

import scala.util.control.Breaks
import jetbrains.buildServer.agent._
import java.util.concurrent.atomic.{AtomicReference, AtomicBoolean}
import xml._
import jetbrains.buildserver.metarunner.util.BuildParametersUtil._
import jetbrains.buildServer.RunBuildException
import scala.collection.JavaConversions._
import jetbrains.buildServer.util.FileUtil._
import com.intellij.openapi.util.io.FileUtil

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 21:49 
 */

class MetaRunnerBuildProcess(spec: RunnerSpec,
                             factory: BuildProcessFacade,
                             build: AgentRunningBuild,
                             runner: BuildRunnerContext) extends BuildProcess {
  private val myIsInterrupted = new AtomicBoolean(false)
  private val myIsFinished = new AtomicBoolean(false)
  private val myCurrentStep = new AtomicReference[BuildProcess](null)

  private def checkInterrupted() : Boolean = myIsInterrupted.get

  def start() {
    if (spec.getMetaRunnerRoot.isDirectory) {
      val resources = createTempDirectory("meta-runner-" + spec.runType, ".resources", build.getAgentTempDirectory)
      FileUtil.copyDir(spec.getMetaRunnerRoot, resources)

      runner.addRunnerParameter("meta.runner.resources.path", getCanonicalFile(resources).getPath)
    }
  }

  def interrupt() {
    val step = myCurrentStep.get()
    if (step != null) step.interrupt()

    myIsInterrupted.set(true)
  }

  def isFinished = myIsFinished.get
  def isInterrupted = myIsInterrupted.get

  def waitFor = {
    val myBreaks = new Breaks
    import myBreaks.break

    for(x <- spec.runners) {
      if (checkInterrupted()) break()
      ///TODO: support error checking here
      callRunner(x)
    }

    if (checkInterrupted()) BuildFinishedStatus.INTERRUPTED
    BuildFinishedStatus.FINISHED_SUCCESS
  }

  private def callRunner(spec : RunnerStepSpec) : BuildFinishedStatus = {
    //TODO: Support working directory
    val ctx = factory.createBuildRunnerContext(build, spec.runType, "", runner)
    for( par <- spec.parameters) {
      val key = par.scope
      val value = MetaReferenceResolver(par.value, runner.getRunnerParameters)

      key match {
        case RunnerScope => ctx.addRunnerParameter(par.key, value)
        case BuildScope =>
          par.key match {
            case ENV_PREFIX(x) => ctx.addEnvironmentVariable(x, value)
            case SYSTEM_PREFIX(x) => ctx.addSystemProperty(x, value)
            case x => throw new RunBuildException(
              "Build parameter should start with " +
                      "" + Constants.ENV_PREFIX + " or " +
                      "" + Constants.SYSTEM_PREFIX + " " +
                      "but was: "+ x)
          }
      }
    }

    val exec: BuildProcess = factory.createExecutable(build, ctx)
    myCurrentStep.set(exec)
    try {
      if (checkInterrupted()) BuildFinishedStatus.INTERRUPTED
      exec.start()
      exec.waitFor()
    } finally {
      myCurrentStep.set(null)
    }
  }
}
