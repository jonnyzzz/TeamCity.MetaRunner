package jetbrains.buildserver.metarunner

import java.io.File
import com.intellij.openapi.diagnostic.Logger
import xml.{MetaRunnerSpecParser, RunnerSpec}
import jetbrains.buildServer.serverSide.ServerPaths

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:10 
 */

class ServerSpecLocator(private val parser: MetaRunnerSpecParser,
                        private val paths: MetaRunnerSpecsPaths)
        extends MetaRunnerSpecsLoader {
  val LOG = Logger.getInstance(getClass.getName())

  private def loadPluginFromFolder(path: File) = {
    LOG.info("Loading meta-runner specs from: " + path)

    val root = path.listFiles;
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

  def loadMetaRunners: List[RunnerSpec] = {
    val homes = paths.getUserPluginsPath :: paths.getBundledPluginsPath :: (Nil : List[File])
    homes.map(loadPluginFromFolder).foldLeft(Nil : List[RunnerSpec])((list, rr) =>(list ::: rr))
  }
}

