package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import org.testng.Assert
import java.lang.String
import xml._
import org.jmock.{Expectations, Mockery}
import jetbrains.buildServer.agent._
import scala.collection.JavaConversions._

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 09.12.10 14:19 
 */

@Test
class MetaRunnerBuildProcessTest {

  @Test
  def test_01 = {
    doTest(new DoTest() {
      override def getRunnerSpec() = mockRunnerSpec(Nil: List[RunnerStepSpec])
    })
  }

  @Test
  def test_02 = {

    doTest(new DoTest() {
      val step = m.mock(classOf[BuildRunnerContext], "step-runner")

      override def setupExpectations : Expectations = {
        new Expectations() {
          import Expectations._
          oneOf(facade).createBuildRunnerContext(build, "meta-run-type", "", runner)
          will(returnValue(step))

          oneOf(step).addRunnerParameter("key", "555")
          oneOf(step).addSystemProperty("AAA", "555")
          oneOf(step).addEnvironmentVariable("ZZZ", "ZZ3Z")

          oneOf(facade).createExecutable(build, step)
          will(returnValue(buildProcess))

          oneOf(buildProcess).start()
          oneOf(buildProcess).waitFor()
          will(returnValue(BuildFinishedStatus.FINISHED_SUCCESS))
        }
      }
      override def getRunnerSpec() : RunnerSpec = {
        return mockRunnerSpec(
          new RunnerStepSpec() {
            def parameters = new RunnerStepParams() {
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

            def runType = "meta-run-type"
          } :: Nil)
      }
    })
  }


  @Test
  def test_meta_parameter_reference() {
    doTest(new DoTest() {
      val runnerParameters = asJavaMap(Map[String,String](("AAA"->"zpzpz")))
      val step = m.mock(classOf[BuildRunnerContext], "step-runner")

      override protected def setupExpectations() = {
        new Expectations() {
          import Expectations._

          allowing(runner).getRunnerParameters();
          will(returnValue(runnerParameters));

          oneOf(facade).createBuildRunnerContext(build, "meta-ref", "", runner)
          will(returnValue(step))

          oneOf(step).addRunnerParameter("key", "zz-zpzpz-uu")

          oneOf(facade).createExecutable(build, step)
          will(returnValue(buildProcess))

          oneOf(buildProcess).start()
          oneOf(buildProcess).waitFor()
          will(returnValue(BuildFinishedStatus.FINISHED_SUCCESS))
        }
      }

      protected def getRunnerSpec() : RunnerSpec = {
        mockRunnerSpec(new RunnerStepSpec(){
          def parameters  = (new RunnerParameter("key", RunnerScope, "zz-%meta.AAA%-uu") :: Nil)
          def runType = "meta-ref"
        } :: Nil)
      }
    })

  }

  private abstract class DoTest {
    protected val m = new Mockery()
    protected val facade = m.mock(classOf[BuildProcessFacade])
    protected val build = m.mock(classOf[AgentRunningBuild])
    protected val runner = m.mock(classOf[BuildRunnerContext])
    protected val buildProcess = m.mock(classOf[BuildProcess])

    protected def setupExpectations() : Expectations = {
      new Expectations()
    }

    protected def runnerParameterDef(key: String, value:String) = {
      new ParameterDef() {
        def description = "desc " + key

        def shortName = key

        def defaultValue = value

        def parameterType = null

        def key = ""
      }
    }

    protected def mockRunnerSpec(runnerz: List[RunnerStepSpec]) = {
      new RunnerSpec {
        def description = "this is description"

        def shortName = "this is short name"

        def runType = "runType007"

        def runners = runnerz

        def parameterDefs = Nil: List[ParameterDef]
      }
    }

    protected def getRunnerSpec(): RunnerSpec

    final def doTest() = {
      m.checking(setupExpectations())
      m.checking(new Expectations() {
        import Expectations._

      })


      val runnerSpec = getRunnerSpec()

      val mr = new MetaRunnerBuildProcess(
        runnerSpec,
        facade,
        build,
        runner
      )

      mr.start()
      Assert.assertEquals(mr.waitFor(), BuildFinishedStatus.FINISHED_SUCCESS)

      m.assertIsSatisfied()
    }
  }

  private def doTest(a: DoTest) = {
    a.doTest()
  }
}