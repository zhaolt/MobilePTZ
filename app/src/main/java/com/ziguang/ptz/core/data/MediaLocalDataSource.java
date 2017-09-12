package com.ziguang.ptz.core.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziguang.ptz.App;
import com.ziguang.ptz.core.database.DatabaseHelper;
import com.ziguang.ptz.core.database.PersistenceContract;
import com.ziguang.ptz.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class MediaLocalDataSource implements MediaDataSource {

    private final DatabaseHelper mDatabaseHelper;

    private MediaLocalDataSource() {
        mDatabaseHelper = new DatabaseHelper(App.INSTANCE);
    }

    private static class SingleTon {
        public static final MediaLocalDataSource INSTANCE = new MediaLocalDataSource();
    }

    public static MediaLocalDataSource getInstance() {
        return SingleTon.INSTANCE;
    }

    @Override
    public Observable<List<Media>> getImages() {
        final String[] projection = {
                PersistenceContract.AlbumEntry.DATA
        };
        final String sql = String.format("SELECT %s FROM %s WHERE %s=?",
                TextUtils.join(",", projection),
                PersistenceContract.AlbumEntry.TABLE_NAME,
                PersistenceContract.AlbumEntry.MEDIA_TYPE);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Media.TYPE_IMAGE)});
                try {
                    if (cursor != null && cursor.moveToNext()) {
                        String data = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATA));
                        List<Media> medias = new Gson().fromJson(data,
                                new TypeToken<List<Media>>(){}.getType());
                        if (medias == null) {
                            return new ArrayList<>();
                        }
                        return medias;
                    }
                    return null;
                } finally {
                    FileUtils.close(cursor);
                }
            }
        });
    }

    @Override
    public Observable<List<Media>> getVideos() {
        final String[] projection = {
                PersistenceContract.AlbumEntry.DATA
        };
        final String sql = String.format("SELECT %s FROM %s WHERE %s=?",
                TextUtils.join(",", projection),
                PersistenceContract.AlbumEntry.TABLE_NAME,
                PersistenceContract.AlbumEntry.MEDIA_TYPE);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Media.TYPE_VIDEO)});
                try {
                    if (cursor != null && cursor.moveToNext()) {
                        String data = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATA));
                        List<Media> medias = new Gson().fromJson(data,
                                new TypeToken<List<Media>>(){}.getType());
                        if (medias == null) {
                            return new ArrayList<>();
                        }
                        return medias;
                    }
                    return null;
                } finally {
                    FileUtils.close(cursor);
                }
            }
        });
    }


    @Override
    public Observable<List<Media>> getMedias() {
        final String[] projection = {
                PersistenceContract.AlbumEntry.DATA
        };
        final String sql = String.format("SELECT %s FROM %s WHERE %s=?",
                TextUtils.join(",", projection),
                PersistenceContract.AlbumEntry.TABLE_NAME,
                PersistenceContract.AlbumEntry.MEDIA_TYPE);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Media.TYPE_BOTH)});
                try {
                    if (cursor != null && cursor.moveToNext()) {
                        String data = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATA));
                        List<Media> medias = new Gson().fromJson(data,
                                new TypeToken<List<Media>>(){}.getType());
                        if (medias == null) {
                            return new ArrayList<>();
                        }
                        return medias;
                    }
                    return null;
                } finally {
                    FileUtils.close(cursor);
                }
            }
        });
    }

    @Override
    public void saveMedias(@NonNull final List<Media> medias) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Gson gson = new Gson();
                ContentValues values = new ContentValues();
                values.put(PersistenceContract.AlbumEntry._ID, 2);
                values.put(PersistenceContract.AlbumEntry.MEDIA_TYPE, Media.TYPE_BOTH);
                values.put(PersistenceContract.AlbumEntry.DATA, gson.toJson(medias));
                db.insertWithOnConflict(PersistenceContract.AlbumEntry.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                return null;
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void saveImages(@NonNull final List<Media> medias) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Gson gson = new Gson();
                ContentValues values = new ContentValues();
                values.put(PersistenceContract.AlbumEntry._ID, 0);
                values.put(PersistenceContract.AlbumEntry.MEDIA_TYPE, Media.TYPE_IMAGE);
                values.put(PersistenceContract.AlbumEntry.DATA, gson.toJson(medias));
                db.insertWithOnConflict(PersistenceContract.AlbumEntry.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                return null;
            }
        });
    }

    @Override
    public void saveVideos(@NonNull final List<Media> medias) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Gson gson = new Gson();
                ContentValues values = new ContentValues();
                values.put(PersistenceContract.AlbumEntry._ID, 1);
                values.put(PersistenceContract.AlbumEntry.MEDIA_TYPE, Media.TYPE_VIDEO);
                values.put(PersistenceContract.AlbumEntry.DATA, gson.toJson(medias));
                db.insertWithOnConflict(PersistenceContract.AlbumEntry.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                return null;
            }
        });
    }
}
