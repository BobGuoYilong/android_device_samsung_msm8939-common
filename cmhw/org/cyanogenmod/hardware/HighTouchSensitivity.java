/*
 * Copyright (C) 2014-2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.hardware;

import android.util.Log;

import org.cyanogenmod.internal.util.FileUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Glove mode / high touch sensitivity
 */
public class HighTouchSensitivity {

    private static final String TAG = "HighTouchSensitivity";

    private static final String PATH_TK = "/sys/class/sec/sec_touchkey/glove_mode";
    private static final String COMMAND_PATH = "/sys/class/sec/tsp/cmd";
    private static final String COMMAND_RESULT_PATH = "/sys/class/sec/tsp/cmd_result";
    private static final String GLOVE_MODE = "glove_mode";
    private static final String GLOVE_MODE_ENABLE = "glove_mode,1";
    private static final String GLOVE_MODE_DISABLE = "glove_mode,0";
    private static final String STATUS_OK = ":OK";

    /**
     * Whether device supports high touch sensitivity.
     *
     * @return boolean Supported devices must return always true
     */
    public static boolean isSupported() {
        if (!FileUtils.isFileWritable(COMMAND_PATH) ||
                !FileUtils.isFileReadable(COMMAND_RESULT_PATH)) {
            return false;
        }

        return true;
    }

    /** This method returns the current activation status of high touch sensitivity
     *
     * @return boolean Must be false if high touch sensitivity is not supported or not activated,
     * or the operation failed while reading the status; true in any other case.
     */
    public static boolean isEnabled() {
        return (GLOVE_MODE_ENABLE + STATUS_OK).equals(FileUtils.readOneLine(COMMAND_RESULT_PATH));
    }

    /**
     * This method allows to setup high touch sensitivity status.
     *
     * @param status The new high touch sensitivity status
     * @return boolean Must be false if high touch sensitivity is not supported or the operation
     * failed; true in any other case.
     */
    public static boolean setEnabled(boolean status) {
        return FileUtils.writeLine(COMMAND_PATH, status ? GLOVE_MODE_ENABLE : GLOVE_MODE_DISABLE) &&
               FileUtils.writeLine(PATH_TK, (status ? "1" : "0"));
    }
}
