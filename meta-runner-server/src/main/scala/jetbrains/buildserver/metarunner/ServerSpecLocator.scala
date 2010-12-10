package jetbrains.buildserver.metarunner

import java.io.File
import com.intellij.openapi.diagnostic.Logger
import xml.{MetaRunnerSpecParser, RunnerSpec}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.plugins.bean.PluginInfo

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:10 
 */

class ServerSpecLocator(private val parser: MetaRunnerSpecParser,
                        private val paths: MetaPaths)
        extends MetaRunnerSpecsLoader {
  val LOG = Logger.getInstance(getClass.getName())

  def loadMetaRunners: List[RunnerSpec] = {
    //TODO: Add filewatcher
    //TODO: Use plugin data folder too
    val root = paths.getMetaDefsPath().listFiles;

    if (root == null)
      Nil
    else {
      root.foldLeft(Nil: List[RunnerSpec])((list, dir) => {
        val desc = new File(dir, "meta-runner.xml")
        LOG.info("Loading: " + desc)
        try {
          parser.parse(desc) :: list
        } catch {
          case x: Throwable => {
            LOG.warn("Failed to parse meta-runner. " + x.getMessage, x)
            list
          }
        }
      })
    }
  }
}

