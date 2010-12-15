package jetbrains.buildserver.metarunner

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:26 
 */

trait MetaRunTypeUI {
  def getViewRunnerParamsJspFilePath: String

  def getEditRunnerParamsJspFilePath: String
}

