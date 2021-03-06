package jetbrains.buildserver.metarunner.xml

import scala.xml._
import scala.xml.parsing._

import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import javax.xml.validation.Schema
import org.xml.sax.InputSource

class SchemaAwareFactoryAdapter(schema:Schema) extends NoBindingFactoryAdapter {
  override def load(source: InputSource): Elem = {
    // create parser
    val parser: SAXParser = try {
      val f = SAXParserFactory.newInstance()
      f.setNamespaceAware(true)
      f.setFeature("http://xml.org/sax/features/namespace-prefixes", true)
      f.newSAXParser()
    } catch {
      case e: Exception => throw e
    }

    val xr = parser.getXMLReader()
    val vh = schema.newValidatorHandler()
    vh.setContentHandler(this)
    xr.setContentHandler(vh)

    // parse file
    scopeStack.push(TopScope)
    xr.parse(source)
    scopeStack.pop
    return rootElem.asInstanceOf[Elem]
  }
}