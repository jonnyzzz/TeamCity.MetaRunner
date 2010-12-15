package jetbrains.buildserver.metarunner

import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.web.servlet.ModelAndView
import xml.{RunnerSpec, ParameterDef}
import scala.collection.JavaConversions._
import org.jetbrains.annotations.NotNull

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:26 
 */


class MetaRunTypeUIImpl(spec: RunnerSpec,
                        descriptor: PluginDescriptor,
                        webController: WebControllerManager)
        extends MetaRunTypeUI {

  private def registerController(path: String, name: String) = {
    val jsp = descriptor.getPluginResourcesPath(path)
    val fullName = descriptor.getPluginResourcesPath(spec.runType + "-" + name)
    webController.registerController(fullName,
      new BaseController() {
        def doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView = {
          val model = Map[String, Any]("runner" -> new RunnerSpecBean() {
            @NotNull
            def getParameterDefs = spec.parameterDefs.map(toParameterBean)
          })
          new ModelAndView(jsp, model)
        }
      })
    fullName
  }

  private def toParameterBean(p: ParameterDef): ParameterDefBean = {
    new ParameterDefBean() {
      @NotNull
      val getDescription = p.description

      @NotNull
      val getShortName = p.shortName

      @NotNull
      val getKey = p.key
    }
  }

  val getViewRunnerParamsJspFilePath = registerController("view-meta-runner.jsp", "view.html")

  val getEditRunnerParamsJspFilePath = registerController("edit-meta-runner.jsp", "edit.html")
}