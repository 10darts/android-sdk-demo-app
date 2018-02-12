package com.auroralabs.tendarts.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismacb on 12/2/18.
 */

public class LogEntity implements Parcelable {

    private String category;
    private String type;
    private String message;

    public LogEntity(String category, String type, String message) {
        this.category = category;
        this.type = type;
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.type);
        dest.writeString(this.message);
    }

    public LogEntity() {
    }

    protected LogEntity(Parcel in) {
        this.category = in.readString();
        this.type = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<LogEntity> CREATOR = new Parcelable.Creator<LogEntity>() {
        @Override
        public LogEntity createFromParcel(Parcel source) {
            return new LogEntity(source);
        }

        @Override
        public LogEntity[] newArray(int size) {
            return new LogEntity[size];
        }
    };
}
