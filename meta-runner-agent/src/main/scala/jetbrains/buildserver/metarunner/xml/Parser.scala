package jetbrains.buildserver.metarunner.xml

import scala.xml._
import java.util.List
import java.io.{FileInputStream, File}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:07 
 */

class Parser {

  def parse(spec: File) = {
    def reader = new FileInputStream(spec)
    def xml = XML.load(reader);


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