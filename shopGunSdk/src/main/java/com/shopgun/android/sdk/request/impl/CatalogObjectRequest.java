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

package com.shopgun.android.sdk.request.impl;

import com.shopgun.android.sdk.model.Catalog;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.sdk.network.Response.Listener;
import com.shopgun.android.sdk.request.RequestAutoFill;
import com.shopgun.android.sdk.utils.Api;

import java.util.ArrayList;
import java.util.List;

public class CatalogObjectRequest extends ObjectRequest<Catalog> {

    private CatalogObjectRequest(String url, Listener<Catalog> l) {
        super(url, l);
    }

    public static abstract class Builder extends ObjectRequest.Builder<Catalog> {

        public Builder(String catalogId, Listener<Catalog> l) {
            super(new CatalogObjectRequest(Api.Endpoint.catalogId(catalogId), l));
        }

        public ObjectRequest<Catalog> build() {
            ObjectRequest<Catalog> r = super.build();
            if (getAutofill() == null) {
                setAutoFiller(new CatalogAutoFill());
            }
            return r;
        }

        public void setAutoFill(CatalogAutoFill filler) {
            super.setAutoFiller(filler);
        }

    }

    public static class CatalogAutoFill extends RequestAutoFill<Catalog> {

        private boolean mPages = false;
        private boolean mDealer = false;
        private boolean mStore = false;
        private boolean mHotspots = false;

        public CatalogAutoFill() {
        }

        public boolean loadPages() {
            return mPages;
        }

        public void setLoadPages(boolean pages) {
            mPages = pages;
        }

        public boolean loadDealer() {
            return mDealer;
        }

        public void setLoadDealer(boolean dealer) {
            mDealer = dealer;
        }

        public boolean loadStore() {
            return mStore;
        }

        public void setLoadStore(boolean store) {
            mStore = store;
        }

        public boolean loadHotspots() {
            return mHotspots;
        }

        public void setLoadHotspots(boolean hotspots) {
            mHotspots = hotspots;
        }

        @Override
        public List<Request<?>> createRequests(Catalog data) {

            List<Request<?>> reqs = new ArrayList<Request<?>>();

            if (data != null) {

                if (mStore) {
                    reqs.add(getStoreRequest(data));
                }

                if (mDealer) {
                    reqs.add(getDealerRequest(data));
                }

                if (mPages) {
                    reqs.add(getPagesRequest(data));
                }

                if (mHotspots) {
                    reqs.add(getHotspotsRequest(data));
                }

            }

            return reqs;
        }

    }

}