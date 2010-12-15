package jetbrains.buildserver.metarunner

import java.lang.String
import jetbrains.buildServer.serverSide.{RunTypeRegistry, InvalidProperty, PropertiesProcessor}
import xml.{RunnerStepSpec, RunnerSpec}
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:56 
 */

class MetaRunTypePropertiesProcessor(spec: RunnerSpec, registry: RunTypeRegistry) extends PropertiesProcessor {

  private def resolve(run: RunnerStepSpec, map: java.util.Map[String, String]): java.util.Collection[InvalidProperty] = {
    registry.findRunType(run.runType) match {
      case null => List()
      case runType => runType.getRunnerPropertiesProcessor match {
        case null => List()
        case x => {
          val subs = run.parameters.map(x => (x.key -> MetaReferenceResolver.resolve(x.value, map))).toMap
          val actual = subs.mapValues(_._1)
          x.process(actual) match {
            case null => List()
            //TODO: find back references to actual parameters
            case props => props.flatMap(x=>{
              subs(x.getPropertyName)._2.map(k=>new InvalidProperty(k, "[" + runType.getDisplayName + "] " + x.getInvalidReason))
            })
          }
        }
      }
    }
  }

  def process(p1: java.util.Map[String, String]): java.util.Collection[InvalidProperty] = {
    spec.runners.flatMap(x => resolve(x, p1)).toList
  }
}