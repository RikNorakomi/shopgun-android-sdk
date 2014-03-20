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

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.eTilbudsavis.etasdk.Utils.EtaLog;
import com.eTilbudsavis.etasdk.Utils.Json;

public class Pageflip extends EtaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "Pageflip";
	
	private String mLogo;
	private int mColor = 0;

	public Pageflip() {
	}

	public Pageflip(int color) {
		mColor = color;
	}
	
	public static Pageflip fromJSON(JSONObject pageflip) {
		return fromJSON(new Pageflip(), pageflip);
	}
	
	public static Pageflip fromJSON(Pageflip p, JSONObject pageflip) {
		if (p == null) p = new Pageflip();
		if (pageflip == null) return p;
		
		p.setLogo(Json.valueOf(pageflip, ServerKey.LOGO));
		String color = Json.valueOf(pageflip, ServerKey.COLOR, "7b9119");
		p.setColor(Color.parseColor(String.format("#%s", color)));
		
		return p;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put(ServerKey.LOGO, Json.nullCheck(getLogo()));
			o.put(ServerKey.COLOR, Json.nullCheck(getColorString()));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o;
	}
	
	public String getLogo() {
		return mLogo;
	}
	
	public Pageflip setLogo(String url) {
		mLogo = url;
		return this;
	}
	
	public int getColor() {
		return mColor;
	}

	public String getColorString() {
		return String.format("%06X", 0xFFFFFF & mColor);
	}

	public Pageflip setColor(int color) {
		mColor = color;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + mColor;
		result = prime * result + ((mLogo == null) ? 0 : mLogo.hashCode());
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
		Pageflip other = (Pageflip) obj;
		if (mColor != other.mColor)
			return false;
		if (mLogo == null) {
			if (other.mLogo != null)
				return false;
		} else if (!mLogo.equals(other.mLogo))
			return false;
		return true;
	}
	
	
}
