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

package jetbrains.buildserver.metarunner.samples;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.util.EventDispatcher;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildserver.metarunner.WellknownMetaRunnerSpecPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.12.10 0:31
 */
public class SamplesDeployer {
  private static final Logger LOG = Logger.getInstance(SamplesDeployer.class.getName());
  private final WellknownMetaRunnerSpecPaths myPaths;

  public SamplesDeployer(@NotNull final WellknownMetaRunnerSpecPaths paths,
                         @NotNull final EventDispatcher<BuildServerListener> disp) {
    myPaths = paths;
    disp.addListener(new BuildServerAdapter(){
      @Override
      public void serverStartup() {
        try {
          unpack();
        } finally {
          disp.removeListener(this);
        }
      }
    });
  }

  private void unpack() {
    final File users = myPaths.getUserPluginsPath();

    if (!users.isDirectory()) {
      users.mkdirs();

      if (!users.isDirectory()) {
        LOG.warn("Failed to create meta-runner specs paths at: " + users);
        return;
      }

      try {
        FileUtil.copyDir(myPaths.getBundledPluginsPath(), new File(users, "__samples__"));

      } catch (IOException e) {
        LOG.warn("Failed to unpack meta-runner samples to: " + users);
      }
    }
  }

}
