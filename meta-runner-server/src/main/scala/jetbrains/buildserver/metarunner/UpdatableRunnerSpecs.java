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

import jetbrains.buildServer.util.Action;
import jetbrains.buildServer.util.Disposable;
import jetbrains.buildserver.metarunner.xml.RunnerSpec;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.12.10 23:15
 */
public interface UpdatableRunnerSpecs extends MetaRunnerSpecsLoader {
  Disposable onRunnerSpecAdded(Action<RunnerSpec> action);
  Disposable onRunnerSpecChanged(Action<RunnerSpec> action);
  Disposable onRunnerSpecRemoved(Action<RunnerSpec> action);

  @Nullable
  RunnerSpec findRunnerSpec(@Nullable String runType);
}
