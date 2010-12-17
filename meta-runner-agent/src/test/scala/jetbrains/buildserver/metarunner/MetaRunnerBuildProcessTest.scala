package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import org.testng.Assert
import java.lang.String
import xml._
import org.jmock.{Expectations, Mockery}
import jetbrains.buildServer.agent._
import scala.collection.JavaConversions._
import org.jetbrains.annotations.NotNull
import jetbrains.buildServer.BaseTestCase
import java.io.File
import org.jmock.api.Expectation
import com.intellij.openapi.util.io.FileUtil
import org.hamcrest.{Description, BaseMatcher, Matcher}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 09.12.10 14:19 
 */

@Test
class MetaRunnerBuildProcessTest extends BaseTestCase {

  @Test
  def test_01 = {
    doTest(new DoTest() {
      override def getRunnerSpec() = mockRunnerSpec(Nil: List[RunnerStepSpec])
    })
  }

  @Test
  def test_02 = {
    doTest(new OneStepTest() {
      override def setupExpectations = {
        new Expectations() {
          import Expectations._

          oneOf(step).addRunnerParameter("key", "555")
          oneOf(step).addSystemProperty("AAA", "555")
          oneOf(step).addEnvironmentVariable("ZZZ", "ZZ3Z")
        } :: super.setupExpectations()
      }

      override def getRunnerParmeters()  = {
        new RunnerStepParams() {
          def value = "555"
          def scope = RunnerScope
          def key = "key"
        } :: new RunnerStepParams() {
          def value = "ZZ3Z"
          def scope = BuildScope
          def key = "env.ZZZ"
        } :: (new RunnerStepParams() {
          def value = "555"
          def scope = BuildScope
          def key = "system.AAA"
        } :: Nil)
      }
    })
  }


  @Test
  def test_meta_parameter_reference() {
    doTest(new OneStepTest() {
      val runnerParameters = asJavaMap(Map[String,String](("AAA"->"zpzpz")))

      override protected def setupExpectations() = {
        new Expectations() {
          allowing(runner).getRunnerParameters()
          will(Expectations.returnValue(runnerParameters));

          oneOf(step).addRunnerParameter("key", "zz-zpzpz-uu")
        } :: super.setupExpectations()
      }

      override def getRunnerParmeters() = (new RunnerParameter("key", RunnerScope, "zz-%meta.AAA%-uu") :: Nil)
    })
  }

  @Test
  def test_meta_resources_not_exist() {
    doTest(new OneStepTest() {
      val runnerParameters = asJavaMap(Map[String,String](("AAA"->"zpzpz")))

      override protected def setupExpectations() = {
        new Expectations() {
          allowing(runner).getRunnerParameters()
          will(Expectations.returnValue(runnerParameters));

          oneOf(step).addRunnerParameter("key", "zz-zpzpz-uu")
        } :: super.setupExpectations()
      }


      override protected def getRunnerSpec() = {
        val spec = super.getRunnerSpec()
        FileUtil.delete(spec.getMetaRunnerRoot)
        spec
      }

      override def getRunnerParmeters() = (new RunnerParameter("key", RunnerScope, "zz-%meta.AAA%-uu") :: Nil)
    })
  }

  @Test
  def test_meta_resources_exist_files() {
    doTest(new OneStepTest() {
      val runnerParameters = asJavaMap(Map[String,String](("AAA"->"zpzpz")))

      override protected def setupExpectations() = {
        new Expectations() {
          allowing(runner).getRunnerParameters()
          will(Expectations.returnValue(runnerParameters));

          oneOf(step).addRunnerParameter("key", "zz-zpzpz-uu")
        } :: super.setupExpectations()
      }

      override protected def getRunnerSpec() = {
        val spec = super.getRunnerSpec()
        new File(spec.getMetaRunnerRoot, "folder-001").mkdirs()
        new File(spec.getMetaRunnerRoot, "root-file.txt").createNewFile()
        new File(spec.getMetaRunnerRoot, "folder-002").mkdirs()
        new File(spec.getMetaRunnerRoot, "folder-002/file.txt").createNewFile()
        spec
      }

      override protected def afterRunnerStarted() = {
        val res = getResourcesPath()
        Assert.assertNotNull(res)
        Assert.assertTrue(res.isDirectory())
        Assert.assertTrue(new File(res, "folder-001").isDirectory())
        Assert.assertTrue(new File(res, "root-file.txt").isFile())
        Assert.assertTrue(new File(res, "folder-002/file.txt").isFile())
      }

      override def getRunnerParmeters() = (new RunnerParameter("key", RunnerScope, "zz-%meta.AAA%-uu") :: Nil)
    })
  }


  private abstract class OneStepTest extends DoTest {
    protected val step = m.mock(classOf[BuildRunnerContext], "step-runner")
    override protected def setupExpectations() = {
      new Expectations() {
        import Expectations._

        allowing(runner).getRunnerParameters();

        oneOf(facade).createBuildRunnerContext(build, "meta-ref", "", runner)
        will(Expectations.returnValue(step))

        oneOf(facade).createExecutable(build, step)
        will(Expectations.returnValue(buildProcess))

        oneOf(buildProcess).start()
        oneOf(buildProcess).waitFor()
        will(Expectations.returnValue(BuildFinishedStatus.FINISHED_SUCCESS))
      } :: super.setupExpectations()
    }

    protected def getRunnerParmeters() : List[_ <: RunnerStepParams];

    protected def getRunnerSpec() : RunnerSpec = {
      mockRunnerSpec(new RunnerStepSpec(){
        val parameters  = asJavaCollection(getRunnerParmeters())
        val runType = "meta-ref"
      } :: Nil)
    }
  }


  private abstract class DoTest {
    protected val m = new Mockery()
    protected val facade = m.mock(classOf[BuildProcessFacade])
    protected val build = m.mock(classOf[AgentRunningBuild])
    protected val runner = m.mock(classOf[BuildRunnerContext])
    protected val buildProcess = m.mock(classOf[BuildProcess])

    protected def setupExpectations() : List[Expectations] = Nil

    protected def runnerParameterDef(key: String, value:String) = {
      new ParameterDef() {
        @NotNull
        def description = "desc " + key
        @NotNull
        def shortName = key
        @NotNull
        def defaultValue = value
        @NotNull
        def parameterType = null
        @NotNull
        def key = ""
      }
    }

    protected def mockRunnerSpec(runnerz: List[RunnerStepSpec]) = {
      new RunnerSpec {
        @NotNull
        def description = "this is description"
        @NotNull
        def shortName = "this is short name"
        @NotNull
        def runType = "runType007"
        @NotNull
        def runners = runnerz
        @NotNull
        def parameterDefs = Nil: List[ParameterDef]
        @NotNull
        val getMetaRunnerRoot = createTempDir()
        @NotNull
        val getMetaRunnerXml = createTempFile()
      }
    }

    protected def getRunnerSpec(): RunnerSpec;

    protected def afterRunnerStarted() = {}

    private var resourcesPath : File = null
    protected def getResourcesPath() = resourcesPath

    final def doTest() = {

      setupExpectations().foreach(m.checking)
      m.checking(new Expectations() {
        import Expectations._
        allowing(build).getAgentTempDirectory()
        val tempDir: File = createTempDir()
        will(Expectations.returnValue(tempDir))
        allowing(runner).addRunnerParameter(`with`(equal("meta.runner.resources.path")), `with`(new BaseMatcher[String]() {
          def describeTo(p1: Description) = { p1.appendText("<meta.runner.resources.path>") }

          def matches(p1: AnyRef) = {
            resourcesPath = new File(p1.asInstanceOf[String])
            true
          }
        }))
      })


      val runnerSpec = getRunnerSpec()

      val mr = new MetaRunnerBuildProcess(
        runnerSpec,
        facade,
        build,
        runner
      )

      mr.start()

      afterRunnerStarted()

      Assert.assertEquals(mr.waitFor(), BuildFinishedStatus.FINISHED_SUCCESS)

      m.assertIsSatisfied()
    }
  }

  private def doTest(a: DoTest) = {
    a.doTest()
  }
}
