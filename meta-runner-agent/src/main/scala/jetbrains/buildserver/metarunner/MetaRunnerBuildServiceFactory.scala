package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent._
import xml.RunnerSpec

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 14:30 
 */

class MetaRunnerBuildServiceFactory(spec: RunnerSpec,
                                    facade: BuildProcessFacade,
                                    canRunCalculator: MetaRunnerCanRunCalculator)
        extends AgentBuildRunner with AgentBuildRunnerInfo {
  @Override
  def getRunnerInfo = this

  @Override
  def createBuildProcess(runningBuild: AgentRunningBuild, context: BuildRunnerContext) = {
    new MetaRunnerBuildProcess(
      spec,
      facade,
      runningBuild,
      context
    )
  }

  @Override
  def getType = spec.runType

  @Override
  def canRun(agentConfiguration: BuildAgentConfiguration) = canRunCalculator.canRun(agentConfiguration)
}
