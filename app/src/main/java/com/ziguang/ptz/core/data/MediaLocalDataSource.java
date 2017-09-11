package com.ziguang.ptz.core.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ziguang.ptz.App;
import com.ziguang.ptz.core.database.DatabaseHelper;
import com.ziguang.ptz.core.database.PersistenceContract;
import com.ziguang.ptz.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

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
        final String sql = String.format("SELECT * FROM %s WHERE %s=?",
                PersistenceContract.AlbumEntry.TABLE_NAME,
                PersistenceContract.AlbumEntry.MEDIA_TYPE);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Media.TYPE_IMAGE)});
                List<Media> mediaList = new ArrayList<>();
                try {
                    while (cursor != null && cursor.moveToNext()) {
                        Media media = new Media();
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry._ID));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.MEDIA_PATH));
                        String location = cursor.getString(cursor.getColumnIndex(
                                PersistenceContract.AlbumEntry.LOCATION));
                        int date = cursor.getInt(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATE));
                        media.setId(id);
                        media.setPath(path);
                        if (!TextUtils.isEmpty(location)) {
                            String[] temp = location.split(",");
                            float longitude = Float.valueOf(temp[0]);
                            float latitude = Float.valueOf(temp[1]);
                            media.setLongitude(longitude);
                            media.setLatitude(latitude);
                        }
                        media.setDate(date);
                        mediaList.add(media);
                    }
                } finally {
                    FileUtils.close(cursor);
                }
                return mediaList;
            }
        });
    }

    @Override
    public Observable<List<Media>> getVideos() {
        final String sql = String.format("SELECT * FROM %s WHERE %s=?",
                PersistenceContract.AlbumEntry.TABLE_NAME,
                PersistenceContract.AlbumEntry.MEDIA_TYPE);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Media.TYPE_VIDEO)});
                List<Media> mediaList = new ArrayList<>();
                try {
                    while (cursor != null && cursor.moveToNext()) {
                        Media media = new Media();
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry._ID));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.MEDIA_PATH));
                        String location = cursor.getString(cursor.getColumnIndex(
                                PersistenceContract.AlbumEntry.LOCATION));
                        int date = cursor.getInt(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATE));
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DURATION));
                        media.setId(id);
                        media.setPath(path);
                        if (!TextUtils.isEmpty(location)) {
                            String[] temp = location.split(",");
                            float longitude = Float.valueOf(temp[0]);
                            float latitude = Float.valueOf(temp[1]);
                            media.setLongitude(longitude);
                            media.setLatitude(latitude);
                        }
                        media.setDate(date);
                        media.setDuration(duration);
                        mediaList.add(media);
                    }
                } finally {
                    FileUtils.close(cursor);
                }
                return mediaList;
            }
        });
    }

    @Override
    public Observable<List<Media>> getMedias() {
        final String sql = String.format("SELECT * FROM %s",
                PersistenceContract.AlbumEntry.TABLE_NAME);
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return Observable.fromCallable(new Callable<List<Media>>() {
            @Override
            public List<Media> call() throws Exception {
                Cursor cursor = db.rawQuery(sql, new String[]{});
                List<Media> mediaList = new ArrayList<>();
                try {
                    while (cursor != null && cursor.moveToNext()) {
                        Media media = new Media();
                        String id = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry._ID));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.MEDIA_PATH));
                        String location = cursor.getString(cursor.getColumnIndex(
                                PersistenceContract.AlbumEntry.LOCATION));
                        int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.MEDIA_TYPE));
                        int date = cursor.getInt(cursor.getColumnIndexOrThrow(
                                PersistenceContract.AlbumEntry.DATE));
                        if (mediaType == Media.TYPE_VIDEO) {
                            int duration = cursor.getInt(cursor.getColumnIndex(
                                    PersistenceContract.AlbumEntry.DURATION));
                            media.setDuration(duration);
                        }
                        media.setId(id);
                        media.setPath(path);
                        if (!TextUtils.isEmpty(location)) {
                            String[] temp = location.split(",");
                            float longitude = Float.valueOf(temp[0]);
                            float latitude = Float.valueOf(temp[1]);
                            media.setLongitude(longitude);
                            media.setLatitude(latitude);
                        }
                        media.setDate(date);
                        media.setMediaType(mediaType);
                        mediaList.add(media);
                    }
                } finally {
                    FileUtils.close(cursor);
                }
                return mediaList;
            }
        });
    }

    @Override
    public void save(@NonNull List<Media> medias) {

    }
}
