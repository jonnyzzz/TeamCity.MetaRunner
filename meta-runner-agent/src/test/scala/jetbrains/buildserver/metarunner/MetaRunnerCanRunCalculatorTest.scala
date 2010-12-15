package jetbrains.buildserver.metarunner

import org.testng.annotations.Test
import jetbrains.buildServer.agent.impl.BuildRunnerRegistryEx
import org.testng.Assert
import org.jmock.{Expectations, Mockery}
import java.util.Collections
import xml.{RunnerStepSpec, RunnerSpec}
import jetbrains.buildServer.agent.{AgentBuildRunnerInfo, AgentBuildRunner, BuildAgentConfiguration}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 16.12.10 1:51 
 */

@Test
class MetaRunnerCanRunCalculatorTest {

  @Test
  def test_empty() {
    val m = new Mockery()
    val spec = m.mock(classOf[RunnerSpec])
    val reg = m.mock(classOf[BuildRunnerRegistryEx])
    val conf = m.mock(classOf[BuildAgentConfiguration])

    m.checking(new Expectations() {
      import Expectations._
      allowing(spec).runners
      will(returnValue(Collections.emptyList[RunnerStepSpec]))
    })

    val i = new MetaRunnerCanRunCalculatorImpl(spec, reg)
    Assert.assertTrue(i.canRun(conf))

    m.assertIsSatisfied
  }

  @Test
  def test_one_runner_dependent_not_found() {
    val m = new Mockery()
    val spec = m.mock(classOf[RunnerSpec])
    val reg = m.mock(classOf[BuildRunnerRegistryEx])
    val conf = m.mock(classOf[BuildAgentConfiguration])
    val rspec = m.mock(classOf[RunnerStepSpec])

    m.checking(new Expectations() {
      import Expectations._
      allowing(spec).runners
      will(returnValue(Collections.singleton(rspec)))

      allowing(spec).runType
      will(returnValue("qqq"))

      allowing(rspec).runType
      will(returnValue("aaa"))

      allowing(reg).findRunnerById("aaa")
      will(returnValue(null))
    })

    val i = new MetaRunnerCanRunCalculatorImpl(spec, reg)
    Assert.assertFalse(i.canRun(conf))

    m.assertIsSatisfied
  }

  @Test
  def test_one_runner_dependent_can_not() {
    val m = new Mockery()
    val spec = m.mock(classOf[RunnerSpec])
    val reg = m.mock(classOf[BuildRunnerRegistryEx])
    val conf = m.mock(classOf[BuildAgentConfiguration])
    val rspec = m.mock(classOf[RunnerStepSpec])
    val runner = m.mock(classOf[AgentBuildRunner])
    val runnerInfo = m.mock(classOf[AgentBuildRunnerInfo])

    m.checking(new Expectations() {
      import Expectations._
      allowing(spec).runners
      will(returnValue(Collections.singleton(rspec)))

      allowing(spec).runType
      will(returnValue("qqq"))

      allowing(rspec).runType
      will(returnValue("aaa"))

      allowing(reg).findRunnerById("aaa")
      will(returnValue(runner))

      allowing(runner).getRunnerInfo
      will(returnValue(runnerInfo))

      oneOf(runnerInfo).canRun(conf)
      will(returnValue(false))
    })

    val i = new MetaRunnerCanRunCalculatorImpl(spec, reg)
    Assert.assertFalse(i.canRun(conf))

    m.assertIsSatisfied
  }

  @Test
  def test_one_runner_dependent_can() {
    val m = new Mockery()
    val spec = m.mock(classOf[RunnerSpec])
    val reg = m.mock(classOf[BuildRunnerRegistryEx])
    val conf = m.mock(classOf[BuildAgentConfiguration])
    val rspec = m.mock(classOf[RunnerStepSpec])
    val runner = m.mock(classOf[AgentBuildRunner])
    val runnerInfo = m.mock(classOf[AgentBuildRunnerInfo])

    m.checking(new Expectations() {
      import Expectations._
      allowing(spec).runners
      will(returnValue(Collections.singleton(rspec)))

      allowing(spec).runType
      will(returnValue("qqq"))

      allowing(rspec).runType
      will(returnValue("aaa"))

      allowing(reg).findRunnerById("aaa")
      will(returnValue(runner))

      allowing(runner).getRunnerInfo
      will(returnValue(runnerInfo))

      oneOf(runnerInfo).canRun(conf)
      will(returnValue(true))
    })

    val i = new MetaRunnerCanRunCalculatorImpl(spec, reg)
    Assert.assertTrue(i.canRun(conf))

    m.assertIsSatisfied
  }
}