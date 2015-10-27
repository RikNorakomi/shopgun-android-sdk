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


import android.os.Parcel;
import android.os.Parcelable;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.api.JsonKeys;
import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.model.interfaces.IJson;
import com.shopgun.android.sdk.utils.Json;
import com.shopgun.android.sdk.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Session implements IJson<JSONObject>, Parcelable {

    public static final String TAG = Constants.getTag(Session.class);
    public static Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
    private String mToken;
    private Date mExpires = new Date(1000);
    private User mUser = new User();
    private Permission mPermission;
    private String mProvider;
    private String mClientId;
    private String mReference;

    public Session() {

    }

    private Session(Parcel in) {
        this.mToken = in.readString();
        long tmpMExpires = in.readLong();
        this.mExpires = tmpMExpires == -1 ? null : new Date(tmpMExpires);
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mPermission = in.readParcelable(Permission.class.getClassLoader());
        this.mProvider = in.readString();
        this.mClientId = in.readString();
        this.mReference = in.readString();
    }

    /**
     * Convert a {@link JSONArray} into a {@link List};.
     * @param array A {@link JSONArray}  with a valid API v2 structure for a {@code Session}
     * @return A {@link List} of POJO
     */
    public static List<Session> fromJSON(JSONArray array) {
        List<Session> list = new ArrayList<Session>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = Json.getObject(array, i);
            if (o != null) {
                list.add(Session.fromJSON(o));
            }
        }
        return list;
    }

    /**
     * A factory method for converting {@link JSONObject} into a POJO.
     * @param object A {@link JSONObject} with a valid API v2 structure for a {@code Session}
     * @return A {@link Session}, or {@code null} if {@code object} is {@code null}
     */
    public static Session fromJSON(JSONObject object) {
        if (object == null) {
            return null;
        }

        Session s = new Session();
        s.setToken(Json.valueOf(object, JsonKeys.TOKEN));
        String exp = Json.valueOf(object, JsonKeys.EXPIRES);
        s.setExpires(Utils.stringToDate(exp));
        s.setClientId(Json.valueOf(object, JsonKeys.CLIENT_ID));
        s.setReference(Json.valueOf(object, JsonKeys.REFERENCE));

        JSONObject user = Json.getObject(object, JsonKeys.USER);
        s.setUser(User.fromJSON(user));

        JSONObject perm = Json.getObject(object, JsonKeys.PERMISSIONS);
        s.setPermission(Permission.fromJSON(perm));

        s.setProvider(Json.valueOf(object, JsonKeys.PROVIDER));

        return s;
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        try {
            o.put(JsonKeys.TOKEN, Json.nullCheck(getToken()));
            o.put(JsonKeys.EXPIRES, Json.nullCheck(Utils.dateToString(getExpire())));
            o.put(JsonKeys.USER, getUser().getUserId() == User.NO_USER ? JSONObject.NULL : getUser().toJSON());
            o.put(JsonKeys.PERMISSIONS, Json.toJson(getPermission()));
            o.put(JsonKeys.PROVIDER, Json.nullCheck(getProvider()));
            o.put(JsonKeys.CLIENT_ID, Json.nullCheck(mClientId));
            o.put(JsonKeys.REFERENCE, Json.nullCheck(mReference));
        } catch (JSONException e) {
            SgnLog.e(TAG, "", e);
        }
        return o;
    }

    /**
     * Get this Sessions token. Used for headers in API calls
     * @return token as String if session is active, otherwise null.
     */
    public String getToken() {
        return mToken;
    }

    public Session setToken(String token) {
        mToken = token;
        return this;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public User getUser() {
        return mUser;
    }

    public Session setUser(User user) {
        mUser = user == null ? new User() : user;
        return this;
    }

    public Permission getPermission() {
        return mPermission;
    }

    public Session setPermission(Permission permission) {
        mPermission = permission;
        return this;
    }

    public String getProvider() {
        return mProvider;
    }

    public Session setProvider(String provider) {
        mProvider = provider;
        return this;
    }

    public Session setExpires(Date time) {
        mExpires = time;
        return this;
    }

    public Date getExpire() {
        return mExpires;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mClientId == null) ? 0 : mClientId.hashCode());
        result = prime * result
                + ((mExpires == null) ? 0 : mExpires.hashCode());
        result = prime * result
                + ((mPermission == null) ? 0 : mPermission.hashCode());
        result = prime * result
                + ((mProvider == null) ? 0 : mProvider.hashCode());
        result = prime * result
                + ((mReference == null) ? 0 : mReference.hashCode());
        result = prime * result + ((mToken == null) ? 0 : mToken.hashCode());
        result = prime * result + ((mUser == null) ? 0 : mUser.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Session other = (Session) obj;
        if (mClientId == null) {
            if (other.mClientId != null)
                return false;
        } else if (!mClientId.equals(other.mClientId))
            return false;
        if (mExpires == null) {
            if (other.mExpires != null)
                return false;
        } else if (!mExpires.equals(other.mExpires))
            return false;
        if (mPermission == null) {
            if (other.mPermission != null)
                return false;
        } else if (!mPermission.equals(other.mPermission))
            return false;
        if (mProvider == null) {
            if (other.mProvider != null)
                return false;
        } else if (!mProvider.equals(other.mProvider))
            return false;
        if (mReference == null) {
            if (other.mReference != null)
                return false;
        } else if (!mReference.equals(other.mReference))
            return false;
        if (mToken == null) {
            if (other.mToken != null)
                return false;
        } else if (!mToken.equals(other.mToken))
            return false;
        if (mUser == null) {
            if (other.mUser != null)
                return false;
        } else if (!mUser.equals(other.mUser))
            return false;
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mToken);
        dest.writeLong(mExpires != null ? mExpires.getTime() : -1);
        dest.writeParcelable(this.mUser, flags);
        dest.writeParcelable(this.mPermission, flags);
        dest.writeString(this.mProvider);
        dest.writeString(this.mClientId);
        dest.writeString(this.mReference);
    }

}
