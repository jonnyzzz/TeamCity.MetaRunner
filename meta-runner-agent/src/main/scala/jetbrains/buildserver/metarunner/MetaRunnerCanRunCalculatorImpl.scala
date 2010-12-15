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
    val refs = spec.runners.map(_.runType).toSet
    val runTypes = refs.map(x=>(x->registry.findRunnerById(x))).toMap
    val filed = runTypes.mapValues(x=>x != null && x.getRunnerInfo.canRun(agentConfiguration))
            .toMap.filterNot(_._2).keys.toList

    if (filed.nonEmpty) {
      LOG.info("Meta runner '" + spec.runType + "' was not registered: " +
              "Dependent runners are not available or not registered: " + filed)
      false
    } else {
      true
    }
  }
}

