package com.ziguang.ptz.core.data;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhaoliangtai on 2017/9/12.
 */

public class MediaRepository implements MediaDataSource {

    private final MediaDataSource mSystemDataSource;

    private final MediaDataSource mLocalDataSource;

    private static MediaRepository instance;

    private MediaRepository(MediaDataSource systemDataSource, MediaDataSource localDataSource) {
        mSystemDataSource = systemDataSource;
        mLocalDataSource = localDataSource;
    }

    public static MediaRepository getInstance(MediaDataSource systemDataSource,
                                              MediaDataSource localDataSource) {
        if (instance == null) {
            synchronized (MediaRepository.class) {
                if (null == instance) {
                    instance = new MediaRepository(systemDataSource, localDataSource);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<Directory> getImages() {
        Observable<Directory> local = mLocalDataSource.getImages();
        Observable<Directory> system = mSystemDataSource.getImages()
                .doOnNext(new Action1<Directory>() {
                    @Override
                    public void call(Directory directory) {
                        saveImages(directory);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public Observable<Directory> getVideos() {
        Observable<Directory> local = mLocalDataSource.getVideos();
        Observable<Directory> system = mSystemDataSource.getVideos()
                .doOnNext(new Action1<Directory>() {
                    @Override
                    public void call(Directory directory) {
                        saveVideos(directory);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public Observable<Directory> getMedias() {
        Observable<Directory> local = mLocalDataSource.getMedias();
        Observable<Directory> system = mSystemDataSource.getMedias()
                .doOnNext(new Action1<Directory>() {
                    @Override
                    public void call(Directory directory) {
                        saveMedias(directory);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public void saveMedias(@NonNull Directory directory) {
        if (directory == null) {
            directory = new Directory();
        }
        mLocalDataSource.saveMedias(directory);
    }

    @Override
    public void saveImages(@NonNull Directory directory) {
        if (null == directory) {
            directory = new Directory();
        }
        mLocalDataSource.saveImages(directory);
    }

    @Override
    public void saveVideos(@NonNull Directory directory) {
        if (null == directory) {
            directory = new Directory();
        }
        mLocalDataSource.saveVideos(directory);
    }
}
