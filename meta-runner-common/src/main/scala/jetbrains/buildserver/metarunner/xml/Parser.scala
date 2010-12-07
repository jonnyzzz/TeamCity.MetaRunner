package jetbrains.buildserver.metarunner.xml

import java.io.File
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import org.xml.sax.InputSource

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:07 
 */

class Parser {

  def parse(spec: File) = {
    // A schema can be loaded in like ...

    val sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val s = sf.newSchema(new StreamSource(new File("classpath:/meta-runner-config.xsd")))

    val is : InputSource = new InputSource(spec.getPath)
    val xml = new SchemaAwareFactoryAdapter(s).load(is)

    println(xml)

    /*
        def pp = Nil;
        for( param <- (xml \\ "teamcity-runner/parameters/parameter")) {
          param.
          def key = (param \ "@key").text;
        }

        xml match{
          case <teamcity-meta-runner>{ el @ _*}</teamcity-meta-runner> =>
            el match {
              case <parameters>{ params @ _* }</parameters> => _
              case <steps>{ steps @ _* }</steps> => _
              case _ => throw new XmlFormatException
            }
          case _ => throw new XmlFormatException
        }
    */
  }

  class XmlFormatException extends RuntimeException

}