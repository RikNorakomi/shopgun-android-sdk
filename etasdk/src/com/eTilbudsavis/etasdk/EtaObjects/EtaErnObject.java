/*******************************************************************************
* Copyright 2014 eTilbudsavis
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
*******************************************************************************/
package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.eTilbudsavis.etasdk.Utils.EtaLog;
import com.eTilbudsavis.etasdk.Utils.Json;

/**
 * EtaErnObject has the advantage of having an Etilbudsavis Resource Name (ERN), that is a <i>unique</i> identifier 
 * for this object in the API v2.
 * 
 * @author Danny Hvam - danny@etilbudsavis.dk
 *
 */
public abstract class EtaErnObject<T> extends EtaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "EtaErnObject";
	
	/* 
	 * A list of nice to have strings, ready to use in subclasses
	 * Also gives a hint as to which classes extend this class
	 */
	protected static final String ERN_CATALOG = "ern:catalog";
	protected static final String ERN_DEALER = "ern:dealer";
	protected static final String ERN_OFFER = "ern:offer";
	protected static final String ERN_SHOPPINGLIST = "ern:shopping:list";
	protected static final String ERN_SHOPPINGLISTITEM = "ern:shoppinglist:item";
	protected static final String ERN_STORE = "ern:store";
	protected static final String ERN_SHARE = "ern:share";
	protected static final String ERN_COUNTRY = "ern:country";
	protected static final String ERN_USER = "ern:user";
	
	private String mId;
	private String mErn;

	@Override
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put(ServerKey.ID, Json.nullCheck(getId()));
			o.put(ServerKey.ERN, Json.nullCheck(getErn()));
		} catch (JSONException e) {
			EtaLog.e(TAG, e);
		}
		return o;
	}
	
	/**
	 * Method for getting the preferred Etilbudsavis Resource Name (ERN) prefix for the given object 
	 * (such as "ern:catalog").
	 * @return The prefix as a {@link String}
	 */
	public abstract String getErnPrefix();

	/**
	 * <p>Set the identifier for this object. An identifier is often a {@link UUID} as described in
	 * <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC&nbsp;4122:</a>, 
	 * though exceptions can occur e.g. {@link Country#setId(String)}.</p>
	 * 
	 * <p>When setting the id, the ERN is automatically update to match</p>
	 * @param id A non-<code>null</code> String
	 * @return This object
	 */
	@SuppressWarnings("unchecked")
	public T setId(String id) {
		if (id != null) {
			mId = id;
			mErn = getErnPrefix() + ":" + id;
		}
		return (T)this;
	}
	
	/**
	 * Get the id for this object.
	 * @return A {@link String}, or <code>null</code>
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Set the Etilbudsavis Resource Name (ERN) for this object.
	 * <p>ERN is a <i>unique</i> identifier for objects in the eTilbudsavis API v2, and should in most cases be 
	 * generated by the server, and <i>not</i> by the client</p>
	 * @param ern A non-<code>null</code> String
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T setErn(String ern) {
		if (ern != null) {
			mErn = ern;
			String[] parts = mErn.split(":");
			mId = parts[parts.length-1];
		}
		return (T)this;
	}
	
	/**
	 * Get the Etilbudsavis Resource Name (ERN) for this object.
	 * <p>ERN is a <i>unique</i> identifier for objects in the eTilbudsavis API v2, and should in most cases be 
	 * generated by the server, and <i>not</i> by the client</p>
	 * @return A {@link String}, or null
	 */
	public String getErn() {
		return mErn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mErn == null) ? 0 : mErn.hashCode());
		result = prime * result + ((mId == null) ? 0 : mId.hashCode());
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
		EtaErnObject<?> other = (EtaErnObject<?>) obj;
		if (mErn == null) {
			if (other.mErn != null)
				return false;
		} else if (!mErn.equals(other.mErn))
			return false;
		if (mId == null) {
			if (other.mId != null)
				return false;
		} else if (!mId.equals(other.mId))
			return false;
		return true;
	}
	
}
