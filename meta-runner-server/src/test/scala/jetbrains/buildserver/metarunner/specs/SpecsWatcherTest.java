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

package jetbrains.buildserver.metarunner.specs;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.util.WaitForAssert;
import jetbrains.buildserver.metarunner.MetaRunnerSpecsPaths;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.12.10 22:24
 */
public class SpecsWatcherTest extends BaseTestCase {
  @Test
  public void test_watchFiles() throws IOException {
    final File path = createTempDir();

    MetaRunnerSpecsPaths paths = new MetaRunnerSpecsPaths() {
      @NotNull
      public Collection<File> getSpecRoots() {
        return Collections.singleton(path);
      }
    };
    SpecsWatcher sw = new SpecsWatcher(paths);

    final AtomicInteger i = new AtomicInteger(0);
    sw.addFilesChangedListener(new Runnable() {
      public void run() {
        i.incrementAndGet();
      }
    });
    sw.setTimeout(10);
    sw.start();
    try {
      final int i0 = i.get();

      new File(path, "aaa.txt").createNewFile();

      new WaitForAssert() {
        @Override
        protected boolean condition() {
          return i.get() > i0;
        }
      };

      final int i1 = i.get();
      new File(path, "zzz").mkdirs();
      new File(path, "zzz/zzz").createNewFile();

      new WaitForAssert() {
        @Override
        protected boolean condition() {
          return i.get() > i1;
        }
      };

    } finally {
      sw.stop();
    }
  }
}
