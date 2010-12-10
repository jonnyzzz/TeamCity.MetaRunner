package jetbrains.buildserver.metarunner

import java.io.File

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 13:26 
 */

trait PluginPaths{
  def getMetaDefsPath() : File
  def getAgentLibs() : File
  def getAgentPluginDest() : File
}