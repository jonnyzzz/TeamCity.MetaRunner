package jetbrains.buildserver.metarunner

import jetbrains.buildServer.util.EventDispatcher
import jetbrains.buildServer.ExtensionHolder
import jetbrains.buildServer.agent.{AgentBuildRunner, BuildProcessFacade, AgentLifeCycleAdapter, AgentLifeCycleListener}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:57 
 */

class MetaRunnersRegistrar(val events : EventDispatcher[AgentLifeCycleListener],
                           val locator : MetaRunnerSpecsLoader,
                           val extensions : ExtensionHolder,
                           val facade : BuildProcessFacade) {
  events.addListener(new AgentLifeCycleAdapter{
    override def pluginsLoaded = {

      for(x <- locator.loadMetaRunners) {
        val runner = new MetaRunnerBuildServiceFactory(x,facade)
        extensions.registerExtension(classOf[AgentBuildRunner], "meta-runner@" + runner.getType, runner)
      }

      events.removeListener(this)
    }
  })
}