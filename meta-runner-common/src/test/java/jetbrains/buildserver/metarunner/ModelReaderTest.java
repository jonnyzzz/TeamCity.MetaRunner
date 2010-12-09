/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildserver.metarunner;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildserver.metarunner.xml.MetaRunnerSpecParser;
import jetbrains.buildserver.metarunner.xml.RunnerSpec;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         08.12.10 19:43
 */
public class ModelReaderTest extends BaseTestCase {
  @Test
  public void test_01() throws IOException {
    File f = createTempFile();
    FileUtil.copyResource(this.getClass(), "/meta-runner-01.xml", f);

    final RunnerSpec parse = new MetaRunnerSpecParser().parse(f);

    System.out.println("parse = " + parse);
    System.out.println("parse.parameterDefs() = " + parse.parameterDefs());
    System.out.println("parse.runners() = " + parse.runners());

  }
}
