package jetbrains.buildserver.metarunner.impl

import java.io.File
import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildserver.metarunner.xml.{MetaRunnerSpecParser, RunnerSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 20:38 
 */

class MetaRunnerSpecLoaderBase(private val parser : MetaRunnerSpecParser) {
  private val XML_NAME = "meta-runner.xml"

  protected val LOG = Logger.getInstance(getClass.getName())

  protected  def loadPluginFromFolder(path: File) = {
    LOG.info("Loading meta-runner specs from: " + path)

    val root = path.listFiles;
    if (root == null)
      Nil
    else {
      root.foldLeft(Nil: List[RunnerSpec])((list, dir) => {
        val desc = new File(dir, XML_NAME)
        if (desc.exists) {
          LOG.info("Loading meta-runner spec from: " + desc)
          try {
            parser.parse(desc) :: list
          } catch {
            case x: Throwable => {
              LOG.warn("Failed to parse meta-runner. " + x.getMessage, x)
              list
            }
          }
        } else {
          LOG.info("Failed to load meta-runner spec from: " + desc)
          list
        }
      })
    }
  }

}