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

package jetbrains.buildserver.metarunner.proxy;

import jetbrains.buildserver.metarunner.xml.ParameterDef;
import jetbrains.buildserver.metarunner.xml.RunnerSpec;
import jetbrains.buildserver.metarunner.xml.RunnerStepSpec;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.12.10 23:00
 */
public class ProxifiedRunnerSpecs implements RunnerSpec {
  private final AtomicReference<RunnerSpec> myProxy = new AtomicReference<RunnerSpec>();

  public ProxifiedRunnerSpecs(@NotNull final RunnerSpec spec) {
    setProxy(spec);
  }

  public void setProxy(@NotNull final RunnerSpec spec) {
    myProxy.set(spec);
  }

  @NotNull
  private RunnerSpec getSpec() {
    return myProxy.get();
  }

  @NotNull
  public Collection<? extends ParameterDef> parameterDefs() {
    return getSpec().parameterDefs();
  }

  @NotNull
  public Collection<? extends RunnerStepSpec> runners() {
    return getSpec().runners();
  }

  @NotNull
  public String runType() {
    return getSpec().runType();
  }

  @NotNull
  public String shortName() {
    return getSpec().shortName();
  }

  @NotNull
  public String description() {
    return getSpec().description();
  }

  @NotNull
  public File getMetaRunnerRoot() {
    return getSpec().getMetaRunnerRoot();
  }

  @NotNull
  public File getMetaRunnerXml() {
    return getSpec().getMetaRunnerXml();
  }
}
