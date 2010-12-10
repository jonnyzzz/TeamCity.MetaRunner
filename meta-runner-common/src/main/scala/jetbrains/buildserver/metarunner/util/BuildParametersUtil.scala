package jetbrains.buildserver.metarunner.util

import jetbrains.buildServer.agent.Constants

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 08.12.10 22:42 
 */

object BuildParametersUtil {

  // Extractor object matching first character.
  abstract class StartsWith(val pattern: String) {
    def unapply(variable: String) =
      if (variable != null && variable.startsWith(pattern)) {
        Some(variable.substring(pattern.length))
      } else {
        None
      }

    def apply(variable: String) = {
      pattern + variable
    }
  }

  object ENV_PREFIX extends StartsWith(Constants.ENV_PREFIX)
  object SYSTEM_PREFIX extends StartsWith(Constants.SYSTEM_PREFIX)
}