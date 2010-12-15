package jetbrains.buildserver.metarunner

import java.lang.String

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:59 
 */

trait MetaRunTypeDefaultProperties {
  def getDefaultRunnerProperties : Map[String, String]
}



