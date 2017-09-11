package com.ziguang.ptz.core.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public interface MediaDataSource {

    Observable<List<Media>> getImages();

    Observable<List<Media>> getVideos();

    Observable<List<Media>> getMedias();

    void save(@NonNull List<Media> medias);

}
