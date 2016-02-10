package com.singh.harsukh.finalproject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harsukh on 2/4/16.
 */
public class SingleRow implements Parcelable {
    String title;
    String description;
    Bitmap thumb;
    String id;

    public SingleRow(String title, String desc, Bitmap thumb){
        this.title = title;
        this.description = desc;
        this.thumb = thumb;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    protected SingleRow(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumb = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeValue(thumb);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SingleRow> CREATOR = new Parcelable.Creator<SingleRow>() {
        @Override
        public SingleRow createFromParcel(Parcel in) {
            return new SingleRow(in);
        }

        @Override
        public SingleRow[] newArray(int size) {
            return new SingleRow[size];
        }
    };
}