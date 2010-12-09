package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import scala.collection.JavaConversions._
import org.testng.Assert
import jetbrains.buildServer.agent.{BuildFinishedStatus, BuildRunnerContext, AgentRunningBuild, BuildProcessFacade}
import java.lang.String
import xml._
import org.jmock.{Expectations, Mockery}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 09.12.10 14:19 
 */

@Test
class MetaRunnerBuildProcessTest {

  @Test
  def test_01 = {
    val m = new Mockery()
    val facade = m.mock(classOf[BuildProcessFacade])
    val build = m.mock(classOf[AgentRunningBuild])
    val runner = m.mock(classOf[BuildRunnerContext])
    val runnerSpec = mockRunnerSpec(Nil : List[RunnerStepSpec])

    val mr = new MetaRunnerBuildProcess(
      runnerSpec,
      facade,
      build,
      runner
    )

    mr.start()
    Assert.assertEquals(mr.waitFor(), BuildFinishedStatus.FINISHED_SUCCESS)
  }

  @Test
  def test_02 = {
    val m = new Mockery()
    val facade = m.mock(classOf[BuildProcessFacade])
    val build = m.mock(classOf[AgentRunningBuild])
    val runner = m.mock(classOf[BuildRunnerContext])
    val step = m.mock(classOf[BuildRunnerContext], "step-runner")
    val runnerSpec = mockRunnerSpec(
      new RunnerStepSpec(){
        def parameters = new RunnerStepParams(){
          def value = "555"
          def scope = RunnerScope
          def key = "key"
        } :: ( new RunnerStepParams(){
          def value = "555"
          def scope = RunnerScope
          def key = "system.AAA"
        } :: Nil )

        def runType = "meta-run-type"
      }:: Nil)

    m.checking(new Expectations(){
      import Expectations._

      allowing(facade).createBuildRunnerContext(build, "meta-run-type", "", runner)
      will(returnValue(step))

      allowing(step).addRunnerParameter("key", "555")
      allowing(step).addSystemProperty("AAA", "555")
    })


    val mr = new MetaRunnerBuildProcess(
      runnerSpec,
      facade,
      build,
      runner
    )

    mr.start()
    Assert.assertEquals(mr.waitFor(), BuildFinishedStatus.FINISHED_SUCCESS)
  }




  def mockRunnerSpec(runnerz : List[RunnerStepSpec]) = {
    new RunnerSpec{
      def description = "this is description"

      def shortName = "this is short name"

      def runType = "runType007"

      def runners = runnerz

      def parameterDefs = Nil : List[ParameterDef]
    }
  }
}