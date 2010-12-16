package jetbrains.buildserver.metarunner.usages

import jetbrains.buildserver.metarunner.MetaRunnerSpecsLoader
import collection.JavaConversions._
import jetbrains.buildserver.metarunner.xml.RunnerSpec

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

class MetaRunnerUsagesImpl(providers: java.util.Collection[MetaRunnerUsagesProvider],
                           specs: MetaRunnerSpecsLoader)
        extends MetaRunnerUsages {

  def resolveRunnerSpecReferences(spec: RunnerSpec) = providers.flatMap(_.resolveReferences(spec)).toList

  def resolveReferences = specs.loadMetaRunners.map(spec => (spec -> resolveRunnerSpecReferences(spec))).toMap
}


