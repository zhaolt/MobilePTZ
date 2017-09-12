package com.ziguang.ptz.core.data;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public interface MediaDataSource {

    Observable<Directory> getImages();

    Observable<Directory> getVideos();

    Observable<Directory> getMedias();

    void saveMedias(@NonNull Directory directory);

    void saveImages(@NonNull Directory directory);

    void saveVideos(@NonNull Directory directory);
}
