package jetbrains.buildserver.metarunner

import java.util.zip.{ZipEntry, ZipOutputStream}
import jetbrains.buildServer.util.{FileUtil}
import java.io.{Closeable, FileOutputStream, FileInputStream, File}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 10.12.10 12:57 
 */

class AgentPluginPacker(private val paths: PluginPaths) {

  def packPlugin() : File = {
    val basePath = "meta-runner/"

    val file: File = paths.getAgentPluginDest()
    file.getParentFile.mkdirs()
    using(new ZipOutputStream(new FileOutputStream(file)))(
      zip => {
        zipDirectory(zip, basePath, paths.getAgentLibs())
        zipDirectory(zip, basePath + Constants.SpecFolder + "/", paths.getMetaDefsPath())
      }
    )
    file
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