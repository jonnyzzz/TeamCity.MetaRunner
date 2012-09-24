package jetbrains.buildserver.metarunner

import jetbrains.buildServer.util.EventDispatcher
import jetbrains.buildServer.ExtensionHolder
import jetbrains.buildServer.agent.{AgentBuildRunner, BuildProcessFacade, AgentLifeCycleAdapter, AgentLifeCycleListener}
import jetbrains.buildServer.agent.impl.BuildRunnerRegistryEx

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:57 
 */

class MetaRunnersRegistrar(events : EventDispatcher[AgentLifeCycleListener],
                           locator : MetaRunnerSpecsLoader,
                           extensions : ExtensionHolder,
                           facade : BuildProcessFacade,
                           registry : BuildRunnerRegistryEx) {
  events.addListener(new AgentLifeCycleAdapter{
    override def pluginsLoaded() {

      for(x <- locator.loadMetaRunners) {
        val runner = new MetaRunnerBuildServiceFactory(x,facade, new MetaRunnerCanRunCalculatorImpl(x, registry))
        extensions.registerExtension(classOf[AgentBuildRunner], "meta-runner@" + runner.getType, runner)
      }

      events.removeListener(this)
    }
  })
}
