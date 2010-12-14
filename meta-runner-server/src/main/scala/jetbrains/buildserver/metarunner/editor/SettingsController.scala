package jetbrains.buildserver.metarunner.editor

import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.serverSide.auth.{Permission, AuthorityHolder}
import jetbrains.buildserver.metarunner.UpdatableRunnerSpecs
import collection.JavaConversions._
import org.jetbrains.annotations.NotNull
import jetbrains.buildServer.serverSide.RunTypeRegistry
import jetbrains.buildServer.controllers.{RequestPermissionsChecker, AuthorizationInterceptor, BaseController}
import jetbrains.buildserver.metarunner.xml.{RunnerSpec, RunnerStepSpec}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 14.12.10 12:22 
 */

class SettingsController(webControllerManager: WebControllerManager,
                         descriptor: PluginDescriptor,
                         auth: AuthorizationInterceptor,
                         specs: UpdatableRunnerSpecs,
                         registry: RunTypeRegistry
                                )
        extends BaseController {
  val path = descriptor.getPluginResourcesPath("admin.html")

  webControllerManager.registerController(path, this)
  auth.addPathBasedPermissionsChecker(path, new RequestPermissionsChecker() {

    def checkPermissions(p: AuthorityHolder, p2: HttpServletRequest) = {
      p.isPermissionGrantedGlobally(Permission.CHANGE_SERVER_SETTINGS) &&
              p.isPermissionGrantedForAnyProject(Permission.EDIT_PROJECT) &&
              p.isPermissionGrantedForAnyProject(Permission.CUSTOMIZE_BUILD_PARAMETERS)
    }
  })

  def doHandle(p1: HttpServletRequest, p2: HttpServletResponse) = {
    val model = Map[String, Any](
      "specs" -> new SpecsBean() {
        def getSize = getRunnerSpecs.size

        @NotNull
        def getRunnerSpecs = specs.loadMetaRunners.map((x: RunnerSpec) => new RunnerSpecBean() {
          @NotNull
          def getDescription = x.description

          @NotNull
          def getShortName = x.shortName

          @NotNull
          def getRunType = x.runType

          @NotNull
          def getBasedOnRunners = {
            x.runners.map((x: RunnerStepSpec) => x.runType).toSet.map((runType: String) =>
              registry.findRunType(runType) match {
                case null => runType
                case y => y.getDisplayName
              }
            )
          }
        })
      },
      "descr" -> descriptor)
    new ModelAndView(descriptor.getPluginResourcesPath("admin.jsp"), model)
  }
}