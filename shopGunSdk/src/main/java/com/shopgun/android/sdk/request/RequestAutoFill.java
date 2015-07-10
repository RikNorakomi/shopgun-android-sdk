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

package com.shopgun.android.sdk.request;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.ShopGun;
import com.shopgun.android.sdk.log.EtaLog;
import com.shopgun.android.sdk.model.Catalog;
import com.shopgun.android.sdk.model.Dealer;
import com.shopgun.android.sdk.model.HotspotMap;
import com.shopgun.android.sdk.model.Images;
import com.shopgun.android.sdk.model.Store;
import com.shopgun.android.sdk.model.interfaces.ICatalog;
import com.shopgun.android.sdk.model.interfaces.IDealer;
import com.shopgun.android.sdk.model.interfaces.IStore;
import com.shopgun.android.sdk.network.Delivery;
import com.shopgun.android.sdk.network.ShopGunError;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.sdk.network.RequestDebugger;
import com.shopgun.android.sdk.network.RequestQueue;
import com.shopgun.android.sdk.network.Response.Listener;
import com.shopgun.android.sdk.network.impl.JsonArrayRequest;
import com.shopgun.android.sdk.network.impl.JsonObjectRequest;
import com.shopgun.android.sdk.network.impl.ThreadDelivery;
import com.shopgun.android.sdk.utils.Api;
import com.shopgun.android.sdk.utils.Api.Endpoint;
import com.shopgun.android.sdk.utils.Api.Param;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class RequestAutoFill<T> {

    public static final String TAG = Constants.getTag(RequestAutoFill.class);

    private Listener<T> mListener;
    private T mData;
    private ShopGunError mError;
    private AutoFillParams mParams;
    private List<Request<?>> mRequests = new ArrayList<Request<?>>();
    private ShopGun mShopGun;

    public RequestAutoFill() {
        mShopGun = ShopGun.getInstance();
    }

    public abstract List<Request<?>> createRequests(T data);

    public void prepare(AutoFillParams params, T data, Listener<T> l) {
        prepare(params, data, null, l);
    }

    public void prepare(AutoFillParams params, T data, ShopGunError e, Listener<T> l) {
        mParams = params;
        mListener = l;
        mData = data;
        mError = e;
    }

    public void execute(RequestQueue rq) {

        mRequests.clear();
        if (mData != null) {
            mRequests = createRequests(mData);
            for (Request<?> r : mRequests) {
                r.addEvent("executed-by-autofiller");
                mParams.applyParams(r);
                r.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
                rq.add(r);
            }
        } else if (mError == null) {
            mError = new AutoLoadError(new Exception("The data provided is null"));
        }
        done();

    }

    protected void done() {
        if (isFinished()) {
            if (mData == null && mError == null) {
                mError = new AutoLoadError(new Exception("Daiam"));
            }
            mListener.onComplete(mData, mError);
        }
    }

    public List<Request<?>> getRequests() {
        return mRequests;
    }

    /**
     * Returns true if ALL requests in this {@link RequestAutoFill} is finished
     *
     * @return true if all {@link RequestAutoFill} are finished, else false
     */
    public boolean isFinished() {
        for (Request<?> r : mRequests) {
            if (!r.isFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if ALL requests in this {@link RequestAutoFill} is cancelled
     *
     * @return true if all {@link RequestAutoFill} are cancelled, else false
     */
    public boolean isCancled() {
        for (Request<?> r : mRequests) {
            if (!r.isCanceled()) {
                return false;
            }
        }
        return true;
    }

    public void cancel() {
        for (Request<?> r : mRequests) {
            r.cancel();
        }
    }

    protected JsonArrayRequest getDealerRequest(final List<? extends IDealer<?>> list) {

        Set<String> ids = new HashSet<String>(list.size());
        for (IDealer<?> item : list) {
            ids.add(item.getDealerId());
        }

        JsonArrayRequest req = new JsonArrayRequest(Endpoint.DEALER_LIST, new Listener<JSONArray>() {

            public void onComplete(JSONArray response, ShopGunError error) {

                if (response != null) {
                    List<Dealer> dealers = Dealer.fromJSON(response);
                    for (IDealer<?> item : list) {
                        for (Dealer d : dealers) {
                            if (item.getDealerId().equals(d.getId())) {
                                item.setDealer(d);
                                break;
                            }
                        }
                    }

                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }

                done();

            }
        });
        req.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        req.setIds(Param.DEALER_IDS, ids);
        return req;
    }

    protected JsonObjectRequest getDealerRequest(final IDealer<?> item) {
        String url = Api.Endpoint.dealerId(item.getDealerId());
        JsonObjectRequest r = new JsonObjectRequest(url, new Listener<JSONObject>() {

            public void onComplete(JSONObject response, ShopGunError error) {

                if (response != null) {
                    item.setDealer(Dealer.fromJSON(response));
                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();
            }
        });
        r.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        return r;
    }

    protected JsonArrayRequest getStoreRequest(final List<? extends IStore<?>> list) {

        Set<String> ids = new HashSet<String>(list.size());
        for (IStore<?> item : list) {
            ids.add(item.getStoreId());
        }

        JsonArrayRequest req = new JsonArrayRequest(Endpoint.STORE_LIST, new Listener<JSONArray>() {

            public void onComplete(JSONArray response, ShopGunError error) {

                if (response != null) {
                    List<Store> stores = Store.fromJSON(response);
                    for (IStore<?> item : list) {
                        for (Store s : stores) {
                            if (item.getStoreId().equals(s.getId())) {
                                item.setStore(s);
                                break;
                            }
                        }
                    }

                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();

            }
        });
        req.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        req.setIds(Param.STORE_IDS, ids);
        return req;
    }

    protected JsonObjectRequest getStoreRequest(final IStore<?> item) {
        String url = Api.Endpoint.storeId(item.getStoreId());
        JsonObjectRequest r = new JsonObjectRequest(url, new Listener<JSONObject>() {

            public void onComplete(JSONObject response, ShopGunError error) {

                if (response != null) {
                    item.setStore(Store.fromJSON(response));
                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();
            }
        });
        r.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        return r;
    }

    protected JsonArrayRequest getCatalogRequest(final List<? extends ICatalog<?>> list) {

        Set<String> ids = new HashSet<String>(list.size());
        for (ICatalog<?> item : list) {
            ids.add(item.getCatalogId());
        }

        Listener<JSONArray> l = new Listener<JSONArray>() {

            public void onComplete(JSONArray response, ShopGunError error) {

                if (response != null) {
                    List<Catalog> catalogss = Catalog.fromJSON(response);
                    for (ICatalog<?> item : list) {
                        for (Catalog c : catalogss) {
                            if (item.getCatalogId().equals(c.getId())) {
                                item.setCatalog(c);
                                break;
                            }
                        }
                    }

                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }

                done();

            }
        };

        JsonArrayRequest req = new JsonArrayRequest(Endpoint.CATALOG_LIST, l);
        req.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        req.setIds(Param.CATALOG_IDS, ids);
        return req;
    }

    protected JsonObjectRequest getCatalogRequest(final ICatalog<?> item) {
        String url = Api.Endpoint.dealerId(item.getCatalogId());
        JsonObjectRequest r = new JsonObjectRequest(url, new Listener<JSONObject>() {

            public void onComplete(JSONObject response, ShopGunError error) {

                if (response != null) {
                    item.setCatalog(Catalog.fromJSON(response));
                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();
            }
        });
        r.setDelivery(new ThreadDelivery(mShopGun.getExecutor()));
        return r;
    }

    protected JsonArrayRequest getPagesRequest(final Catalog c) {

        JsonArrayRequest req = new JsonArrayRequest(Endpoint.catalogPages(c.getId()), new Listener<JSONArray>() {

            public void onComplete(JSONArray response, ShopGunError error) {
                if (response != null) {
                    c.setPages(Images.fromJSON(response));
                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();
            }
        });

        return req;

    }

    protected JsonArrayRequest getHotspotsRequest(final Catalog c) {

        JsonArrayRequest req = new JsonArrayRequest(Endpoint.catalogHotspots(c.getId()), new Listener<JSONArray>() {

            public void onComplete(JSONArray response, ShopGunError error) {
                if (response != null) {
                    c.setHotspots(HotspotMap.fromJSON(c.getDimension(), response));
                } else {
                    EtaLog.d(TAG, error.toJSON().toString());
                }
                done();
            }
        });

        return req;

    }

    public static class AutoFillParams {

        private Object tag = null;
        private RequestDebugger debugger = null;
        private Delivery delivery = null;
        private boolean useLocation = true;
        private boolean ignoreCache = false;
//		private boolean isCachable = true;

        public AutoFillParams() {
            this(new Object(), null, null, true, false);
        }

        public AutoFillParams(Request<?> parent) {
            this(parent.getTag(), parent.getDebugger(), parent.getDelivery(), parent.useLocation(), parent.ignoreCache());
        }

        public AutoFillParams(Object tag, RequestDebugger debugger, Delivery d, boolean useLocation, boolean ignoreCache) {
            this.tag = (tag == null ? new Object() : tag);
            this.debugger = debugger;
            this.delivery = d;
            this.useLocation = useLocation;
            this.ignoreCache = ignoreCache;
        }

        public Object getTag() {
            return tag;
        }

        public AutoFillParams setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public RequestDebugger getDebugger() {
            return debugger;
        }

        public AutoFillParams setDebugger(RequestDebugger debugger) {
            this.debugger = debugger;
            return this;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        public AutoFillParams setDelivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public AutoFillParams setUseLocation(boolean useLocation) {
            this.useLocation = useLocation;
            return this;
        }

        public boolean useLocation() {
            return useLocation;
        }

        public boolean getIgnoreCache() {
            return ignoreCache;
        }

        public AutoFillParams setIgnoreCache(boolean ignoreCache) {
            this.ignoreCache = ignoreCache;
            return this;
        }

        public void applyParams(Request<?> r) {
            r.setTag(tag);
            r.setDebugger(debugger);
            r.setUseLocation(useLocation);
            r.setDelivery(delivery);
            r.setIgnoreCache(ignoreCache);
        }

    }
}