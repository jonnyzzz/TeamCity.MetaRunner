package jetbrains.buildserver.metarunner

import impl.MetaRunnerSpecLoaderBase
import xml.{MetaRunnerSpecParser, RunnerSpec}
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:10 
 */

class ServerSpecLocator(private val parser: MetaRunnerSpecParser,
                        private val paths: MetaRunnerSpecsPaths)
        extends MetaRunnerSpecLoaderBase(parser)
        with MetaRunnerSpecsLoader {

  def loadMetaRunners: List[RunnerSpec] = {
    paths.getSpecRoots.map(loadPluginFromFolder).flatten(identity).toList
  }
}

