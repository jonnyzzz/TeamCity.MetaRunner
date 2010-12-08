package jetbrains.buildserver.metarunner

import java.lang.String
import xml.RunnerSpec
import java.util.{TreeMap, Collections, Map}
import collection.immutable.HashMap
import jetbrains.buildServer.serverSide.{InvalidProperty, PropertiesProcessor, RunType}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:59 
 */

class MetaRunType(val spec : RunnerSpec) extends RunType {
  def getDescription = spec.description

  def getDisplayName = spec.shortName

  def getType = spec.runType

  def getDefaultRunnerProperties = {
    val map = new TreeMap[String, String]
    for(x <- spec.parameterDefs) {
      if (x.default != null) {
        map.put(x.key, x.default)
      }
    }
    map
  }

  def getViewRunnerParamsJspFilePath = ""

  def getEditRunnerParamsJspFilePath = ""

  def getRunnerPropertiesProcessor = {
    new PropertiesProcessor{
      //TODO: call refered RunType property processors in this processor
      //TODO: to check if the runner could be started in principle
      def process(p1: Map[String, String]) = Collections.emptyList[InvalidProperty]
    }
  }
}