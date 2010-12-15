package jetbrains.buildserver.metarunner

import scala.collection.JavaConversions._
import xml.{RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:59 
 */

class MetaRunTypeDefaultPropertiesImpl(spec: RunnerSpec) extends MetaRunTypeDefaultProperties {
  def getDefaultRunnerProperties = spec
          .parameterDefs
          .filter(_.defaultValue != null)
          .map(x => (x.key -> x.defaultValue))
          .toMap
}

