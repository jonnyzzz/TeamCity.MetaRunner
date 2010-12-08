package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent._
import xml.RunnerSpec

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 14:30 
 */

class MetaRunnerBuildServiceFactory(val spec : RunnerSpec,
                                    val facade : BuildProcessFacade
                                   ) extends AgentBuildRunner with AgentBuildRunnerInfo {
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
  def canRun(agentConfiguration: BuildAgentConfiguration) = {
    //TODO: check all required build runners are available on build agent.
    //TODO: this is done on the server side now
    true
  }
}
