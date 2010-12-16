package jetbrains.buildserver.metarunner.usages

import jetbrains.buildServer.BuildType
/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

case class ReferenceFromConfiguration(bt : BuildType) extends Reference  {
  def getDisplayText = "Build Configuration: " + bt.getFullName

  def getBuildType() : BuildType = bt
}









