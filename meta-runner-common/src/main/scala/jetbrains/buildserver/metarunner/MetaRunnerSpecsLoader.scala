package jetbrains.buildserver.metarunner

import xml.RunnerSpec

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:46 
 */

trait MetaRunnerSpecsLoader {
  def loadMetaRunners : List[RunnerSpec]
}