package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent.BuildAgentConfiguration
import xml.RunnerSpec
import scala.collection.JavaConversions._
import jetbrains.buildServer.agent.impl.BuildRunnerRegistryEx
import com.intellij.openapi.diagnostic.Logger

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 1:32
 */

class MetaRunnerCanRunCalculatorImpl(spec : RunnerSpec, registry : BuildRunnerRegistryEx) extends MetaRunnerCanRunCalculator {
  val LOG = Logger.getInstance(getClass.getName)

  def canRun(agentConfiguration: BuildAgentConfiguration) = {
    val filed = spec
      .runners
      .map(_.runType)
      .toSet
      .filter(id => {
          val runner = registry.findRunnerById(id)
          if (runner == null)
            true
          else
            !runner.getRunnerInfo.canRun(agentConfiguration)
        })
      .toList

    if (filed.nonEmpty) {
      LOG.info("Meta runner '" + spec.runType + "' was not registered: " +
              "Dependent runners are not available or not registered: " + filed)
      false
    } else {
      true
    }
  }
}

