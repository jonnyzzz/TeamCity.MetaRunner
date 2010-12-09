package jetbrains.buildserver.metarunner.xml

import java.util.Collection
import collection.JavaConversions

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */


class ParameterDef(val key: String,
                   val parameterType: ParameterType,
                   val default: String,
                   val shortName: String,
                   val description: String
                  ) {
  override def toString : String = {
    "ParameterDef[ key=" + key + ", type=" + parameterType + " ]"
  }
}

abstract class ParameterScope
case object RunnerScope extends ParameterScope
case object BuildScope extends ParameterScope
