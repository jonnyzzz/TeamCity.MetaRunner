package jetbrains.buildserver.metarunner.usages

import jetbrains.buildserver.metarunner.xml.RunnerSpec
/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

case class ReferenceFromMetaRunner(spec : RunnerSpec) extends Reference {
  def getDisplayText = "Meta Runner: " + spec.shortName

  def getMetaRunnerName = spec.shortName
}
