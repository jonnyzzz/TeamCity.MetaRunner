package jetbrains.buildserver.metarunner

import agent.{AgentPluginFileHolder, AgentPluginLibrariesLocator}
import java.io.File
import jetbrains.buildServer.plugins.bean.PluginInfo
import org.jetbrains.annotations.NotNull
import jetbrains.buildServer.serverSide.ServerPaths
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 15:21 
 */

class PluginPathsImpl(info : PluginInfo, paths : ServerPaths)
        extends MetaRunnerSpecsPaths
        with AgentPluginLibrariesLocator
        with AgentPluginFileHolder
{
  private val pluginRoot = info.getPluginRoot()
  private val usersRoot = new File(paths.getConfigDir(), "__meta_runners__")

  @NotNull
  def getBundledPluginsPath() = new File(pluginRoot, MetaRunnerConstants.SpecFolder)

  @NotNull
  def getAgentLibs() = new File(pluginRoot, "agent-libs")

  @NotNull
  def getAgentPluginDest() = new File(pluginRoot, "tmp/meta-runner-agent.zip")

  @NotNull
  def getUserPluginsPath = {
    usersRoot.mkdirs()
    usersRoot
  }

  def getSpecRoots = getBundledPluginsPath :: getUserPluginsPath :: Nil
}