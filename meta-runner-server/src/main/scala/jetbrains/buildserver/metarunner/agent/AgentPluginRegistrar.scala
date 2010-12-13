package jetbrains.buildserver.metarunner.agent

import jetbrains.buildServer.serverSide.{AgentDistributionMonitor, BuildServerAdapter, BuildServerListener}
import jetbrains.buildserver.metarunner.{MetaRunnerSpecsLoader}
import scala.collection.JavaConversions._
import jetbrains.buildserver.metarunner.specs.SpecsWatcher
import java.io.File
import jetbrains.buildServer.util.{EventDispatcher}
import com.intellij.openapi.util.io.FileUtil


/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:41 
 */

class AgentPluginRegistrar(private val packer: AgentPluginPacker,
                           private val dispatcher: EventDispatcher[BuildServerListener],
                           private val publisher: AgentDistributionMonitor,
                           private val pluginsLocator : MetaRunnerSpecsLoader,
                           private val paths : AgentPluginFileHolder,
                                   val watcher : SpecsWatcher) {
  val agentPluginZip = paths.getAgentPluginDest()

  def packAgentPlugin = {
    this.synchronized{
      val tempFile = new File(agentPluginZip.getPath + ".tmp")
      packer.packPlugin(tempFile, pluginsLocator.loadMetaRunners)

      FileUtil.delete(agentPluginZip)
      FileUtil.rename(tempFile, agentPluginZip)
    }
  }

  dispatcher.addListener(new BuildServerAdapter() {
    override def pluginsLoaded = {
      packAgentPlugin

      publisher.registerAgentPlugin(agentPluginZip)

      dispatcher.removeListener(this)

      watcher.addFilesChangedListener(new Runnable(){
        def run = packAgentPlugin
      })
    }
  })
}