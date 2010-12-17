package jetbrains.buildserver.metarunner.xml

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */

class ParameterDefImpl(val key: String,
                       val parameterType: ParameterType,
                       val defaultValue: String,
                       val shortName: String,
                       val description: String)
        extends ParameterDef {
  override def toString: String = {
    "ParameterDef[ key=" + key + ", type=" + parameterType + " ]"
  }
}

abstract class ParameterScope
case object RunnerScope extends ParameterScope
case object BuildScope extends ParameterScope
