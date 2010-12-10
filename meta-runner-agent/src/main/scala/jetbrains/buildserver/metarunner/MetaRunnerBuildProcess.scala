package jetbrains.buildserver.metarunner

import scala.util.control.Breaks
import jetbrains.buildServer.agent._
import java.util.concurrent.atomic.{AtomicReference, AtomicBoolean}
import xml._
import jetbrains.buildserver.metarunner.util.BuildParametersUtil._
import jetbrains.buildServer.RunBuildException
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 21:49 
 */

class MetaRunnerBuildProcess(
                                    val spec : RunnerSpec,
                                    val factory: BuildProcessFacade,
                                    val build : AgentRunningBuild,
                                    val runner : BuildRunnerContext
                                    )
        extends BuildProcess {
  private val myIsInterrupted = new AtomicBoolean(false)
  private val myIsFinished = new AtomicBoolean(false)
  private val myCurrrentStep = new AtomicReference[BuildProcess](null)

  private def checkInterrupted() : Boolean = myIsInterrupted.get()

  def start = {

  }

  def interrupt = {
    val step = myCurrrentStep.get()
    if (step != null) step.interrupt()

    myIsInterrupted.set(true)
  }

  def isFinished = myIsFinished.get

  def isInterrupted = myIsInterrupted.get()

  def waitFor = {
    val mybreaks = new Breaks
    import mybreaks.break

    for(x <- spec.runners) {
      if (checkInterrupted) break
      ///TODO: support error checking here
      callRunner(x)
    }

    if (checkInterrupted) BuildFinishedStatus.INTERRUPTED
    BuildFinishedStatus.FINISHED_SUCCESS
  }

  private def callRunner(spec : RunnerStepSpec) : BuildFinishedStatus = {


    //TODO: Support working directory
    val ctx = factory.createBuildRunnerContext(build, spec.runType, "", runner)
    for( par <- spec.parameters) {
      val key = par.scope
      key match {
        case RunnerScope => ctx.addRunnerParameter(par.key, par.value)
        case BuildScope =>
          par.key match {
            case ENV_PREFIX(x) => ctx.addEnvironmentVariable(x, par.value)
            case SYSTEM_PREFIX(x) => ctx.addSystemProperty(x, par.value)
            case x => throw new RunBuildException(
              "Build parameter should start with " +
                      "" + Constants.ENV_PREFIX + " or " +
                      "" + Constants.SYSTEM_PREFIX + " " +
                      "but was: "+ x)
          }
      }
    }

    val exec: BuildProcess = factory.createExecutable(build, ctx)
    myCurrrentStep.set(exec)
    try {
      if (checkInterrupted) BuildFinishedStatus.INTERRUPTED
      exec.start()
      exec.waitFor()
    } finally {
      myCurrrentStep.set(null)
    }
  }
}
