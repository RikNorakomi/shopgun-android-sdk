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

package com.shopgun.android.sdk.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.log.EtaLog;
import com.shopgun.android.sdk.model.interfaces.IJson;
import com.shopgun.android.sdk.utils.Api;
import com.shopgun.android.sdk.utils.Api.JsonKey;
import com.shopgun.android.sdk.utils.Json;
import com.shopgun.android.sdk.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HotspotMap extends HashMap<Integer, List<Hotspot>> implements IJson<JSONArray>, Parcelable {

    public static final String TAG = Constants.getTag(HotspotMap.class);
    private static final long serialVersionUID = -4654824845675092954L;
    private static final String TYPE_OFFER = "offer";
    private static final int[] mRectColors = {
            Color.BLACK,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW,
            Color.MAGENTA
    };
    public static Parcelable.Creator<HotspotMap> CREATOR = new Parcelable.Creator<HotspotMap>() {
        public HotspotMap createFromParcel(Parcel source) {
            return new HotspotMap(source);
        }

        public HotspotMap[] newArray(int size) {
            return new HotspotMap[size];
        }
    };
    private boolean mIsNormalized = false;

    public HotspotMap() {

    }

    private HotspotMap(Parcel in) {
        this.mIsNormalized = in.readByte() != 0;
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = in.readInt();
            List<Hotspot> value = new ArrayList<Hotspot>();
            in.readTypedList(value, Hotspot.CREATOR);
            this.put(key, value);
        }
    }

    public static HotspotMap fromJSON(Dimension d, JSONArray jHotspots) {

        HotspotMap map = new HotspotMap();
        if (jHotspots == null) {
            return map;
        }

        for (int i = 0; i < jHotspots.length(); i++) {

            try {

                JSONObject jHotspot = jHotspots.getJSONObject(i);

                String type = Json.valueOf(jHotspot, JsonKey.TYPE, null);
                // We all know that someone is going to introduce a new type at some point, so might as well check now
                if (TYPE_OFFER.equals(type)) {

                    JSONObject offer = jHotspot.getJSONObject(Api.JsonKey.OFFER);
                    Offer o = Offer.fromJSON(offer);

                    int color = mRectColors[i % mRectColors.length];

                    JSONObject rectangleList = jHotspot.getJSONObject(Api.JsonKey.LOCATIONS);

                    List<String> keys = Utils.copyIterator(rectangleList.keys());

                    for (String key : keys) {

                        Integer page = Integer.valueOf(key);
                        JSONArray rect = rectangleList.getJSONArray(key);

                        if (!map.containsKey(page)) {
                            map.put(page, new ArrayList<Hotspot>());
                        }

                        Hotspot h = Hotspot.fromJSON(rect);

                        h.setPage(page);
                        h.setOffer(o);
                        h.setType(type);
                        h.setColor(color);
                        h.setDualPage(keys.size() > 1);
                        map.get(page).add(h);

                    }
                }

            } catch (JSONException e) {
                EtaLog.e(TAG, e.getMessage(), e);
            }
        }

        map.normalize(d);

        return map;
    }

    public boolean isNormalized() {
        return mIsNormalized;
    }

    public void normalize(Dimension d) {

        if (!d.isSet()) {
            return;
        }

        Set<Integer> keys = keySet();
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (Integer i : keys) {

            List<Hotspot> hotspots = get(i);
            if (hotspots != null && !hotspots.isEmpty()) {
                for (Hotspot h : hotspots) {
                    h.normalize(d);
                }
            }

        }

        mIsNormalized = true;
    }

    public List<Hotspot> getHotspots(int page, double xPercent, double yPercent, boolean landscape) {
        return getHotspots(page, xPercent, yPercent, Hotspot.SIGNIFICANT_AREA, landscape);
    }

    public List<Hotspot> getHotspots(int page, double xPercent, double yPercent, double minArea, boolean landscape) {
        List<Hotspot> list = new ArrayList<Hotspot>();
        List<Hotspot> lh = get(page);
        if (lh == null) {
            return list;
        }
        for (Hotspot h : lh) {
            if (h.inBounds(xPercent, yPercent, minArea, landscape)) {
                list.add(h);
            }
        }
        return list;
    }

    public JSONArray toJSON() {

        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (mIsNormalized ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        HotspotMap other = (HotspotMap) obj;
        if (mIsNormalized != other.mIsNormalized)
            return false;
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(mIsNormalized ? (byte) 1 : (byte) 0);
        dest.writeInt(size());
        for (Map.Entry<Integer, List<Hotspot>> e : entrySet()) {
            dest.writeInt(e.getKey());
            dest.writeTypedList(e.getValue());
        }
    }

}