package jetbrains.buildserver.metarunner

import java.lang.String
import xml.RunnerSpec
import java.util.{TreeMap, Collections, Map}
import jetbrains.buildServer.serverSide.{InvalidProperty, PropertiesProcessor, RunType}
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConversions._
/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 23:59 
 */

class MetaRunType(val spec : RunnerSpec,
                  val descriptor : PluginDescriptor,
                  val webController : WebControllerManager) extends RunType {
  def getDescription = spec.description

  def getDisplayName = spec.shortName

  def getType = spec.runType

  def getDefaultRunnerProperties = {
    val map = new TreeMap[String, String]
    for(x <- spec.parameterDefs) {
      if (x.default != null) {
        map.put(x.key, x.default)
      }
    }
    map
  }

  private def registerController(path: String, name : String) = {
    val jsp = descriptor.getPluginResourcesPath(path)
    val fullName = descriptor.getPluginResourcesPath(getType + "-" + name)
    webController.registerController(fullName,
    new BaseController{
      def doHandle(p1: HttpServletRequest, p2: HttpServletResponse) = new ModelAndView(jsp)
    })
    fullName
  }


  val getViewRunnerParamsJspFilePath = registerController("view-meta-runner.jsp", "view.html")

  val getEditRunnerParamsJspFilePath = registerController("edit-meta-runner.jsp", "edit.html")

  def getRunnerPropertiesProcessor = {
    new PropertiesProcessor{
      //TODO: call refered RunType property processors in this processor
      //TODO: to check if the runner could be started in principle
      def process(p1: Map[String, String]) = Collections.emptyList[InvalidProperty]
    }
  }
}