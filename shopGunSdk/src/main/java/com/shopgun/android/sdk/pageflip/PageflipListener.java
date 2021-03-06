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

package com.shopgun.android.sdk.pageflip;

import android.view.View;

import com.shopgun.android.sdk.model.Hotspot;
import com.shopgun.android.sdk.network.ShopGunError;

import java.util.List;

public interface PageflipListener {

    /**
     * Called once the {@link PageflipFragment} is ready to display the catalog.
     */
    public void onReady();

    /**
     * Called on every page change event.
     *
     * @param position The current position in the adapter - not to be confused with pages
     * @param pages The current set of pages being displayed
     */
    public void onPageChange(int position, int[] pages);

    /**
     * Called when the user swipes 'out' of the catalog, at either the most left or most right position.
     *
     * @param left true if trying to swipe out to the most left side, false if it's the most right side.
     */
    public void onOutOfBounds(boolean left);

    /**
     * Called whenever there is a change in the dragging state of the {@link PageflipFragment}
     *
     * @param state The current state
     */
    public void onDragStateChanged(int state);

    /**
     * If an non fatal error occurs, this will be triggered
     *
     * @param error The error
     */
    public void onError(ShopGunError error);

    /**
     * Called when a click is performed on a page in {@link PageflipFragment}.
     *
     * @param v        The {@link View} clicked
     * @param page     The page number of the clicked page
     * @param x        The relative x-position of the page clicked, given as a percentage. (not the pixel clicked)
     * @param y        The relative y-position of the page clicked, given as a percentage. (not the pixel clicked)
     * @param hotspots A list of {@link Hotspot hotspots} that was clicked. The list is empty of nothing was clicked.
     */
    public void onSingleClick(View v, int page, float x, float y, List<Hotspot> hotspots);

    /**
     * Called when a double click is performed on a page in {@link PageflipFragment}.
     *
     * @param v        The {@link View} double clicked
     * @param page     The page number of the double clicked page
     * @param x        The relative x-position of the page double clicked, given as a percentage. (not the pixel clicked)
     * @param y        The relative y-position of the page double clicked, given as a percentage. (not the pixel clicked)
     * @param hotspots A list of {@link Hotspot hotspots} that was double clicked. The list is empty of nothing was clicked.
     */
    public void onDoubleClick(View v, int page, float x, float y, List<Hotspot> hotspots);


    /**
     * Called when a long click is performed on a page in {@link PageflipFragment}.
     *
     * @param v        The {@link View} long click
     * @param page     The page number of the long click page
     * @param x        The relative x-position of the page long click, given as a percentage. (not the pixel clicked)
     * @param y        The relative y-position of the page long click, given as a percentage. (not the pixel clicked)
     * @param hotspots A list of {@link Hotspot hotspots} that was long clicked. The list is empty of nothing was clicked.
     */
    public void onLongClick(View v, int page, float x, float y, List<Hotspot> hotspots);

    /**
     * Called when a zoom event is performed on a page in {@link PageflipFragment}
     *
     * @param v      The view zoomed.
     * @param pages  The pages zoomed
     * @param zoomIn true if it was a zoom-in event, false if it was returned to normal scale.
     */
    public void onZoom(View v, int[] pages, boolean zoomIn);

}
