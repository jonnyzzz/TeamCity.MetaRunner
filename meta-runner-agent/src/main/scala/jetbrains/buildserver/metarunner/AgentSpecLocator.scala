package jetbrains.buildserver.metarunner

import impl.MetaRunnerSpecLoaderBase
import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import java.io.File
import xml.{MetaRunnerSpecParser, RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:47 
 */

class AgentSpecLocator(private val description : PluginDescriptor,
                       private val parser : MetaRunnerSpecParser)
        extends MetaRunnerSpecLoaderBase(parser)
        with MetaRunnerSpecsLoader {
  def loadMetaRunners : List[RunnerSpec] = {
    val root = new File(description.getPluginRoot, MetaRunnerConstants.SpecFolder);
    loadPluginFromFolder(root)
  }
}