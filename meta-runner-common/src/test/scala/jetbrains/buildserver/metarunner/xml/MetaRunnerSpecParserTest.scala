package jetbrains.buildserver.metarunner.xml

import jetbrains.buildServer.BaseTestCase
import org.testng.annotations.Test
import jetbrains.buildServer.util.FileUtil

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:43 
*/

@Test
class MetaRunnerSpecParserTest extends BaseTestCase {

  @Test
  def test_01() = {
    val f = createTempFile()
    FileUtil.copyResource(this.getClass(), "/meta-runner-01.xml", f)

    new MetaRunnerSpecParser().parse(f)
  }
}
