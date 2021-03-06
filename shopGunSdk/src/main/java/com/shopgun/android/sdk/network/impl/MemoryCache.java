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

package com.shopgun.android.sdk.network.impl;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.network.Cache;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.sdk.network.Request.Method;
import com.shopgun.android.sdk.network.Response;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache implements Cache {

    public static final String TAG = Constants.getTag(MemoryCache.class);

    /** On average we've measured a Cache.Item from the ETA API to be around 4kb */
    private static final int AVG_ITEM_SIZE = 4096;

    /** Max cache size - init to 1mb */
    private int mMaxItems = 256;

    /** Perceent of cache to remove on cleanup */
    private int mPercentToClean = 20;

    private Map<String, Item> mCache;

    public MemoryCache() {

        setLimit(Runtime.getRuntime().maxMemory() / 8);

        float loadFactor = 1.5f; // Quicker lookup times
        boolean accessOrder = true; // Enable LRU ordering
        mCache = Collections.synchronizedMap(new LinkedHashMap<String, Item>(mMaxItems, loadFactor, accessOrder));

    }

    /**
     * Set the percentage of cache to clean out when memory limit is hit
     * @param percentToClean A percentage between 0 and 100 (default is 20)
     */
    public void setCleanLimit(int percentToClean) {
        if (percentToClean <= 0 || 100 <= percentToClean) {
            throw new IllegalArgumentException("Percent a number between 0-100");
        }
        mPercentToClean = percentToClean;
    }

    /**
     * Set the limit on memory this Cache may use.
     * @param maxMemLimit The limit in bytes
     */
    public void setLimit(long maxMemLimit) {
        if (maxMemLimit > Runtime.getRuntime().maxMemory()) {
            throw new IllegalArgumentException("maxMemLimit cannot be more than max heap size");
        }
        mMaxItems = (int) (maxMemLimit / AVG_ITEM_SIZE);
        SgnLog.v(TAG, "New memory limit: " + maxMemLimit / 1024 + "kb (approx " + mMaxItems + " items)");
    }

    public void put(Request<?> request, Response<?> response) {

        // If the request is cacheable
        if (request.getMethod() == Method.GET && request.isCacheable() && !request.isCacheHit() && response.cache != null) {

            request.addEvent("add-response-to-cache");
            synchronized (MemoryCache.class) {
                mCache.putAll(response.cache);
                checkSize();
            }

        }

    }

    private void checkSize() {

        int size = mCache.size();

        if (size > mMaxItems) {

            float percentToRemove = (float) mPercentToClean / (float) 100;
            int itemsToRemove = (int) (size * percentToRemove);

            //least recently accessed item will be the first one iterated
            Iterator<Entry<String, Cache.Item>> it = mCache.entrySet().iterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
                if (itemsToRemove-- == 0) {
                    break;
                }
            }

            SgnLog.d(TAG, "Cleaned " + TAG + " new size: " + mCache.size());

        }

    }

    public Cache.Item get(String key) {

        synchronized (MemoryCache.class) {

            Cache.Item c = mCache.get(key);
            if (c == null) {
                return null;
            } else if (c.isExpired()) {
                mCache.remove(key);
                return null;
            }
            return c;

        }

    }

    public void clear() {
        synchronized (MemoryCache.class) {
            mCache.clear();
        }
    }

}
