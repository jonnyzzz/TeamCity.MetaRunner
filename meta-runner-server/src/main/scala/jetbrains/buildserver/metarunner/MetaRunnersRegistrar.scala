package jetbrains.buildserver.metarunner

import jetbrains.buildServer.util.EventDispatcher
import jetbrains.buildServer.serverSide.{BuildServerAdapter, BuildServerListener, RunTypeRegistry}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 09.12.10 0:06 
 */

class MetaRunnersRegistrar(val loader: MetaRunnerSpecsLoader,
                           val registry : RunTypeRegistry,
                           val disp : EventDispatcher[BuildServerListener]) {
  disp.addListener(new BuildServerAdapter{
    override def pluginsLoaded = {

      for(r <- loader.loadMetaRunners) {
        registry.registerRunType(new MetaRunType(r))
      }

      disp.removeListener(this)
    }
  })
}