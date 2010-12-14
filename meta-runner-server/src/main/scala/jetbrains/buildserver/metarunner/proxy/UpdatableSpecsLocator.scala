package jetbrains.buildserver.metarunner.proxy

import collection.JavaConversions._
import jetbrains.buildserver.metarunner.{UpdatableRunnerSpecs, MetaRunnerSpecsLoader}
import jetbrains.buildserver.metarunner.xml.RunnerSpec
import java.util.concurrent._
import jetbrains.buildServer.util._
import collection.mutable.{HashMap, HashSet, ListBuffer}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 23:03 
 */

class UpdatableSpecsLocator(private val loader: MetaRunnerSpecsLoader)
        extends MetaRunnerSpecsLoader
        with UpdatableRunnerSpecs {
  private val myRunners = new HashMap[String, ProxifiedRunnerSpecs]

  def reloadRenners() = {
    this.synchronized{
      val found = HashSet[String]()
      loader.loadMetaRunners.foreach(newSpec => {
        found += newSpec.runType

        myRunners.get(newSpec.runType) match {
          case None => {
            val spec = new ProxifiedRunnerSpecs(newSpec)
            myRunners.put(newSpec.runType, spec)
            onAdd(spec)
          }
          case Some(y) => {
            y.setProxy(newSpec)
            onChange(y)
          }
        }
      })

      val keysToRemove = myRunners.filterKeys(!found.contains(_)).keys.toList
      keysToRemove.foreach(x => {
        onDelete(myRunners(x))
        myRunners.remove(x)
      })
    }
    onAllChanged(loadMetaRunners)
  }

  def loadMetaRunners = this.synchronized{myRunners.values.toList}

  private val onAdd = new ExtensionHolder[RunnerSpec]
  private val onChange = new ExtensionHolder[RunnerSpec]
  private val onDelete = new ExtensionHolder[RunnerSpec]
  private val onAllChanged = new ExtensionHolder[List[RunnerSpec]]


  def onRunnerSpecRemoved(action: Action[RunnerSpec]) = onDelete.addHandler(action)

  def onRunnerSpecChanged(action: Action[RunnerSpec]) = onChange.addHandler(action)

  def onRunnerSpecAdded(action: Action[RunnerSpec]) = onAdd.addHandler(action)

  def onRunnersChanged(action : Action[List[RunnerSpec]]) = onAllChanged.addHandler(action)

  private class ExtensionHolder[T] {
    private val handlers = new CopyOnWriteArrayList[Action[T]]()

    def addHandler(h: Action[T]) = {
      handlers.add(h)
      new Disposable {
        def dispose = {
          handlers.remove(h)
        }
      }
    }

    def apply(h: T) = handlers.foreach(_.apply(h))
  }
}