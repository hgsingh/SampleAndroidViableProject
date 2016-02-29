package com.singh.harsukh.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harsukh on 2/29/16.
 */
public class TextRow implements Parcelable {
    String name;
    int type;
    String address;

    public TextRow(String name, int type, String address) {
        this.name = name;
        this.type = type;
        this.address = address;
    }

    protected TextRow(Parcel in) {
        name = in.readString();
        type = in.readInt();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(address);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TextRow> CREATOR = new Parcelable.Creator<TextRow>() {
        @Override
        public TextRow createFromParcel(Parcel in) {
            return new TextRow(in);
        }

        @Override
        public TextRow[] newArray(int size) {
            return new TextRow[size];
        }
    };
}
