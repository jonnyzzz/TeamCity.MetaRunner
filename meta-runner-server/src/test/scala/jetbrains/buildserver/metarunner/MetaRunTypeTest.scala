package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import jetbrains.buildServer.web.openapi.PluginDescriptor
import org.jmock.{Expectations, Mockery}
import xml._
import org.testng.Assert
import java.lang.String
import jetbrains.buildServer.serverSide._
import scala.collection.JavaConversions._
import collection.immutable.List
import org.jetbrains.annotations.NotNull

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 15.12.10 22:16 
 */

@Test
class MetaRunTypeTest {

  private def createRunnerSpec(_runners : List[RunnerStepSpec], defs : List[ParameterDef]) : RunnerSpec = new RunnerSpec(){
    @NotNull
    def getMetaRunnerRoot = throw new RuntimeException("not implemented")
    @NotNull
    def getMetaRunnerXml = throw new RuntimeException("not implemented")
    @NotNull
    def description = "description-55"
    @NotNull
    def shortName = "short-name-87"
    @NotNull
    def runType = "test-run-type"
    @NotNull
    def runners = _runners
    @NotNull
    def parameterDefs = defs
  }

  private def createRunnerSpec() : RunnerSpec = createRunnerSpec(List(), List())

  private def paramDef(_key: String) = new ParameterDef() {
    @NotNull
    def description = "param-" + _key
    @NotNull
    def shortName = "short-" + _key
    @NotNull
    def defaultValue = "defautl-" + _key
    @NotNull
    def parameterType = TextType(false)
    @NotNull
    def key = _key
  }

  private def paramRef(_scope : ParameterScope, _key: String, _value : String) = new RunnerStepParams() {
    def key = _key
    def value = _value
    def scope = _scope
  }

  private def step(_type : String, refs : List[RunnerStepParams]) = new RunnerStepSpec() {
    def runType = _type
    def parameters = refs
  }

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

  def runType(_type : String, proc : PropertiesProcessor) = new RunType {
    def getDescription = "desc" + _type
    def getDisplayName = "disp" + _type
    def getType = _type
    def getDefaultRunnerProperties = Map[String,String]()
    def getViewRunnerParamsJspFilePath = "aaa"
    def getEditRunnerParamsJspFilePath = "bbb"
    def getRunnerPropertiesProcessor = proc
  }

  def registry : RunTypeRegistry = registry(List())

  def registry(runners : List[RunType]) : RunTypeRegistry = new RunTypeRegistry() {
    def findExtendedRunType(p1: String) = throw new RuntimeException("register not supported")
    def getRegisteredRunTypes = throw new RuntimeException("register not supported")
    def findRunType(p1: String) = runners.find(_.getType.equals(p1)).orNull
    def registerRunType(p1: RunType) = throw new RuntimeException("register not supported")
  }

  @Test
  def test_parametersProcessor_empty() {
    val v = new MetaRunTypePropertiesProcessor(createRunnerSpec(), registry)
    Assert.assertTrue(v.process(Map[String, String]()).isEmpty)
  }

  @Test
  def test_parametersProcessor_do_not_check_unused_params() {
    val v = new MetaRunTypePropertiesProcessor(createRunnerSpec(List(), paramDef("a") :: Nil), registry)
    Assert.assertTrue(v.process(Map[String, String]()).isEmpty)
  }
  def proc(errors : List[String]) = new PropertiesProcessor {
    def process(properties: java.util.Map[String, String]) = properties.keys.filter(errors.toList.contains).map(x=>new InvalidProperty(x, "rrr" + x))
  }

  @Test
  def test_parametersProcessor_call_runner_check() {
    val v = new MetaRunTypePropertiesProcessor(createRunnerSpec(step("u", paramRef(RunnerScope, "q", "%meta.a%") :: Nil) :: Nil, paramDef("a") :: Nil), registry(runType("u", proc("q":: Nil))::Nil))
    val list = v.process(Map[String, String]("a" ->"u"))
    Assert.assertFalse(list.isEmpty)
    Assert.assertEquals(list.size, 1)

    //Error should be shown for "a"
    Assert.assertEquals(list.iterator.next.getPropertyName, "a")
  }

  @Test
  def test_parametersProcessor_call_runner_merge_error() {
    val v = new MetaRunTypePropertiesProcessor(
      createRunnerSpec(
        step("u", paramRef(RunnerScope, "q", "%meta.a%") :: Nil) ::
                step("u", paramRef(RunnerScope, "q", "%meta.a%") :: Nil) :: Nil, paramDef("a") :: Nil),
      registry(runType("u", proc("q":: Nil))::Nil)
    )
    val list = v.process(Map[String, String]("a" ->"u"))
    Assert.assertFalse(list.isEmpty)
    Assert.assertEquals(list.size, 1)

    //Error should be shown for "a"
    Assert.assertEquals(list.iterator.next.getPropertyName, "a")
  }
}
