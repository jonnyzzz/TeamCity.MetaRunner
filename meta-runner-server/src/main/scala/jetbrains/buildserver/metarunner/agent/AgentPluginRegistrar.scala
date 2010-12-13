package jetbrains.buildserver.metarunner.agent

import jetbrains.buildServer.util.EventDispatcher
import jetbrains.buildServer.serverSide.{AgentDistributionMonitor, BuildServerAdapter, BuildServerListener}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:41 
 */

class AgentPluginRegistrar(private val packer: AgentPluginPacker,
                           private val dispatcher: EventDispatcher[BuildServerListener],
                           private val publisher: AgentDistributionMonitor) {
  dispatcher.addListener(new BuildServerAdapter() {
    override def pluginsLoaded = {
      publisher.registerAgentPlugin(packer.packPlugin())

      dispatcher.removeListener(this)
    }
  })
}