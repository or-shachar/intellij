/*
 * Copyright 2020 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.idea.rendering;

import static org.junit.Assert.fail;

import com.android.sdklib.devices.Device;
import com.android.tools.idea.configurations.Configuration;
import com.android.tools.idea.configurations.ConfigurationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.UIUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;

/** Utils for render tests. */
public class AswbRenderTestUtils {
  public static final String DEFAULT_DEVICE_ID = "Nexus 4";

  /** Method to be called before every render test case. */
  public static void beforeRenderTestCase() throws IOException {
    // layoutlib binaries are provided by unpacking the SDK. Copy the binaries to a location that's
    // expected by the test.
    File srcFolder =
        new File(
                "./third_party/java/jetbrains/plugin_api/android_studio_4_2/plugins/android/lib/layoutlib/")
            .getAbsoluteFile();
    File destFolder = new File(PathManager.getHomePath(), "plugins/android/lib/layoutlib/");
    Files.createDirectories(destFolder.getParentFile().toPath());
    Files.createSymbolicLink(destFolder.toPath(), srcFolder.getAbsoluteFile().toPath());

    RenderService.shutdownRenderExecutor(5);
    RenderService.initializeRenderExecutor();
  }

  /** Method to be called before every after test case. */
  public static void afterRenderTestCase() {
    RenderLogger.resetFidelityErrorsFilters();
    waitForRenderTaskDisposeToFinish();
  }

  /** Waits for any RenderTask dispose threads to finish */
  public static void waitForRenderTaskDisposeToFinish() {
    // Make sure there is no RenderTask disposing event in the event queue.
    UIUtil.dispatchAllInvocationEvents();
    Thread.getAllStackTraces().keySet().stream()
        .filter(t -> t.getName().startsWith("RenderTask dispose"))
        .forEach(
            t -> {
              try {
                t.join(10 * 1000); // 10s
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
  }

  @NotNull
  public static Configuration getConfiguration(@NotNull Module module, @NotNull VirtualFile file) {
    return getConfiguration(module, file, DEFAULT_DEVICE_ID);
  }

  @NotNull
  public static Configuration getConfiguration(
      @NotNull Module module, @NotNull VirtualFile file, @NotNull String deviceId) {
    ConfigurationManager configurationManager = ConfigurationManager.getOrCreateInstance(module);
    Configuration configuration = configurationManager.getConfiguration(file);
    configuration.setDevice(findDeviceById(configurationManager, deviceId), false);

    return configuration;
  }

  @NotNull
  private static Device findDeviceById(ConfigurationManager manager, String id) {
    for (Device device : manager.getDevices()) {
      if (device.getId().equals(id)) {
        return device;
      }
    }
    fail("Can't find device " + id);
    throw new IllegalStateException();
  }

  private AswbRenderTestUtils() {}
}
