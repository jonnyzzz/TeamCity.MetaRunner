package jetbrains.buildserver.metarunner

import java.io.File
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.plugins.bean.PluginInfo

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:26 
 */

class PluginPaths(private val description : PluginDescriptor) {
  private def getRoot() = description.asInstanceOf[PluginInfo].getPluginRoot()

  def getMetaDefsPath() : File = new File(getRoot(), Constants.SpecFolder)

  def getAgentLibs() = new File(getRoot(), "agent-libs")

  def getAgentPluginDest() = new File(getRoot(), "tmp/agent-plugin.zip")
}