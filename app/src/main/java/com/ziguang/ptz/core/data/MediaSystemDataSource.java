package com.ziguang.ptz.core.data;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ziguang.ptz.App;
import com.ziguang.ptz.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class MediaSystemDataSource implements MediaDataSource {

    private static class SingleTon {
        public static final MediaSystemDataSource INSTANCE  = new MediaSystemDataSource();
    }

    private MediaMetadataRetriever mRetriever;

    private MediaSystemDataSource() {
        mRetriever = new MediaMetadataRetriever();
    }

    public MediaSystemDataSource getInstance() {
        return SingleTon.INSTANCE;
    }

    @Override
    public Observable<List<Media>> getImages() {
        final String[] imageProjection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED
        };
        return Observable.unsafeCreate(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Context context = App.INSTANCE;
                Cursor cursor = null;
                try {
                    cursor = MediaStore.Images.Media.query(
                            context.getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imageProjection);
                    if (!subscriber.isUnsubscribed() && cursor != null) {
                        subscriber.onNext(cursor);
                    }
                    subscriber.onCompleted();
                } finally {
                    FileUtils.close(cursor);
                }
            }
        }).map(new Func1<Cursor, List<Media>>() {
            @Override
            public List<Media> call(Cursor cursor) {
                List<Media> mediaList = new ArrayList<>();
                mediaList.clear();
                while (cursor != null && cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    int dateAdded = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    float longitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                    float latitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                    if (ignoreSpecificTypeFile(path)) {
                        continue;
                    }
                    String parentPath = getParentDir(path);
                    if (!parentPath.equals(FileUtils.PHOTO_DIR_PATH)) {
                        continue;
                    }
                    Media media = new Media();
                    media.setId(id);
                    media.setPath(path);
                    media.setDate(dateAdded);
                    media.setLatitude(latitude);
                    media.setLongitude(longitude);
                    media.setMediaType(Media.TYPE_VIDEO);
                    mediaList.add(media);
                }
                return mediaList;
            }
        });
    }

    @Override
    public Observable<List<Media>> getVideos() {
        final String[] videoProjection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.LATITUDE,
                MediaStore.Video.Media.LONGITUDE,
                MediaStore.Video.Media.DATE_ADDED
        };
        return Observable.unsafeCreate(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Context context = App.INSTANCE;
                Cursor cursor = null;
                try {
                    cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, videoProjection);
                    if (!subscriber.isUnsubscribed() && cursor != null) {
                        subscriber.onNext(cursor);
                    }
                    subscriber.onCompleted();
                } finally {
                    FileUtils.close(cursor);
                }
            }
        }).map(new Func1<Cursor, List<Media>>() {
            @Override
            public List<Media> call(Cursor cursor) {
                List<Media> mediaList = new ArrayList<>();
                mediaList.clear();
                while (cursor != null && cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    long duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    if (duration < 1) {
                        continue;
                    }
                    int dateAdded = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                    float longitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
                    float latitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE));
                    if (ignoreSpecificTypeFile(path)) {
                        continue;
                    }
                    String parentPath = getParentDir(path);
                    if (!parentPath.equals(FileUtils.VIDEO_DIR_PATH)) {
                        continue;
                    }
                    Media media = new Media();
                    media.setId(id);
                    media.setPath(path);
                    media.setDate(dateAdded);
                    media.setLatitude(latitude);
                    media.setLongitude(longitude);
                    media.setMediaType(Media.TYPE_VIDEO);
                    media.setDuration(duration);
                    mediaList.add(media);
                }
                return mediaList;
            }
        });
    }

    @Override
    public Observable<List<Media>> getMedias() {
        return rawQuery();
    }

    private Observable<List<Media>> rawQuery() {
        final String[] imagesProjection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED
        };
        final String[] videoProjection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.LATITUDE,
                MediaStore.Video.Media.LONGITUDE,
                MediaStore.Video.Media.DATE_ADDED
        };
        return Observable.unsafeCreate(new Observable.OnSubscribe<Cursor[]>() {
            @Override
            public void call(Subscriber<? super Cursor[]> subscriber) {
                Context context = App.INSTANCE;
                Cursor[] cursors = new Cursor[2];
                try {
                    cursors[0] = MediaStore.Images.Media.query(
                            context.getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imagesProjection);
                    cursors[1] = MediaStore.Video.query(context.getContentResolver(),
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection);
                    if (!subscriber.isUnsubscribed() && cursors[0] != null && cursors[1] != null) {
                        subscriber.onNext(cursors);
                    }
                    subscriber.onCompleted();
                } finally {
                    FileUtils.close(cursors[0]);
                    FileUtils.close(cursors[1]);
                }

            }
        }).map(new Func1<Cursor[], List<Media>>() {
            @Override
            public List<Media> call(Cursor[] cursors) {
                List<Media> mediaList = new ArrayList<>();
                CursorJoiner cursorJoiner = new CursorJoiner(cursors[0], imagesProjection,
                        cursors[1], videoProjection);
                mediaList.clear();
                while (cursorJoiner.hasNext()) {
                    CursorJoiner.Result result = cursorJoiner.next();
                    Media media = null;
                    String id, path, parentPath;
                    int dateAdded;
                    float longitude, latitude;
                    switch (result) {
                        case LEFT:
                            id = cursors[0].getString(cursors[0].getColumnIndex(
                                    MediaStore.Images.Media._ID));
                            path = cursors[0].getString(cursors[0].getColumnIndex(
                                    MediaStore.Images.Media.DATA));
                            dateAdded = cursors[0].getInt(cursors[0].getColumnIndex(
                                    MediaStore.Images.Media.DATE_ADDED));
                            longitude = cursors[0].getFloat(cursors[0].getColumnIndex(
                                    MediaStore.Images.Media.LONGITUDE));
                            latitude = cursors[0].getFloat(cursors[0].getColumnIndex(
                                    MediaStore.Images.Media.LATITUDE));

                            if (ignoreSpecificTypeFile(path)) {
                                continue;
                            }
                            parentPath = getParentDir(path);
                            if (!parentPath.equals(FileUtils.PHOTO_DIR_PATH)) {
                                continue;
                            }
                            media = new Media();
                            media.setId(id);
                            media.setPath(path);
                            media.setDate(dateAdded);
                            media.setLatitude(latitude);
                            media.setLongitude(longitude);
                            media.setMediaType(Media.TYPE_IMAGE);
                            break;
                        case RIGHT:
                            id = cursors[1].getString(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media._ID));
                            path = cursors[1].getString(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media.DATA));
                            long duration = cursors[1].getInt(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media.DURATION));
                            if (duration < 1) {
                                continue;
                            }
                            dateAdded = cursors[1].getInt(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media.DATE_ADDED));
                            longitude = cursors[1].getFloat(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media.LONGITUDE));
                            latitude = cursors[1].getFloat(cursors[1].getColumnIndex(
                                    MediaStore.Video.Media.LATITUDE));
                            if (ignoreSpecificTypeFile(path)) {
                                continue;
                            }
                            parentPath = getParentDir(path);
                            if (!parentPath.equals(FileUtils.VIDEO_DIR_PATH)) {
                                continue;
                            }
                            media = new Media();
                            media.setId(id);
                            media.setPath(path);
                            media.setDate(dateAdded);
                            media.setLatitude(latitude);
                            media.setLongitude(longitude);
                            media.setMediaType(Media.TYPE_VIDEO);
                            media.setDuration(duration);
                            break;
                    }
                    if (null != media) {
                        mediaList.add(media);
                    }
                }
                return mediaList;
            }
        });
    }

    @Override
    public void saveMedias(@NonNull List<Media> medias) {
        // do nothing
    }

    @Override
    public void saveImages(@NonNull List<Media> medias) {
        // do nothing
    }

    @Override
    public void saveVideos(@NonNull List<Media> medias) {
        // do nothing
    }

    private String getParentDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return file.getParent();
    }

    /**
     * 过滤非图片、视频文件
     * @param path
     * @return
     */
    private boolean ignoreSpecificTypeFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (path.endsWith(".jpeg") ||
                    path.endsWith(".JPEG") ||
                    path.endsWith(".jpg") ||
                    path.endsWith(".JPG") ||
                    path.endsWith(".mp4") ||
                    path.endsWith(".MP4") ||
                    path.endsWith(".mov") ||
                    path.endsWith(".MOV")) {
                return false;
            }
        }
        return true;
    }
}
