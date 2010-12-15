package jetbrains.buildserver.metarunner

import jetbrains.buildServer.parameters.ReferencesResolverUtil.ReferencesResolverListener
import jetbrains.buildServer.parameters.ReferencesResolverUtil
import jetbrains.buildserver.metarunner.util.BuildParametersUtil._
import java.util.Map
import collection.mutable.SetBuilder

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 23:46 
 */

object MetaReferenceResolver {
  def apply(text : String, parameters : Map[String, String]) : String = {
    resolve(text, parameters)._1
  }

  def resolve(text : String, parameters : Map[String, String]) : (String, Set[String]) = {
    val buffer = new StringBuilder()
    val refs = collection.mutable.Set[String]()
    ReferencesResolverUtil.resolve(text, new ReferencesResolverListener(){
      def appendReference(referenceKey: String) = {
        referenceKey match {
          case META_PREFIX(x) => {
            val resolved = parameters.get(x)
            if (resolved != null) {
              buffer.append(resolved)
              refs += x
              true
            } else {
              false
            }
          }
          case _ => false
        }
      }

      def appendText(text: String) = {
        buffer.append(text)
      }
    })
    (buffer.toString(), refs.toSet)
  }
}