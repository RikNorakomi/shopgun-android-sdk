/*******************************************************************************
 * Copyright 2015 ShopGun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shopgun.android.sdk.log;

import android.util.Log;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.utils.ExceptionUtils;

public class DefaultLogger implements SgnLogger {

    public static final String TAG = Constants.getTag(DefaultLogger.class);

    /**
     * Variable to control the size of the exception log
     */
    public static final int DEFAULT_EXCEPTION_LOG_SIZE = 16;

    private EventLog mLog;

    public DefaultLogger() {
        this(DEFAULT_EXCEPTION_LOG_SIZE);
    }

    public DefaultLogger(int logSize) {
        mLog = new EventLog(logSize);
    }

    public int v(String tag, String msg) {
        return 0;
    }

    public int v(String tag, String msg, Throwable tr) {
        return 0;
    }

    public int d(String tag, String msg) {
        return 0;
    }

    public int d(String tag, String msg, Throwable tr) {
        return 0;
    }

    public int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    public int i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    public int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    public int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    public int e(String tag, String msg, Throwable tr) {
        if (msg == null || msg.length() == 0) {
            msg = tr.getMessage();
        }
        mLog.add(Event.TYPE_EXCEPTION, ExceptionUtils.exceptionToJson(tr));
        return Log.e(tag, msg, tr);
    }

    public EventLog getLog() {
        return mLog;
    }

}
