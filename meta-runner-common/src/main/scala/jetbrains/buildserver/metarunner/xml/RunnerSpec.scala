package jetbrains.buildserver.metarunner.xml

import java.util.Collection
import collection.JavaConversions
import org.jetbrains.annotations.NotNull

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */


trait RunnerStepParams {
  def key: String
  def scope :ParameterScope
  def value : String
}

class StepDef(val runType : String,
              val resources : RunnerResources,
              val parameterList : List[RunnerParameter]) extends RunnerStepSpec {
  @NotNull
  def parameters = JavaConversions.asJavaCollection(parameterList)
}

class RunnerResources(val relativePath : String)

class RunnerParameter(val key: String,
                      val scope :ParameterScope,
                      val value : String) extends RunnerStepParams





