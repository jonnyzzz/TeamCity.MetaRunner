package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import xml.{ParameterDef, RunnerStepSpec, RunnerSpec}
import scala.collection.JavaConversions._
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.serverSide.RunTypeRegistry
import org.jmock.{Expectations, Mockery}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:16 
 */

@Test
class MetaRunTypeTest {

  private def createRunnerSpec(runners : List[RunnerStepSpec], defs : List[ParameterDef]) : RunnerSpec = new RunnerSpec(){
    def getMetaRunnerRoot = throw new RuntimeException("not implemented")

    def description = "description-55"

    def shortName = "short-name-87"

    def runType = "test-run-type"

    def runners = runners

    def parameterDefs = defs
  }

  private def createRunnerSpec() : RunnerSpec = createRunnerSpec(List(), List())

  private def createUI = new MetaRunTypeUI {
    def getEditRunnerParamsJspFilePath = "edit.html"

    def getViewRunnerParamsJspFilePath = "view.html"
  }

  @Test
  def test_01() {
    val m = new Mockery()
    val descr = m.mock(classOf[PluginDescriptor])
    val registry = m.mock(classOf[RunTypeRegistry])

    m.checking(new Expectations(){
      import Expectations._
      allowing(registry).registerRunType(`with`(any(classOf[MetaRunType])))
    })

    val t = new MetaRunType(
      createRunnerSpec(),
      createUI,
      new MetaRunTypeDefaultProperties{
        def getDefaultRunnerProperties = Map()
      },
      null
    )

    m.assertIsSatisfied
  }
}