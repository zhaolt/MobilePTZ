package com.ziguang.ptz.core.database;

import android.provider.BaseColumns;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public final class PersistenceContract {

    public interface AlbumEntry extends BaseColumns {
        String TABLE_NAME = "album";
        String MEDIA_TYPE = "media_type";
        String DATA = "data";
    }

}
