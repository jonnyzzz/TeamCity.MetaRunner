package jetbrains.buildserver.metarunner.xml

import java.io.File
import javax.xml.XMLConstants
import javax.xml.validation.SchemaFactory
import javax.xml.transform.stream.StreamSource
import org.xml.sax.{InputSource}
import scala.xml.{Node, Elem}
import com.intellij.openapi.util.text.StringUtil
import collection.JavaConversions

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:07 
 */

class MetaRunnerSpecParser {
  def parse(spec: File) = {
    // A schema can be loaded in like ...

    val sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val s = sf.newSchema(new StreamSource(getClass.getResourceAsStream("/meta-runner-config.xsd")))

    val is: InputSource = new InputSource(spec.getPath)
    val xml = new SchemaAwareFactoryAdapter(s).load(is)

    val res = Nil

    def parseType(param: Elem) = {
      param match {
        case x@ <type-text>{_*}</type-text> => TextType(false)
        case x@ <type-hidden>{_*}</type-hidden> => HiddenType
        case x => throw new RuntimeException("Failed to parse: " + x)
      }
    }

    def parseScope(param: Node) = {
      (param \ "@scope").text match {
        case "runner" => RunnerScope
        case "build" => BuildScope
        case x => throw new RuntimeException("Failed to parse scope: " + x)
      }
    }

    val runTypeValue = (xml \ "@runType").text
    val runTypeShortName = (xml \ "@shortName").text
    val runTypeDescription = (xml \ "descpription").text

    val paramDefs = (xml \ "runner-parameters" \ "parameter").foldLeft(Nil: List[ParameterDef])((list, elem) => {
      new ParameterDef(
        (elem \ "@name").text,
        HiddenType,
        (elem \ "default").text,
        (elem \ "short-name").text,
        (elem \ "description").text
      ) :: list
    })

    val stepDefs = (xml \ "steps" \ "step").foldLeft(Nil: List[StepDef])((list, elem) => {
      new StepDef(
        (elem \ "@run-type").text,
        new RunnerResources(
          (elem \ "resource" \ "@relative-path").text
        ),
        (elem \ "parameters").foldLeft(Nil: List[RunnerParameter])((list, elem) => {
          val scope = parseScope(elem)

          val params =
            (elem \ "param").foldLeft(Nil: List[RunnerParameter])((list, elem) => {
              val key = (elem \ "@name").text
              val valueText = (elem \ "value").text
              val valueAttribute = (elem \ "@value").text

              new RunnerParameter(key,
                scope,
                if (StringUtil.isEmptyOrSpaces(valueText))
                  valueAttribute
                else
                  valueText
              )
                      ::
                      list
            })

          list ::: params
        })
      )
              ::
              list
    })

    new RunnerSpec{
      val runType = runTypeValue
      val runners = JavaConversions.asJavaCollection(stepDefs)
      val parameterDefs = JavaConversions.asJavaCollection(paramDefs)
      val description = runTypeDescription
      val shortName = runTypeShortName
    }
  }

  class XmlFormatException extends RuntimeException

}