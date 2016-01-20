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

package com.shopgun.android.sdk.requests;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.network.Cache;
import com.shopgun.android.sdk.network.Delivery;
import com.shopgun.android.sdk.network.NetworkResponse;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.sdk.network.RequestQueue;
import com.shopgun.android.sdk.network.Response;
import com.shopgun.android.sdk.network.ShopGunError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class LoaderRequest<T> extends Request<T> implements Delivery {

    public static final String TAG = Constants.getTag(LoaderRequest.class);

    private T mData;
    private final Listener<T> mListener;
    private final List<Request> mRequests = Collections.synchronizedList(new ArrayList<Request>());
    private final List<ShopGunError> mErrors = Collections.synchronizedList(new ArrayList<ShopGunError>());
    private final LoaderDelivery<T> mDelivery;
    private final Object LOCK = new Object();
    private boolean mCanceled = false;

    public LoaderRequest(T data, Listener<T> l) {
        super(Method.PUT, null, null);
        mData = data;
        mListener = l;
        mDelivery = new LoaderDelivery<T>(mListener);
    }

    public T getData() {
        return mData;
    }

    public Request setData(T data) {
        mData = data;
        return this;
    }

    public Request addError(ShopGunError e) {
        mErrors.add(e);
        return this;
    }

    public List<ShopGunError> getErrors() {
        return mErrors;
    }

    @Override
    public Request setRequestQueue(RequestQueue requestQueue) {
        if (getTag() == null) {
            // Attach a tag if one haven't been provided
            // This will be used at a cancellation signal
            setTag(new Object());
        }
        super.setDelivery(this);
        super.setRequestQueue(requestQueue);
        runFillerRequests();
        mCanceled = true;
        super.cancel();
        return this;
    }

    @Override
    public Request setDelivery(Delivery d) {
        throw new RuntimeException(new IllegalAccessException("Custom delivery not possible"));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        return Response.fromError(new InternalOkError());
    }

    @Override
    protected Response<T> parseCache(Cache c) {
        return Response.fromError(new InternalOkError());
    }

    /**
     * Method for creating the needed requests for filling out a given object
     * @return A list of Request
     */
    public abstract List<Request> createRequests(T data);

    private void runFillerRequests() {
        synchronized (LOCK) {
            List<Request> tmp = createRequests(mData);
            Iterator<Request> it = tmp.iterator();
            while (it.hasNext()) {
                Request r = it.next();
                if (r==null) {
                    it.remove();
                }
            }
            mRequests.addAll(tmp);
            for (Request r : mRequests) {
                applyState(r);
                getRequestQueue().add(r);
            }
        }
    }

    /**
     * Method for applying the current request state to the given request
     * @param r A {@link Request} to apply state to.
     */
    private void applyState(Request r) {
        // mimic parent behaviour
        r.setDebugger(getDebugger());
        r.setDelivery(this);
        r.setTag(getTag());
        r.setIgnoreCache(ignoreCache());
        r.setTimeOut(getTimeOut());
        r.setUseLocation(useLocation());
    }

    @Override
    public boolean isCanceled() {
        return mCanceled;
    }

    @Override
    public void cancel() {
        synchronized (LOCK) {
            if (!mCanceled) {
                mCanceled = true;
                super.cancel();
                getRequestQueue().cancelAll(getTag());
            }
        }
    }

    @Override
    public boolean isFinished() {
        for (Request r : mRequests) {
            if (!r.isFinished()) {
                return false;
            }
        }
        return super.isFinished();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postResponse(Request<?> request, Response<?> response) {

        synchronized (LOCK) {

            request.addEvent("post-response");

            // Deliver catalog if needed
            mRequests.remove(request);
            boolean finished = mRequests.isEmpty();
            mDelivery.deliver(request, response, mData, mErrors, !finished);

        }

    }

    /** Callback interface for delivering parsed responses. */
    public interface Listener<T> {
        /**
         * Called when a response is received.
         * @param response The response data.
         * @param errors A list of {@link ShopGunError} that occurred during execution.
         */
        void onComplete(T response, List<ShopGunError> errors);

        /**
         * Called every time a request finishes.
         * <p>This is done on the network thread. Doing intensive work from this thread is discouraged</p>
         * @param response The current state of the response data, this is not a complete set.
         * @param errors A list of {@link ShopGunError} that occurred during execution.
         */
        void onIntermediate(T response, List<ShopGunError> errors);
    }

}