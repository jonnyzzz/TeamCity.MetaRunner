package jetbrains.buildserver.metarunner

import jetbrains.buildServer.serverSide.{BuildServerAdapter, BuildServerListener, RunTypeRegistry}
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import jetbrains.buildServer.util.{Action, EventDispatcher}
import xml.RunnerSpec
import com.intellij.openapi.diagnostic.Logger

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 09.12.10 0:06 
 */

class MetaRunnersRegistrar(loader: UpdatableRunnerSpecs,
                           registry : RunTypeRegistry,
                           disp : EventDispatcher[BuildServerListener],
                           descriptor : PluginDescriptor,
                           webControllerManager : WebControllerManager) {
  private val LOG = Logger.getInstance(getClass.getName)

  private def addAction(spec : RunnerSpec) = {
    registry.registerRunType(new MetaRunType(spec, descriptor, webControllerManager))
  }

  private def removeAction(spec : RunnerSpec) = {
   LOG.warn("Meta-runner: " + spec.runType + " was removed. Runner remove does not supported. ")
  }

  disp.addListener(new BuildServerAdapter{
    override def pluginsLoaded = {

      for(r <- loader.loadMetaRunners) {
        addAction(r)
      }

      disp.removeListener(this)

      loader.onRunnerSpecAdded(new Action[RunnerSpec]{
        def apply(p: RunnerSpec) = addAction(p)
      })

      loader.onRunnerSpecRemoved(new Action[RunnerSpec] {
        def apply(p: RunnerSpec) = removeAction(p)
      })
    }
  })
}