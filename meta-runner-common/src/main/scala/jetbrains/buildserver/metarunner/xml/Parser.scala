package jetbrains.buildserver.metarunner.xml

import java.io.File
import javax.xml.XMLConstants
import javax.xml.validation.SchemaFactory
import javax.xml.transform.stream.StreamSource
import org.xml.sax.{InputSource}
import xml.Elem

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:07 
 */

class Parser {
  def parse(spec: File) = {
    // A schema can be loaded in like ...

    val sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val s = sf.newSchema(new StreamSource(getClass.getResourceAsStream("/meta-runner-config.xsd")))

    val is : InputSource = new InputSource(spec.getPath)
    val xml = new SchemaAwareFactoryAdapter(s).load(is)

    val res = Nil

    def parseType(param : Elem) = {
      param match {
        case x @ <type-text>{_*}</type-text> => TextType(false)
        case x @ <type-hidden>{_*}</type-hidden> => HiddenType
        case x => throw new RuntimeException("Failed to parse: " + x)
      }
    }

    def parseScope(param : Elem) = {
      (param \ "@scope").text match {
        case "runner" => RunnerScope
        case "config" => ConfigScope
        case "env" => EnvScope
        case "system" => SystemScope
        case x => throw new RuntimeException("Failed to parse scope: " + x)
      }
    }

    val paramDefs = (xml \ "runner-parameters" \ "parameter").foldLeft(Nil:List[ParameterDef])((list, elem) => {
      new ParameterDef(
        (elem \ "@name").text,
        HiddenType,
        parseScope(elem),
        (elem \ "default").text,
        (elem \ "short-name").text,
        (elem \ "description").text
      ) :: list
    })

    val stepDefs = (xml \ "steps" \ "step").foldLeft(Nil:List[StepDef])((list, elem) => {
      new StepDef(
        (elem \ "@run-type").text,
        new RunnerResources(
          (elem \ "resource" \ "@relative-path").text
        ),
        (elem \ "parameters").foldLeft(Nil)((list, elem) => {
          val key = (elem \ "@name").text
          val scope = parseScope(elem)
          val ref = (elem \ "ref" \ "@ref").text
          val value = (elem \ "value").text

          if (ref != null) {
            new RunnerParameterRef(key, scope, ref)
          } else {
            new RunnerParameterVakue(key, scope, value)
          }
          list
        })
      )
              ::
              list
    })

    println(paramDefs)
  }

  class XmlFormatException extends RuntimeException

}