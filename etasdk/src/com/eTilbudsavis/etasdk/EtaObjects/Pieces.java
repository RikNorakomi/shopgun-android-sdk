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

import com.eTilbudsavis.etasdk.Utils.EtaLog;
import com.eTilbudsavis.etasdk.Utils.Json;

public class Pieces extends EtaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = "Pieces";
	
	private int mFrom = 1;
	private int mTo = 1;
	
	public Pieces() {
		
	}
	
	public static Pieces fromJSON(JSONObject pieces) {
		return fromJSON(new Pieces(), pieces);
	}
	
	public static Pieces fromJSON(Pieces p, JSONObject pieces) {
		if (p == null) p = new Pieces();
		if (pieces == null) return p;
		
		p.setFrom(Json.valueOf(pieces, ServerKey.FROM, 1));
		p.setTo(Json.valueOf(pieces, ServerKey.TO, 1));
		
		return p;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put(ServerKey.FROM, Json.nullCheck(getFrom()));
			o.put(ServerKey.TO, Json.nullCheck(getTo()));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o;
	}
	
	public int getFrom() {
		return mFrom;
	}
	
	public Pieces setFrom(int from) {
		mFrom = from;
		return this;
	}


	public int getTo() {
		return mTo;
	}


	public Pieces setTo(int to) {
		mTo = to;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mFrom;
		result = prime * result + mTo;
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
		Pieces other = (Pieces) obj;
		if (mFrom != other.mFrom)
			return false;
		if (mTo != other.mTo)
			return false;
		return true;
	}
	
	
}
