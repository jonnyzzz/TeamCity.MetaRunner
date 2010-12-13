package jetbrains.buildserver.metarunner.impl

import java.io.File
import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildserver.metarunner.xml.{MetaRunnerSpecParser, RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 20:38 
 */

class MetaRunnerSpecLoaderBase(private val parser : MetaRunnerSpecParser) {
  protected val LOG = Logger.getInstance(getClass.getName())

  protected  def loadPluginFromFolder(path: File) = {
    LOG.info("Loading meta-runner specs from: " + path)

    val root = path.listFiles;
    if (root == null)
      Nil
    else {
      root.foldLeft(Nil: List[RunnerSpec])((list, dir) => {
        val desc = new File(dir, "meta-runner.xml")
        LOG.info("Loading: " + desc)
        try {
          parser.parse(desc) :: list
        } catch {
          case x: Throwable => {
            LOG.warn("Failed to parse meta-runner. " + x.getMessage, x)
            list
          }
        }
      })
    }
  }

}