package jetbrains.buildserver.metarunner.agent

import jetbrains.buildServer.util.EventDispatcher
import jetbrains.buildServer.serverSide.{AgentDistributionMonitor, BuildServerAdapter, BuildServerListener}
import jetbrains.buildserver.metarunner.{MetaRunnerSpecsLoader, MetaPaths}
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:41 
 */

class AgentPluginRegistrar(private val packer: AgentPluginPacker,
                           private val dispatcher: EventDispatcher[BuildServerListener],
                           private val publisher: AgentDistributionMonitor,
                           private val pluginsLocator : MetaRunnerSpecsLoader,
                           paths : MetaPaths) {
  val agentPluginZip = paths.getAgentPluginDest()

  dispatcher.addListener(new BuildServerAdapter() {
    override def pluginsLoaded = {

      packer.packPlugin(agentPluginZip, pluginsLocator.loadMetaRunners)
      publisher.registerAgentPlugin(agentPluginZip)

      dispatcher.removeListener(this)
    }
  })
}