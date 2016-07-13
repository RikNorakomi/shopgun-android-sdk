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

package com.shopgun.android.sdk;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShopGun {

    public static final String TAG = Constants.getTag(ShopGun.class);

    public static final int VERSION = 400000;

    private static ShopGun mSingleton;
    private Map<Class<? extends Kit>, Kit> mKits;
    private WeakReference<Activity> mActivity;
    private AtomicBoolean mInititlised = new AtomicBoolean(false);

    private ShopGun(Context context, boolean develop, ExecutorService executorService) {
        mActivity = new WeakReference<Activity>((context instanceof Activity) ? ((Activity) context) : null);
    }

    public static ShopGun with(Context ctx, CoreKit core, Kit... kits) {
        if (mSingleton == null) {
            synchronized (ShopGun.class) {
                if (mSingleton == null) {
                    setSingleton(new Builder(ctx).kits(kits).build());
                }
            }
        }
        return mSingleton;
    }

    private static ShopGun setSingleton(ShopGun sgn) {
        mSingleton = sgn;
        mSingleton.init();
        return mSingleton;
    }

    private static ShopGun getSingleton() {
        if(mSingleton == null) {
            throw new IllegalStateException("Remember to init ShopGun!");
        } else {
            return mSingleton;
        }
    }

    public static boolean isInitialized() {
        return mSingleton != null && mSingleton.mInititlised.get();
    }

    private void init() {
        getSingleton().initKits();
        // TODO initialize kits sequentially, or async with callback behaviour?

    }

    @SuppressWarnings("unchecked")
    public static <T extends Kit> T getKit(Class<T> clazz) {
        return (T) getSingleton().mKits.get(clazz);
    }

    public static void addKit(Kit kit) {
        if (getSingleton().mKits.containsKey(kit.getClass())) {
            throw new IllegalStateException("Kit already added");
        }
        getSingleton().mKits.put(kit.getClass(), kit);
        getSingleton().initKits();
    }

    private void initKits() {

    }

    public static class Builder {

        final Context mContext;
        CoreKit mCore;
        Kit[] mKits;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder kits(Kit... kits) {
            mKits = kits;
            return this;
        }

        public ShopGun build() {

            if (mSingleton != null) {
                throw new IllegalStateException("Simgleton please");
            }

            // check for CoreKit

            return new ShopGun(mContext, false, null);

        }

    }

}
