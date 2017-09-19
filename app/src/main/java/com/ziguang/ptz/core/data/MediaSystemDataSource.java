package com.ziguang.ptz.core.data;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.ziguang.ptz.App;
import com.ziguang.ptz.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class MediaSystemDataSource implements MediaDataSource {

    private static final String TAG = MediaSystemDataSource.class.getSimpleName();

    private static class SingleTon {
        public static final MediaSystemDataSource INSTANCE  = new MediaSystemDataSource();
    }

    private MediaMetadataRetriever mRetriever;

    private MediaSystemDataSource() {
        mRetriever = new MediaMetadataRetriever();
    }

    public static MediaSystemDataSource getInstance() {
        return SingleTon.INSTANCE;
    }

    @Override
    public Observable<Directory> getImages() {
        return rawQueryImages().map(new Func1<List<Media>, Directory>() {
            @Override
            public Directory call(List<Media> medias) {
                Directory directory = new Directory();
                addToAlbums(medias, directory);
                return directory;
            }
        });
    }

    @NonNull
    private Observable<List<Media>> rawQueryImages() {
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
                    if (parentPath == null || !parentPath.equals(FileUtils.PHOTO_DIR_PATH)) {
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
    public Observable<Directory> getVideos() {
        return rawQueryVideos().map(new Func1<List<Media>, Directory>() {
            @Override
            public Directory call(List<Media> medias) {
                Directory directory = new Directory();
                addToAlbums(medias, directory);
                return directory;
            }
        });
    }

    @NonNull
    private Observable<List<Media>> rawQueryVideos() {
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
                    if (parentPath == null || !parentPath.equals(FileUtils.VIDEO_DIR_PATH)) {
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
    public Observable<Directory> getMedias() {
        return rawQueryBoth().map(new Func1<List<Media>, Directory>() {
            @Override
            public Directory call(List<Media> medias) {
                Directory directory = new Directory();
                addToAlbums(medias, directory);
                return directory;
            }
        });
    }

    @Override
    public void saveMedias(@NonNull Directory directory) {

    }

    @Override
    public void saveImages(@NonNull Directory directory) {

    }

    @Override
    public void saveVideos(@NonNull Directory directory) {

    }

    private void addToAlbums(List<Media> medias, Directory directory) {
        Map<String, List<Media>> imagesAndVideos = directory.getMedias();
        for (Media media : medias) {
            if (!imagesAndVideos.containsKey(getDate(media.getDate()))) {
                List<Media> group = new ArrayList<>();
                group.add(media);
                imagesAndVideos.put(getDate(media.getDate()), group);
            } else {
                imagesAndVideos.get(getDate(media.getDate())).add(media);
            }
        }
    }

    private Observable<List<Media>> rawQueryBoth() {
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
                            if (parentPath == null || !parentPath.equals(FileUtils.PHOTO_DIR_PATH)) {
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
                            Log.w(TAG, "path: " + path);
                            parentPath = getParentDir(path);
                            Log.w(TAG, "parentPath: " + parentPath);
                            if (parentPath == null || !parentPath.equals(FileUtils.VIDEO_DIR_PATH)) {
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


    private String getParentDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return file.getParent() + File.separator;
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

    private String getDate(long dateAdded) {
        Date date = new Date(dateAdded * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }
}
