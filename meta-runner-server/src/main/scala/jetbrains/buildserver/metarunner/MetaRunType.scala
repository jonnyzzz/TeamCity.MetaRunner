package jetbrains.buildserver.metarunner

import scala.collection.JavaConversions._
import xml.{RunnerSpec}
import jetbrains.buildServer.serverSide.{PropertiesProcessor, RunType}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:59 
 */

class MetaRunType(spec : RunnerSpec,
                  ui : MetaRunTypeUI,
                  defaults : MetaRunTypeDefaultProperties,
                  pp : PropertiesProcessor)
        extends RunType {

  def getDescription = spec.description

  def getDisplayName = spec.shortName

  def getType = spec.runType

  def getDefaultRunnerProperties = defaults.getDefaultRunnerProperties

  def getViewRunnerParamsJspFilePath = ui.getViewRunnerParamsJspFilePath

  def getEditRunnerParamsJspFilePath = ui.getEditRunnerParamsJspFilePath

  def getRunnerPropertiesProcessor = pp
}