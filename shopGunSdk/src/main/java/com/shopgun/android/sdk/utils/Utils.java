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

package com.shopgun.android.sdk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.utils.DateUtils;
import com.shopgun.android.utils.ExceptionUtils;
import com.shopgun.android.utils.PackageUtils;
import com.shopgun.android.utils.UnitUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class Utils {

    public static final String TAG = Constants.getTag(Utils.class);

    /**
     * The date format as returned from the server
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZ";

    /**
     * String representation of epoc
     */
    public static final String DATE_EPOC = "1970-01-01T00:00:00+0000";

    /**
     * Single instance of SimpleDateFormat to save time and memory
     */
    private static SimpleDateFormat mSdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private static final Object DATE_LOCK = new Object();

    private Utils() {
        // private
    }

    /**
     * Create universally unique identifier.
     *
     * @return Universally unique identifier (UUID).
     */
    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Builds a url + query string.<br>
     * e.g.: https://api.etilbudsavis.dk/v2/catalogs?order_by=popular
     *
     * @param r to build from
     * @return A String
     */
    public static String requestToUrlAndQueryString(Request<?> r) {
        if (r == null || r.getUrl() == null) {
            return null;
        }
        if (r.getParameters() == null || r.getParameters().isEmpty()) {
            return r.getUrl();
        }
        return r.getUrl() + "?" + mapToQueryString(r.getParameters(), r.getParamsEncoding());
    }

    /**
     * Returns a string of parameters, ordered alfabetically (for better cache performance)
     *
     * @param apiParams to convert into query parameters
     * @param encoding encoding to use
     * @return a string of parameters
     */
    public static String mapToQueryString(Map<String, String> apiParams, String encoding) {
        if (apiParams == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        LinkedList<String> keys = new LinkedList<String>(apiParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String value = valueIsNull(apiParams.get(key));
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(encode(key, encoding)).append("=").append(encode(value, encoding));

        }
        return sb.toString();
    }

    /**
     *
     * Returns a string of parameters.
     *
     * <p>This method doesn't do encoding or sorting of parameters</p>
     *
     * @param parameters A map of parameters to convert
     * @return A query string
     */
    public static String mapToQueryString(Map<String, String> parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        LinkedList<String> keys = new LinkedList<String>(parameters.keySet());
        for (String key : keys) {
            String value = valueIsNull(parameters.get(key));
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);

        }
        return sb.toString();
    }

    /**
     * Method for handling null-values
     *
     * @param value to check
     * @return s string where the empty string "" represents null
     */
    private static String valueIsNull(Object value) {
        return value == null ? "" : value.toString();
    }

    /**
     * URL encoding of strings
     *
     * @param value    to encode
     * @param encoding encoding to use
     * @return an URL-encoded string
     */
    @SuppressWarnings("deprecation")
    public static String encode(String value, String encoding) {
        try {
            value = URLEncoder.encode(value, encoding);
        } catch (NullPointerException e) {
            // Happens on older devices (HTC Sense)?
            value = URLEncoder.encode(value);
        } catch (UnsupportedEncodingException e) {
            value = URLEncoder.encode(value);
        } catch (IllegalCharsetNameException e) {
            value = URLEncoder.encode(value);
        }
        return value;
    }

    /**
     * Convert an API date of the format "2013-03-03T13:37:00+0000" into a Date object.
     *
     * @param date to convert
     * @return a Date object
     */
    public static Date stringToDate(String date) {
        synchronized (DATE_LOCK) {
            try {
                return mSdf.parse(date);
            } catch (ParseException e) {
                return new Date(0);
            }
        }
    }

    /**
     * Convert a Date object into a date string, that will be accepted by the API.
     * <p>The format for an API date is {@link #DATE_FORMAT}</p>
     *
     * @param date to convert
     * @return a string
     */
    public static String dateToString(Date date) {
        synchronized (DATE_LOCK) {
            try {
                return mSdf.format(date);
            } catch (NullPointerException e) {
                return DATE_EPOC;
            }
        }
    }

    /**
     * Checks a given status code, is in the range from (including) 200 to (not including) 300, or 304
     *
     * @param statusCode to check
     * @return true is is success, else false
     */
    public static boolean isSuccess(int statusCode) {
        return 200 <= statusCode && statusCode < 300 || statusCode == 304;
    }

    /** @deprecated see {@link DateUtils#roundTime(Date)} */
    @Deprecated
    public static Date roundTime(Date date) {
        return DateUtils.roundTime(date);
    }

    /** @deprecated see {@link UnitUtils#humanReadableByteCount(long, boolean)} */
    @Deprecated
    public static String humanReadableByteCount(long bytes, boolean si) {
        return UnitUtils.humanReadableByteCount(bytes, si);
    }

    /** @deprecated see {@link ExceptionUtils#exceptionToString(Throwable)} */
    @Deprecated
    public static String exceptionToString(Throwable t) {
        return ExceptionUtils.exceptionToString(t);
    }

    /**
     * Copy all elements from an iterator to a {@link List}
     * @param it An {@link Iterator}
     * @param <T> Any type
     * @return A list containing all elements from the {@link Iterator}
     */
    public static <T> List<T> copyIterator(Iterator<T> it) {
        List<T> copy = new ArrayList<T>();
        while (it.hasNext()) {
            copy.add(it.next());
        }
        return copy;
    }

    /** @deprecated see {@link UnitUtils#dpToPx(int, Context)} */
    @Deprecated
    public static int convertDpToPx(int dp, Context c) {
        return UnitUtils.dpToPx(dp, c);
    }

    /** @deprecated see {@link UnitUtils#pxToDp(int, Context)} */
    @Deprecated
    public static int convertPxToDp(int px, Context c) {
        return UnitUtils.pxToDp(px, c);
    }

    /** @deprecated see {@link PackageUtils#getVersionName(Context)} */
    @Deprecated
    public static String getAppVersion(Context c) {
        return PackageUtils.getVersionName(c);
    }

    /**
     * Create a deep copy of any {@link Parcelable} implementation.
     *
     * @param obj     An object to clone
     * @param creator The creator to clone from
     * @param <T> Any type
     * @return A clone of the obj
     */
    public static <T extends Parcelable> T copyParcelable(T obj, Parcelable.Creator<T> creator) {
        Parcel parcel = Parcel.obtain();
        obj.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return creator.createFromParcel(parcel);
    }

    /**
     * Create a deep copy of any {@link List} containing {@link Parcelable}
     *
     * @param list    A list to clone
     * @param creator The creator to clone from
     * @param <T> Any type
     * @return A cloned list
     */
    public static <T extends Parcelable> List<T> copyParcelable(List<T> list, Parcelable.Creator<T> creator) {
        ArrayList<T> tmp = new ArrayList<T>();
        for (T t : list) {
            tmp.add(copyParcelable(t, creator));
        }
        return tmp;
    }

    /** @deprecated see {@link PackageUtils#getMetaData(Context)} */
    @Deprecated
    public static Bundle getMetaData(Context c) {
        return PackageUtils.getMetaData(c);
    }

    /**
     * Get the max available heap size
     *
     * @param c A context
     * @return the maximum available heap size for the device
     */
    public static int getMaxHeap(Context c) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return am.getLargeMemoryClass();
        } else {
            return am.getMemoryClass();
        }
    }

}
