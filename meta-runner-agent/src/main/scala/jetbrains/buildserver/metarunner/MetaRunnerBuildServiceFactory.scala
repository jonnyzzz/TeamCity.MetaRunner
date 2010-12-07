package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 14:30 
 */

class MetaRunnerBuildServiceFactory extends  AgentBuildRunner with AgentBuildRunnerInfo {
  def getRunnerInfo = null

  def createBuildProcess(runningBuild: AgentRunningBuild, context: BuildRunnerContext) = null

  def canRun(agentConfiguration: BuildAgentConfiguration) = false

  def getType = ""
}