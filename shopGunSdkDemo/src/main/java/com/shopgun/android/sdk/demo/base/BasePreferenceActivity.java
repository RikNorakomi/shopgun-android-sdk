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

package com.shopgun.android.sdk.demo.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.shopgun.android.sdk.ShopGun;
import com.shopgun.android.sdk.demo.Tools;

@SuppressLint("Registered")
public class BasePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.shopGunCreate(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        ShopGun.getInstance(this).onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShopGun.getInstance(this).onStop();
    }

}
