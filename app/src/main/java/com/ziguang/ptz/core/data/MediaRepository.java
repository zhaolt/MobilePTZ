package com.ziguang.ptz.core.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
    public Observable<List<Media>> getImages() {
        Observable<List<Media>> local = mLocalDataSource.getImages();
        Observable<List<Media>> system = mSystemDataSource.getImages()
                .doOnNext(new Action1<List<Media>>() {
                    @Override
                    public void call(List<Media> medias) {
                        saveImages(medias);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public Observable<List<Media>> getVideos() {
        Observable<List<Media>> local = mLocalDataSource.getVideos();
        Observable<List<Media>> system = mSystemDataSource.getVideos()
                .doOnNext(new Action1<List<Media>>() {
                    @Override
                    public void call(List<Media> medias) {
                        saveVideos(medias);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public Observable<List<Media>> getMedias() {
        Observable<List<Media>> local = mLocalDataSource.getMedias();
        Observable<List<Media>> system = mSystemDataSource.getMedias()
                .doOnNext(new Action1<List<Media>>() {
                    @Override
                    public void call(List<Media> medias) {
                        saveMedias(medias);
                    }
                });
        return Observable.concat(local, system);
    }

    @Override
    public void saveMedias(@NonNull List<Media> medias) {
        if (medias == null) {
            medias = new ArrayList<>();
        }
        mLocalDataSource.saveMedias(medias);
    }

    @Override
    public void saveImages(@NonNull List<Media> medias) {
        if (null == medias) {
            medias = new ArrayList<>();
        }
        mLocalDataSource.saveImages(medias);
    }

    @Override
    public void saveVideos(@NonNull List<Media> medias) {
        if (null == medias) {
            medias = new ArrayList<>();
        }
        mLocalDataSource.saveVideos(medias);
    }
}
