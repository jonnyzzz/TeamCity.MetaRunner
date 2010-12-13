package jetbrains.buildserver.metarunner.agent

import java.util.zip.{ZipEntry, ZipOutputStream}
import jetbrains.buildServer.util.{FileUtil}
import java.io.{Closeable, FileOutputStream, FileInputStream, File}
import jetbrains.buildserver.metarunner.{MetaRunnerConstants, MetaRunnerSpecsPaths}
import jetbrains.buildserver.metarunner.xml.RunnerSpec
import java.util.List
import scala.collection.JavaConversions._
import com.intellij.openapi.diagnostic.Logger

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 12:57 
 */

class AgentPluginPacker(private val paths: AgentPluginLibrariesLocator) {
  private val LOG = Logger.getInstance(getClass.getName)

  def packPlugin(destFile : File, runners : List[RunnerSpec]) = {
    LOG.info("Packing agent plugin")

    val basePath = "meta-runner/"

    destFile.getParentFile.mkdirs()
    using(new ZipOutputStream(new FileOutputStream(destFile)))(
      zip => {
        zipDirectory(zip, basePath + "lib/", paths.getAgentLibs())
        for(runner <- runners) {
          zipDirectory(zip, basePath + MetaRunnerConstants.SpecFolder + "/" + runner.runType + "/", runner.getMetaRunnerRoot())
        }
      }
    )
  }

  private def zipDirectory(zip: ZipOutputStream, prefix: String, root: File): Unit = {
    val files = root.listFiles()
    if (files != null) {
      for (x <- files) {
        if (x.isFile) {
          zip.putNextEntry(new ZipEntry(prefix + x.getName()));
          using(new FileInputStream(x))(fis => FileUtil.copyStreams(fis, zip));
          zip.closeEntry();
        } else {
          zipDirectory(zip, prefix + x.getName() + "/", x);
        }
      }
    }
  }

  private def using[X <: Closeable, A](resource: X)(f: X => A) = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }
}