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

package com.shopgun.android.sdk.pageflip.impl;

import android.view.View;

import com.shopgun.android.sdk.model.Hotspot;
import com.shopgun.android.sdk.network.ShopGunError;
import com.shopgun.android.sdk.pageflip.PageflipListener;

import java.util.List;

public class SimplePageflipListener implements PageflipListener {

    @Override
    public void onReady() {

    }

    @Override
    public void onPageChange(int position, int[] pages) {

    }

    @Override
    public void onOutOfBounds(boolean left) {

    }

    @Override
    public void onDragStateChanged(int state) {

    }

    @Override
    public void onError(ShopGunError error) {

    }

    @Override
    public void onSingleClick(View v, int page, float x, float y, List<Hotspot> hotspots) {

    }

    @Override
    public void onDoubleClick(View v, int page, float x, float y, List<Hotspot> hotspots) {

    }

    @Override
    public void onLongClick(View v, int page, float x, float y, List<Hotspot> hotspots) {

    }

    @Override
    public void onZoom(View v, int[] pages, boolean zoomIn) {

    }
}
