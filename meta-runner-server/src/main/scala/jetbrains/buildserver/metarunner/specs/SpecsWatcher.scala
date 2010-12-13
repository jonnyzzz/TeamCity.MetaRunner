package jetbrains.buildserver.metarunner.specs

import java.io.File
import jetbrains.buildServer.configuration.{ChangeListener, FilesWatcher}
import java.lang.String
import com.intellij.openapi.diagnostic.Logger
import java.util.concurrent.CopyOnWriteArrayList
import jetbrains.buildServer.util.Disposable
import scala.collection.JavaConversions._
import jetbrains.buildserver.metarunner.MetaRunnerSpecsPaths

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 20:46 
 */

class SpecsWatcher(private val specsRoots: MetaRunnerSpecsPaths) {
  private val LOG = Logger.getInstance(getClass().getName)
  private val listeners = new CopyOnWriteArrayList[Runnable]

  val watcher = new FilesWatcher(new FilesWatcher.WatchedFilesProvider() {
    def getWatchedFiles = {

      def collectFiles(root : File) : Set[File] = {
        if (root.isFile) {
          Set(root)
        } else {
          val files = root.listFiles()
          if (files == null || root.getName.startsWith("_"))
            Set()
          else
            (files map collectFiles).foldLeft(Set[File]()) (_++_)
        }
      }
      (specsRoots.getSpecRoots map collectFiles).foldLeft(Set[File]()) (_++_) toArray
    }
  })
  watcher.setSleepingPeriod(10000)
  def fireChanged: Unit = {
    listeners.foreach(_.run())
  }

  watcher.registerListener(new ChangeListener() {
    def changeOccured(p1: String) = {
      fireChanged
    }
  })

  def setTimeout(l : Long) = watcher.setSleepingPeriod(l)

  def addFilesChangedListener(handler : Runnable) : Disposable = {
    listeners.add(handler)
    new Disposable{
      def dispose = { listeners.remove(handler) }
    }
  }

  def start = {
    fireChanged
    watcher.start
  }

  def stop = watcher.stop
}