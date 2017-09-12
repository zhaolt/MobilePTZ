package com.ziguang.ptz.core.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhaoliangtai on 2017/9/12.
 */

public class Directory implements Parcelable {

    private int id;
    private String name;
    // 日期-以天为单位 list内为同一天内所有的媒体文件
    Map<String, List<Media>> mMedias = new TreeMap<>(Collections.<String>reverseOrder());

    public Directory() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<Media>> getMedias() {
        return mMedias;
    }

    protected Directory(Parcel in) {
        id = in.readInt();
        name = in.readString();
        int meidiasSize = in.readInt();
        this.mMedias = new TreeMap<>(mMedias);
        for (int i = 0; i < meidiasSize; i++) {
            String key = in.readString();
            List<Media> value = in.createTypedArrayList(Media.CREATOR);
            this.mMedias.put(key, value);
        }
    }

    public static final Creator<Directory> CREATOR = new Creator<Directory>() {
        @Override
        public Directory createFromParcel(Parcel in) {
            return new Directory(in);
        }

        @Override
        public Directory[] newArray(int size) {
            return new Directory[size];
        }
    };

    public int getCount() {
        int count = 0;
        for (List<Media> list : mMedias.values()) {
            count += list.size();
        }
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(this.mMedias.size());
        for (Map.Entry<String, List<Media>> entry : mMedias.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeTypedList(entry.getValue());
        }
    }
}
