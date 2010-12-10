package jetbrains.buildserver.metarunner

import java.io.File
import jetbrains.buildServer.plugins.bean.PluginInfo

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 15:21 
 */

class PluginPathsImpl(private val description : PluginInfo) extends PluginPaths {
  private def getRoot() = description.getPluginRoot()

  def getMetaDefsPath() : File = new File(getRoot(), Constants.SpecFolder)

  def getAgentLibs() = new File(getRoot(), "agent-libs")

  def getAgentPluginDest() = new File(getRoot(), "tmp/agent-plugin.zip")
}