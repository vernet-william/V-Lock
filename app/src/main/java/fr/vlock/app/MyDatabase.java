package fr.vlock.app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "MyDb.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Station";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITRE = "Titre";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_ETAT = "Etat";
    public static final String COLUMN_DESCRIPTION = "Description";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITRE + " TEXT, " +
                COLUMN_LATITUDE + " DECIMAL, " +
                COLUMN_LONGITUDE + " DECIMAL, " +
                COLUMN_ETAT + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addStation(String titre, double latitude, double longitude, String etat, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITRE, titre);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_ETAT, etat);
        cv.put(COLUMN_DESCRIPTION, desc);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Log.w(TAG, "Fail");
        } else {
            Log.w(TAG, "Succes");
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


}
