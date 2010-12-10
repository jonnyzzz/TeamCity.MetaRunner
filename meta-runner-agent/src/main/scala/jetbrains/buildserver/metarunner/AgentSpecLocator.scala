package jetbrains.buildserver.metarunner

import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import java.io.File
import com.intellij.openapi.diagnostic.Logger
import xml.{MetaRunnerSpecParser, RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:47 
 */

class AgentSpecLocator(val description : PluginDescriptor,
                       val parser : MetaRunnerSpecParser) extends MetaRunnerSpecsLoader {
  val LOG = Logger.getInstance(getClass.getName())

  def loadMetaRunners : List[RunnerSpec] = {
    val root = new File(description.getPluginRoot, MetaRunnerConstants.SpecFolder).listFiles;

    if (root == null)
      Nil
    else {
      root.foldLeft(Nil : List[RunnerSpec])((list, dir) => {
        val desc = new File(dir, "meta-runner.xml")
        LOG.info("Loading: " + desc)
        try {
          parser.parse(desc) :: list
        } catch {
          case x : Throwable => {
            LOG.warn("Failed to parse meta-runner. " + x.getMessage, x)
            list
          }
        }
      })
    }
  }
}