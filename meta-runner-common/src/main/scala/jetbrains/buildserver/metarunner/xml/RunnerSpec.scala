package jetbrains.buildserver.metarunner.xml

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */

trait RunnerSpec {
  def parameterDef : List[ParameterDef];
  def runners : List[RunnerStepSpec]
  def runType : String
}

trait RunnerStepSpec {
  def runType : String
  def parameters : List[RunnerStepParams]
}

trait RunnerStepParams {
  def key: String
  def scope :ParameterScope
  def value : String
}

class StepDef(val runType : String,
              val resources : RunnerResources,
              val parameters : List[RunnerParameter]) extends RunnerStepSpec

class RunnerResources(val relativePath : String)

class RunnerParameter(val key: String,
                      val scope :ParameterScope,
                      val value : String) extends RunnerStepParams


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

class ChooserItem(value : String, description : String, isDefault : Boolean)

