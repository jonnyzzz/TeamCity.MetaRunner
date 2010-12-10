package jetbrains.buildserver.metarunner

import java.lang.String
import ui.{ParameterDefBean, RunnerSpecBean}
import java.util.{TreeMap, Collections, Map}
import jetbrains.buildServer.serverSide.{InvalidProperty, PropertiesProcessor, RunType}
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConversions._
import xml.{ParameterDef, RunnerSpec}

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
      if (x.defaultValue != null) {
        map.put(x.key, x.defaultValue)
      }
    }
    map
  }

  private def registerController(path: String, name : String) = {
    val jsp = descriptor.getPluginResourcesPath(path)
    val fullName = descriptor.getPluginResourcesPath(getType + "-" + name)
    webController.registerController(fullName,
    new BaseController{
      def doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView = {
        val model = new java.util.HashMap[String, Object]
        model.put("runner", new RunnerSpecBean {
          override def getParameterDefs = spec.parameterDefs.map(toParameterBean)
        })
        new ModelAndView(jsp, model)
      }
    })
    fullName
  }

  private def toParameterBean(p: ParameterDef): ParameterDefBean = {
    new ParameterDefBean {
      def getDescription  = p.description
      def getShortName    = p.shortName
      def getKey          = p.key
    }
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