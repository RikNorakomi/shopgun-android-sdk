package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.eTilbudsavis.etasdk.Utils.EtaLog;

public class Images extends EtaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "Images";
	
	private String mView;
	private String mZoom;
	private String mThumb;

	public Images() {
	}
	
	public static Images fromJSON(String images) {
		Images i = new Images();
		try {
			i = fromJSON(i, new JSONObject(images));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public static Images fromJSON(JSONObject images) {
		return fromJSON(new Images(), images);
	}
	
	public static Images fromJSON(Images i, JSONObject image) {
		if (i == null) i = new Images();
		if (image == null) return i;
		
		i.setView(getJsonString(image, Key.VIEW));
		i.setZoom(getJsonString(image, Key.ZOOM));
		i.setThumb(getJsonString(image, Key.THUMB));
		
    	return i;
	}
	
	public JSONObject toJSON() {
		return toJSON(this);
	}
	
	public static JSONObject toJSON(Images i) {
		JSONObject o = new JSONObject();
		try {
			o.put(Key.VIEW, i.getView());
			o.put(Key.ZOOM, i.getZoom());
			o.put(Key.THUMB, i.getThumb());
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o;
	}
	
	public String getView() {
		return mView;
	}

	public void setView(String viewUrl) {
		this.mView = viewUrl;
	}

	public String getZoom() {
		return mZoom;
	}

	public void setZoom(String zoomUrl) {
		this.mZoom = zoomUrl;
	}

	public String getThumb() {
		return mThumb;
	}

	public void setThumb(String thumbUrl) {
		this.mThumb = thumbUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Images))
			return false;

		Images i = (Images)o;
		return stringCompare(mThumb, i.getThumb()) &&
				stringCompare(mView, i.getView()) &&
				stringCompare(mZoom, i.getZoom());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
		.append(getClass().getSimpleName()).append("[")
		.append("view=").append(mView)
		.append(", zoom=").append(mZoom)
		.append(", thumb=").append(mThumb)
		.append("]").toString();
	}
	
}
