package jetbrains.buildserver.metarunner

import java.io.File
import jetbrains.buildServer.plugins.bean.PluginInfo
import org.jetbrains.annotations.NotNull

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 15:21 
 */

class PluginPathsImpl(private val description : PluginInfo) extends PluginPaths {
  private def getRoot() = description.getPluginRoot()

  @NotNull
  def getMetaDefsPath() : File = new File(getRoot(), Constants.SpecFolder)

  @NotNull
  def getAgentLibs() = new File(getRoot(), "agent-libs")

  @NotNull
  def getAgentPluginDest() = new File(getRoot(), "tmp/meta-runner-agent.zip")
}