package jetbrains.buildserver.metarunner

import xml.RunnerSpec
import java.lang.String
import scala.collection.JavaConversions._
import java.util.Map
import jetbrains.buildServer.serverSide.{RunTypeRegistry, InvalidProperty, PropertiesProcessor}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:56 
 */

class MetaRunTypePropertiesProcessor(spec : RunnerSpec, registry : RunTypeRegistry ) extends PropertiesProcessor {

  def process(p1: Map[String, String]) = {
    List[InvalidProperty]()
  }
}