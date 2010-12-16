package jetbrains.buildserver.metarunner.usages

import jetbrains.buildserver.metarunner.xml.RunnerSpec

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

trait MetaRunnerUsages {
  def resolveReferences: Map[RunnerSpec, List[Reference]]

  def resolveRunnerSpecReferences(spec : RunnerSpec) : List[Reference]
}










