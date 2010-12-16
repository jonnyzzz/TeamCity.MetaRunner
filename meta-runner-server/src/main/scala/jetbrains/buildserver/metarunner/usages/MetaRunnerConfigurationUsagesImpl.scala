package jetbrains.buildserver.metarunner.usages

import jetbrains.buildserver.metarunner.xml.{RunnerSpec}
import jetbrains.buildServer.serverSide.ProjectManager
import collection.JavaConversions._

/**
 * @aut\hor Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 14:08 
 */

class MetaRunnerConfigurationUsagesImpl(projectManager : ProjectManager) extends MetaRunnerUsagesProvider {

  def resolveReferences(spec : RunnerSpec) = {
    val buildTypes = projectManager.getProjects.flatMap(_.getBuildTypes)
    buildTypes.flatMap(bt => {
      if (bt.getRunnerTypes.contains(spec.runType)) {
        Some(ReferenceFromConfiguration(bt))
      } else {
        None
      }
    }).toList
  }
}









