package jetbrains.buildserver.metarunner.xml

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */

trait RunnerSpec {
  def parameterDef : List[ParameterDef];
  def runners : List[StepDef]
}


class StepDef(runnerType : String, resources : RunnerResources, parameters : List[RunnerParmeter])

class RunnerResources(relativePath : String)

abstract class RunnerParmeter(key : String, scope : ParameterScope)
case class RunnerParameterRef(key : String, scope : ParameterScope, refName : String) extends RunnerParmeter(key = key, scope = scope)
case class RunnerParameterVakue(key: String, scope :ParameterScope, value : String) extends RunnerParmeter(key = key, scope = scope)


class ParameterDef(val key: String,
                   val parameterType: ParameterType,
                   val scope: ParameterScope,
                   val default: String,
                   val shortName: String,
                   val description: String
                  ) {
  override def toString : String = {
    "ParameterDef[ key=" + key + ", scope=" + scope +", type=" + parameterType + " ]"
  }
}


abstract class ParameterScope
case object RunnerScope extends ParameterScope
case object ConfigScope extends ParameterScope
case object SystemScope extends ParameterScope
case object EnvScope extends ParameterScope

class ChooserItem(value : String, description : String, isDefault : Boolean)

