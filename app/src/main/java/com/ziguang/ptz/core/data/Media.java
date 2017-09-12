package com.ziguang.ptz.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class Media implements Span, Parcelable {

    public static final int TYPE_VIDEO = 2;

    public static final int TYPE_IMAGE = 1;

    public static final int TYPE_BOTH = 3;

    public Media() {}

    private String id;

    private String path;

    private float latitude;

    private float longitude;

    private int mediaType;

    private long date;

    private long duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int getSpan() {
        return SPAN_CONTENT;
    }

    protected Media(Parcel in) {
        id = in.readString();
        path = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        mediaType = in.readInt();
        date = in.readLong();
        duration = in.readLong();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(path);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeInt(mediaType);
        dest.writeLong(date);
        dest.writeLong(duration);
    }


    @Override
    public String toString() {
        return "Media : {" +
                "id = " + id +
                ", path = " + path +
                ", latitude = " + latitude +
                ", longitude = " + longitude +
                ", mediaType = " + (mediaType == TYPE_IMAGE ? "image" : "video") +
                ", date = " + date +
                ", duration = " + duration +
                "}";
    }
}
