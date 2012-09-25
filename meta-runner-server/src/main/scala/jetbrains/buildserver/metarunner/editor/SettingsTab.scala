package jetbrains.buildserver.metarunner.editor

import jetbrains.buildServer.web.openapi._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 14.12.10 12:18 
 */

class SettingsTab(pagePlaces : PagePlaces,
                  descriptor : PluginDescriptor)
        extends SimpleCustomTab(
          pagePlaces,
          PlaceId.ADMIN_SERVER_CONFIGURATION_TAB,
          descriptor.getPluginName + "_server_admin",
          descriptor.getPluginResourcesPath("admin.html"),
          "Meta Runners"
        ) {
  addCssFile(descriptor.getPluginResourcesPath("css/admin.css"))
  register()
}