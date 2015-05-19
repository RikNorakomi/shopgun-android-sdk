package com.eTilbudsavis.etasdk.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    public static JSONArray dumpTable(SQLiteDatabase db, String table) {
        Cursor c = db.query(table, null, null, null, null, null, null);
        List<ContentValues> list = cursorToContentValues(c);
        JSONArray jTable = new JSONArray();
        for (ContentValues cv : list) {
            try {
                JSONObject jRow = new JSONObject();
                for (String name : cv.keySet()) {
                    String value = cv.getAsString(name);
                    jRow.put(name, value);
                }
                jTable.put(jRow);
            } catch (JSONException e) {
                // ignore
            }
        }
        return jTable;
    }

    /**
     * Read the cursor into a {@link ContentValues} object, and close the {@link Cursor} when finished
     * @param c A cursor
     * @return A {@link List} of {@link ContentValues}
     */
    public static List<ContentValues> cursorToContentValues(Cursor c) {
        ArrayList<ContentValues> list = new ArrayList<ContentValues>();
        try {
            if(c.moveToFirst()) {
                do {
                    ContentValues map = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(c, map);
                    list.add(map);
                } while(c.moveToNext());
            }
        } finally {
            c.close();
        }
        return list;
    }

}
