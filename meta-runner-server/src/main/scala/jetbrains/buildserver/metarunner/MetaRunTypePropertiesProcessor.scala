package jetbrains.buildserver.metarunner

import xml.RunnerSpec
import java.lang.String
import scala.collection.JavaConversions._
import jetbrains.buildServer.serverSide.{InvalidProperty, PropertiesProcessor}
import java.util.Map

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:56 
 */

class MetaRunTypePropertiesProcessor(spec : RunnerSpec) extends PropertiesProcessor {

  def process(p1: Map[String, String]) = List()
}