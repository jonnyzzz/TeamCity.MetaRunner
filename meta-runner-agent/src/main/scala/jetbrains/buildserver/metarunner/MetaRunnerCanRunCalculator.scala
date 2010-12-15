package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent.BuildAgentConfiguration

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 1:32 
 */

trait MetaRunnerCanRunCalculator {
  def canRun(agentConfiguration: BuildAgentConfiguration) : Boolean
}

