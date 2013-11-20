package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eTilbudsavis.etasdk.NetworkInterface.Request.Endpoint;
import com.eTilbudsavis.etasdk.NetworkInterface.Request.Param;
import com.eTilbudsavis.etasdk.NetworkInterface.Request.Sort;
import com.eTilbudsavis.etasdk.Utils.EtaLog;


public class Store extends EtaErnObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "Store";
	
	/** Sort a list by distance in ascending order. (smallest to largest) */
	public static final String SORT_DISTANCE = Sort.DISTANCE;

	/** Sort a list by distance in descending order. (largest to smallest)*/
	public static final String SORT_DISTANCE_DESC = Sort.DISTANCE_DESC;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED = Sort.CREATED;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED_DESC = Sort.CREATED_DESC;

	/** Parameter for getting a list of specific store id's */
	public static final String FILTER_STORE_IDS = Param.FILTER_STORE_IDS;

	/** Endpoint for store list resource */
	public static final String ENDPOINT_LIST = Endpoint.STORE_LIST;

	/** Endpoint for a single store resource */
	public static final String ENDPOINT_ID = Endpoint.STORE_ID;

	/** Endpoint for searching stores */
	public static final String ENDPOINT_SEARCH = Endpoint.STORE_SEARCH;

	/** Endpoint for fast searching stores */
	public static final String ENDPOINT_QUICK_SEARCH = Endpoint.STORE_QUICK_SEARCH;

	
	private String mStreet;
	private String mCity;
	private String mZipcode;
	private Country mCountry;
	private double mLatitude = 0.0;
	private double mLongitude = 0.0;
	private String mDealerUrl;
	private String mDealerId;
	private Branding mBranding;
	private String mContact;
	
	private Dealer mDealer;

	public Store() { }

	@SuppressWarnings("unchecked")
	public static ArrayList<Store> fromJSON(JSONArray stores) {
		ArrayList<Store> list = new ArrayList<Store>();
		try {
			for (int i = 0 ; i < stores.length() ; i++ )
				list.add(Store.fromJSON((JSONObject)stores.get(i)));
			
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static Store fromJSON(JSONObject store) {
		return fromJSON(new Store(), store);
	}
	
	private static Store fromJSON(Store s, JSONObject store) {
		if (s == null) s = new Store();
		if (store == null) return s;
		
		try {
			s.setId(getJsonString(store, Key.ID));
			s.setErn(getJsonString(store, Key.ERN));
			s.setStreet(getJsonString(store, Key.STREET));
			s.setCity(getJsonString(store, Key.CITY));
			s.setZipcode(getJsonString(store, Key.ZIP_CODE));
			s.setCountry(Country.fromJSON(store.getJSONObject(Key.COUNTRY)));
			s.setLatitude(store.getDouble(Key.LATITUDE));
			s.setLongitude(store.getDouble(Key.LONGITUDE));
			s.setDealerUrl(getJsonString(store, Key.DEALER_URL));
			s.setDealerId(getJsonString(store, Key.DEALER_ID));
			s.setBranding(Branding.fromJSON(store.getJSONObject(Key.BRANDING)));
			s.setContact(getJsonString(store, Key.CONTACT));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return s;
	}

	public JSONObject toJSON() {
		return toJSON(this);
	}
	
	public static JSONObject toJSON(Store s) {
		JSONObject o = new JSONObject();
		try {
			o.put(Key.ID, s.getId());
			o.put(Key.ERN, s.getErn());
			o.put(Key.STREET, s.getStreet());
			o.put(Key.CITY, s.getCity());
			o.put(Key.ZIP_CODE, s.getZipcode());
			o.put(Key.COUNTRY, s.getCountry().toJSON());
			o.put(Key.LATITUDE, s.getLatitude());
			o.put(Key.LONGITUDE, s.getLongitude());
			o.put(Key.DEALER_URL, s.getDealerUrl());
			o.put(Key.DEALER_ID, s.getDealerId());
			o.put(Key.BRANDING, s.getBranding().toJSON());
			o.put(Key.CONTACT, s.getContact());
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o;
	}
	
	public Store setStreet(String street) {
		mStreet = street;
		return this;
	}

	public String getStreet() {
		return mStreet;
	}

	public Store setCity(String city) {
		mCity = city;
		return this;
	}

	public String getCity() {
		return mCity;
	}

	public Store setZipcode(String zipcode) {
		mZipcode = zipcode;
		return this;
	}

	public String getZipcode() {
		return mZipcode;
	}

	public Store setCountry(Country country) {
		mCountry = country;
		return this;
	}

	public Country getCountry() {
		return mCountry;
	}

	public Store setLatitude(Double latitude) {
		mLatitude = latitude;
		return this;
	}

	public Double getLatitude() {
		return mLatitude;
	}

	public Store setLongitude(Double longitude) {
		mLongitude = longitude;
		return this;
	}

	public Double getLongitude() {
		return mLongitude;
	}

	public String getDealerUrl() {
		return mDealerUrl;
	}

	public Store setDealerUrl(String url) {
		mDealerUrl = url;
		return this;
	}

	public Store setDealerId(String dealer) {
		mDealerId = dealer;
		return this;
	}

	public String getDealerId() {
		return mDealerId;
	}

	public Branding getBranding() {
		return mBranding;
	}

	public Store setBranding(Branding branding) {
		mBranding = branding;
		return this;
	}

	public Store setContact(String contact) {
		mContact = contact;
		return this;
	}
	
	public String getContact() {
		return mContact;
	}

	public Store setDealer(Dealer d) {
		mDealer = d;
		return this;
	}
	
	public Dealer getDealer() {
		return mDealer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Store))
			return false;

		Store s = (Store)o;
		return stringCompare(mId, s.getId()) &&
				stringCompare(mErn, s.getErn()) &&
				stringCompare(mStreet, s.getStreet()) &&
				stringCompare(mCity, s.getCity()) &&
				stringCompare(mZipcode, s.getZipcode()) &&
				mCountry == null ? s.getCountry() == null : mCountry.equals(s.getCountry()) &&
				mLatitude == s.getLatitude() &&
				mLongitude == s.getLongitude() &&
				stringCompare(mDealerUrl, s.getDealerUrl()) &&
				stringCompare(mDealerId, s.getDealerId()) &&
				mBranding == null ? s.getBranding() == null : mBranding.equals(s.getBranding()) &&
				stringCompare(mContact, s.getContact());
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean everything) {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("[")
		.append("branding=").append(mBranding.toString(everything))
		.append(", id=").append(mId)
		.append(", street=").append(mStreet)
		.append(", city=").append(mCity);
		
		if (everything) {
			sb.append(", zipcode=").append(mZipcode)
			.append(", country=").append(mCountry.toString(everything))
			.append(", latitude=").append(mLatitude)
			.append(", longitude=").append(mLongitude)
			.append(", dealer=").append(mDealer == null ? mDealerId : mDealer.toString(everything))
			.append(", contact=").append(mContact);
		}
		return sb.append("]").toString();
	}
	
}
