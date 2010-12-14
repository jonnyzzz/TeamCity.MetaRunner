package jetbrains.buildserver.metarunner.agent

import scala.collection.JavaConversions._
import java.io.File
import com.intellij.openapi.util.io.FileUtil
import jetbrains.buildserver.metarunner.xml.RunnerSpec
import jetbrains.buildserver.metarunner.proxy.UpdatableSpecsLocator
import jetbrains.buildServer.util.{EventDispatcher, Action}
import jetbrains.buildServer.serverSide.{BuildServerListener, BuildServerAdapter, AgentDistributionMonitor}


/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:41 
 */

class AgentPluginRegistrar(private val packer: AgentPluginPacker,
                           private val publisher: AgentDistributionMonitor,
                           private val paths : AgentPluginFileHolder,
                           private val dispatcher : EventDispatcher[BuildServerListener],
                                   val watcher : UpdatableSpecsLocator) {
  val agentPluginZip = paths.getAgentPluginDest()

  def packAgentPlugin(runners : List[RunnerSpec]) = {
    this.synchronized{
      val tempFile = new File(agentPluginZip.getPath + ".tmp")
      packer.packPlugin(tempFile, runners)

      FileUtil.delete(agentPluginZip)
      FileUtil.rename(tempFile, agentPluginZip)
    }
  }

  val action = new Action[List[RunnerSpec]]() {
    def apply(p: List[RunnerSpec]) = packAgentPlugin(p)
  }

  watcher.onRunnersChanged(action)

  dispatcher.addListener(new BuildServerAdapter() {
    override def serverStartup = {
      watcher.reloadRenners
      packAgentPlugin(watcher.loadMetaRunners)

      publisher.registerAgentPlugin(agentPluginZip)
      dispatcher.removeListener(this)
    }
  })
}