package com.s22010189.pil_pal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PilTrackerBackup extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="pill.db";
    public static final String TABLE_NAME ="pill_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Pharmaceutical_Name";
    public static final String COL_3 = "Doze";
    public static final String COL_4 = "Time_hour";
    public static final String COL_5 = "Time_minute";

    public PilTrackerBackup(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"PHARMACEUTICAL_NAME TEXT,DOZE TEXT,TIME_HOUR TEXT,TIME_MINUTE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String pharmaceutical_name, String doze, String dhour, String dminute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, pharmaceutical_name);
        contentValues.put(COL_3, doze);
        contentValues.put(COL_4,dhour);
        contentValues.put(COL_5,dminute);

        long results = db.insert(TABLE_NAME, null, contentValues);
        if ( results == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("select * from "+TABLE_NAME,null);
        return results;
    }

    public boolean updateData(String id, String pharmaceutical_name, String doze, String string, String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2, pharmaceutical_name);
        contentValues.put(COL_3, doze);

        db.update(TABLE_NAME, contentValues,  "id  = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,  "ID = ?", new String[]{id});
    }
}
