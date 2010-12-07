package jetbrains.buildserver.metarunner.xml

import jetbrains.buildServer.BaseTestCase
import org.testng.annotations.Test

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:43 
 */


@Test
class ParserTest extends BaseTestCase {

  @Test
  def test_01() = {
    def f = createTempFile("<teamcity-meta-runner></teamcity-meta-runner>")

    new Parser().parse(f)
  }
}