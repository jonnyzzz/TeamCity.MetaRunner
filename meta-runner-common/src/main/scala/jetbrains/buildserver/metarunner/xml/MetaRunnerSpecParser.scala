package jetbrains.buildserver.metarunner.xml

import java.io.File
import javax.xml.XMLConstants
import javax.xml.validation.SchemaFactory
import javax.xml.transform.stream.StreamSource
import org.xml.sax.{InputSource}
import scala.xml.{Node, Elem}
import com.intellij.openapi.util.text.StringUtil
import collection.JavaConversions._
import org.jetbrains.annotations.NotNull

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
        //TODO: support choise
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
    val runTypeDescription = (xml \ "description").text

    val paramDefs = (xml \ "runner-parameters" \ "parameter").map(elem => {
      new ParameterDefImpl(
        (elem \ "@name").text,
        HiddenType,
        (elem \ "@default").text,
        (elem \ "short-name").text,
        (elem \ "description").text
      )
    }).toList

    def parseStepParameterList(elem: Node, scope: ParameterScope): List[RunnerParameter] = {
      (elem \ "param").map(elem=> {
        val key = (elem \ "@name").text
        val valueText = (elem \ "value").text
        val valueAttribute = (elem \ "@value").text

        val defValue =
          if (StringUtil.isEmptyOrSpaces(valueText))
            valueAttribute
          else
            valueText

        (new RunnerParameter(key, scope, defValue))
      }).toList
    }

    def parseStepParameters(elem: Node): List[RunnerParameter] = {
      (elem \ "parameters").flatMap(elem => parseStepParameterList(elem, parseScope(elem))).toList
    }

    val stepDefs = (xml \ "steps" \ "step").map((elem) => {
      new StepDef(
        (elem \ "@run-type").text,
        new RunnerResources((elem \ "resource" \ "@relative-path").text),
        parseStepParameters(elem)
      )
    })

    new RunnerSpec {
      @NotNull
      val runType = runTypeValue
      @NotNull
      val runners : java.util.Collection[_ <: RunnerStepSpec] = stepDefs
      @NotNull
      val parameterDefs : java.util.Collection[_ <: ParameterDef] = paramDefs
      @NotNull
      val description = runTypeDescription
      @NotNull
      val shortName = runTypeShortName
      @NotNull
      val getMetaRunnerRoot = spec.getParentFile()
    }
  }

  class XmlFormatException extends RuntimeException

}