package com.jbielak.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Justyna on 2018-03-10.
 */

public class Video implements Parcelable {

    private String key;
    private String name;
    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.key);
        parcel.writeString(this.name);
        parcel.writeString(this.type);
    }

    private Video(Parcel in) {
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public Video() {
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
