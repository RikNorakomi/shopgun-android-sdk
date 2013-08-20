package com.eTilbudsavis.etasdk;

import org.json.JSONException;
import org.json.JSONObject;

import com.eTilbudsavis.etasdk.Utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

	public static final String TAG = "Settings";
	
	/** Name for the SDK SharedPreferences file */
	private static final String PREFS_NAME = "eta_sdk";

	private static final String SESSION_JSON		= "session_json";
	private static final String SESSION_USER		= "session_user";
	private static final String SESSION_PASS		= "session_pass";
	private static final String SESSION_FACEBOOK	= "session_facebook";
	
	private static final String SLM_CURRENT			= "slm_current";
	
	public static final String LOC_SENSOR			= "loc_sensor";
	public static final String LOC_LATITUDE			= "loc_latitude";
	public static final String LOC_LONGITUDE		= "loc_longitude";
	public static final String LOC_RADIUS			= "loc_radius";
	public static final String LOC_BOUND_EAST		= "loc_b_east";
	public static final String LOC_BOUND_NORTH		= "loc_b_north";
	public static final String LOC_BOUND_SOUTH		= "loc_b_south";
	public static final String LOC_BOUND_WEST		= "loc_b_west";
	public static final String LOC_ADDRESS			= "loc_address";
	public static final String LOC_TIME				= "loc_time";

	private static SharedPreferences mPrefs;
	private static SharedPreferences.Editor mEditor;
	private static Context mContext;
	
	public Settings(Context context) {
		mContext = context;
		mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
	}

	public boolean clear() {
		return mEditor.clear().commit();
	}
	
	public JSONObject getSessionJson() {
		String json = mPrefs.getString(SESSION_JSON, null);
		JSONObject session = null;
		if (json != null) {
			try {
				return new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return session;
	}

	public boolean setSessionJson(JSONObject session) {
		return mEditor.putString(SESSION_JSON, session.toString()).commit();
	}

	public String getSessionUser() {
		return mPrefs.getString(SESSION_USER, null);
	}

	public boolean setSessionUser(String user) {
		return mEditor.putString(SESSION_USER, user).commit();
	}

	public String getSessionPass() {
		return mPrefs.getString(SESSION_PASS, null);
	}

	public boolean setSessionFacebook(String token) {
		return mEditor.putString(SESSION_FACEBOOK, token).commit();
	}

	public String getSessionFacebook() {
		return mPrefs.getString(SESSION_FACEBOOK, null);
	}

	public boolean setSessionPass(String pass) {
		return mEditor.putString(SESSION_PASS, pass).commit();
	}

	public String getShoppinglistManagerCurrent() {
		return mPrefs.getString(SLM_CURRENT, null);
	}

	public boolean setShoppinglistManagerCurrent(String id) {
		return mEditor.putString(SLM_CURRENT, id).commit();
	}
	
	public SharedPreferences getPrefs() {
		return mPrefs;
	}
}