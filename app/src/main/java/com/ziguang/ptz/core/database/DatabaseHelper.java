package com.ziguang.ptz.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ziguang.ptz.BuildConfig;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String INTEGER_TYPE = "INTEGER";

    private static final String TEXT_TYPE = "TEXT";

    private static final String COMMA_SEP = ",";

    private static final int DATABASE_VERSION = BuildConfig.DATABASE_VERSION;

    private static final String DATABASE_NAME = BuildConfig.DATABASE_NAME;

    private static final String SQL_CREATE_ALBUM_ENTRIES =
            "CREATE TABLE " + PersistenceContract.AlbumEntry.TABLE_NAME + " (" +
                    PersistenceContract.AlbumEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    PersistenceContract.AlbumEntry.MEDIA_PATH + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.AlbumEntry.LOCATION + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.AlbumEntry.MEDIA_TYPE + INTEGER_TYPE + COMMA_SEP +
                    PersistenceContract.AlbumEntry.DURATION + INTEGER_TYPE + COMMA_SEP +
                    PersistenceContract.AlbumEntry.DATE + TEXT_TYPE + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALBUM_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
