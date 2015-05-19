package com.eTilbudsavis.etasdk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eTilbudsavis.etasdk.Constants;
import com.eTilbudsavis.etasdk.log.EtaLog;
import com.eTilbudsavis.etasdk.model.Catalog;
import com.eTilbudsavis.etasdk.model.Shoppinglist;
import com.eTilbudsavis.etasdk.model.interfaces.SyncState;
import com.eTilbudsavis.etasdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppinglistSQLiteHelper extends DatabaseHelper {

    public static final String TAG = Constants.getTag(ShoppinglistSQLiteHelper.class);

    public static final String TABLE = "shoppinglists";

    public static final String CREATE_TABLE =
            "create table if not exists " + TABLE + "(" +
                    ID + " text primary key, " +
                    ERN + " text, " +
                    MODIFIED + " text not null, " +
                    NAME + " text not null, " +
                    ACCESS + " text not null, " +
                    STATE + " integer not null, " +
                    PREVIOUS_ID + " text, " +
                    TYPE + " text, " +
                    META + " text, " +
                    USER + " integer not null " +
                    ");";

    public ShoppinglistSQLiteHelper(Context context) {
        super(context);
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

    public static List<Shoppinglist> cursorToList(Cursor c) {
        ArrayList<Shoppinglist> list = new ArrayList<Shoppinglist>();
        for (ContentValues cv : DbUtils.cursorToContentValues(c)) {
            Shoppinglist sl = ShoppinglistSQLiteHelper.contentValuesToObject(cv);
            if (sl!=null) {
                list.add(sl);
            }
        }
        return list;
    }

    public static Shoppinglist contentValuesToObject(ContentValues cv) {

        Shoppinglist sl = Shoppinglist.fromName(cv.getAsString(NAME));
        sl.setId(cv.getAsString(ID));
        sl.setErn(cv.getAsString(ERN));
        sl.setModified(Utils.stringToDate(cv.getAsString(MODIFIED)));
        sl.setAccess(cv.getAsString(ACCESS));
        Integer state = cv.getAsInteger(STATE);
        sl.setState(state == null ? SyncState.TO_SYNC : state);
        sl.setPreviousId(cv.getAsString(PREVIOUS_ID));
        sl.setType(cv.getAsString(TYPE));
        try {
            String meta = cv.getAsString(META);
            sl.setMeta(meta == null ? null : new JSONObject(meta));
        } catch (JSONException e) {
            EtaLog.e(TAG, null, e);
        }
        sl.setUserId(cv.getAsInteger(USER));
        return sl;
    }

    public static ContentValues objectToContentValues(Shoppinglist sl, int userId) {
        ContentValues cv = new ContentValues();
        cv.put(ID, sl.getId());
        cv.put(ERN, sl.getErn());
        cv.put(MODIFIED, Utils.dateToString(sl.getModified()));
        cv.put(NAME, sl.getName());
        cv.put(ACCESS, sl.getAccess());
        cv.put(STATE, sl.getState());
        cv.put(PREVIOUS_ID, sl.getPreviousId());
        cv.put(TYPE, sl.getType());
        String meta = sl.getMeta() == null ? null : sl.getMeta().toString();
        cv.put(META, meta);
        cv.put(USER, userId);
        return cv;
    }

}
