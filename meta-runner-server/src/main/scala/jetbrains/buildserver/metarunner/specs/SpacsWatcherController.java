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

import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.12.10 22:56
 */
public class SpacsWatcherController {
  private final SpecsWatcher myWatcher;

  public SpacsWatcherController(@NotNull final SpecsWatcher watcher,
                                @NotNull EventDispatcher<BuildServerListener> disp) {
    myWatcher = watcher;
    disp.addListener(new BuildServerAdapter(){
      @Override
      public void serverStartup() {
        myWatcher.start();
      }

      @Override
      public void serverShutdown() {
        myWatcher.stop();
      }
    });
  }
}
