package jetbrains.buildserver.metarunner.proxy

import org.testng.annotations.Test
import jetbrains.buildserver.metarunner.{MetaRunnerSpecsLoader}
import org.testng.Assert
import jetbrains.buildserver.metarunner.xml.RunnerSpec
import jetbrains.buildServer.util.Action
import org.jmock.{Expectations, Mockery}

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 23:39 
 */

@Test
class UpdatableSpecLocatorTest {

  @Test
  def test_initial() {
    val m = new Mockery
    val ld = m.mock(classOf[MetaRunnerSpecsLoader])

    val s = new UpdatableSpecsLocator(ld)
    Assert.assertTrue(s.loadMetaRunners.isEmpty)
    m.assertIsSatisfied()
  }

  @Test
  def test_event_load() {
    val m = new Mockery
    val ld = m.mock(classOf[MetaRunnerSpecsLoader])
    val runner  = m.mock(classOf[RunnerSpec])

    m.checking(new Expectations() {
      allowing(ld).loadMetaRunners
      will(Expectations.returnValue(runner :: (Nil:List[RunnerSpec])))

      allowing(runner).runType
      will(Expectations.returnValue("aaa"))
    })

    val s = new UpdatableSpecsLocator(ld)
    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 1)
    m.assertIsSatisfied()
  }

  @Test
  def test_event_add() {
    val m = new Mockery
    val ld = m.mock(classOf[MetaRunnerSpecsLoader])
    val runner  = m.mock(classOf[RunnerSpec])

    m.checking(new Expectations() {
      allowing(ld).loadMetaRunners
      will(Expectations.returnValue(runner :: (Nil:List[RunnerSpec])))

      allowing(runner).runType
      will(Expectations.returnValue("aaa"))
    })

    val s = new UpdatableSpecsLocator(ld)
    var upd : RunnerSpec = null
    s.onRunnerSpecAdded(new Action[RunnerSpec]{
      def apply(p: RunnerSpec) {
        upd = p
      }
    })
    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 1)
    val runner2 = s.loadMetaRunners.head

    Assert.assertNotNull(upd)
    Assert.assertSame(runner2, upd)

    m.assertIsSatisfied()
  }

  @Test
  def test_event_change() {
    val m = new Mockery
    val ld = m.mock(classOf[MetaRunnerSpecsLoader])
    val runner = m.mock(classOf[RunnerSpec])
    m.checking(new Expectations() {
      exactly(2).of(ld).loadMetaRunners
      will(Expectations.returnValue(runner :: (Nil:List[RunnerSpec])))

      allowing(runner).runType
      will(Expectations.returnValue("aaa"))
    })

    val s = new UpdatableSpecsLocator(ld)
    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 1)
    val runner1 = s.loadMetaRunners.head

    Assert.assertFalse(s.loadMetaRunners.isEmpty)

    var upd : RunnerSpec = null
    s.onRunnerSpecChanged(new Action[RunnerSpec]{
      def apply(p: RunnerSpec) {
        upd = p
      }
    })

    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 1)
    val runner2 = s.loadMetaRunners.head

    Assert.assertNotNull(upd)
    Assert.assertSame(runner1, runner2)
    Assert.assertSame(runner1, upd)

    m.assertIsSatisfied()
  }

  @Test
  def test_event_remove() {
    val m = new Mockery
    val ld = m.mock(classOf[MetaRunnerSpecsLoader])
    val runner = m.mock(classOf[RunnerSpec])
    m.checking(new Expectations() {
      exactly(1).of(ld).loadMetaRunners
      will(Expectations.returnValue(runner :: (Nil:List[RunnerSpec])))

      exactly(1).of(ld).loadMetaRunners
      will(Expectations.returnValue(Nil:List[RunnerSpec]))

      allowing(runner).runType
      will(Expectations.returnValue("aaa"))
    })

    val s = new UpdatableSpecsLocator(ld)
    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 1)
    val runner1 = s.loadMetaRunners.head

    Assert.assertFalse(s.loadMetaRunners.isEmpty)

    var upd : RunnerSpec = null
    s.onRunnerSpecRemoved(new Action[RunnerSpec]{
      def apply(p: RunnerSpec) {
        upd = p
      }
    })

    s.reloadRenners()

    Assert.assertEquals(s.loadMetaRunners.size, 0)
    Assert.assertNotNull(upd)
    Assert.assertSame(runner1, upd)

    m.assertIsSatisfied()
  }
}