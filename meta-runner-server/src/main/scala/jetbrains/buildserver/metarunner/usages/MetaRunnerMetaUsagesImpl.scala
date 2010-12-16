package jetbrains.buildserver.metarunner.usages

import jetbrains.buildserver.metarunner.MetaRunnerSpecsLoader
import jetbrains.buildserver.metarunner.xml.RunnerSpec
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

class MetaRunnerMetaUsagesImpl(specs : MetaRunnerSpecsLoader)
        extends MetaRunnerUsagesProvider {

  def resolveReferences(spec : RunnerSpec) = {
    specs.loadMetaRunners.flatMap(anotherRunner => {
      if (anotherRunner.runners.filter(x=>x.runType.equals(spec.runType)).nonEmpty) {
        Some(ReferenceFromMetaRunner(anotherRunner))
      } else {
        None
      }
    }).toList
  }
}









