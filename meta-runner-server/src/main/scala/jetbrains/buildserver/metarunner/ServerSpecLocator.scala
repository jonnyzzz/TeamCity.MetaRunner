package jetbrains.buildserver.metarunner

import impl.MetaRunnerSpecLoaderBase
import java.io.File
import xml.{MetaRunnerSpecParser, RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:10 
 */

class ServerSpecLocator(private val parser: MetaRunnerSpecParser,
                        private val paths: MetaRunnerSpecsPaths)
        extends MetaRunnerSpecLoaderBase(parser)
        with MetaRunnerSpecsLoader {

  def loadMetaRunners: List[RunnerSpec] = {
    val homes = paths.getUserPluginsPath :: paths.getBundledPluginsPath :: (Nil : List[File])
    homes.map(loadPluginFromFolder).foldLeft(Nil : List[RunnerSpec])((list, rr) =>(list ::: rr))
  }
}

