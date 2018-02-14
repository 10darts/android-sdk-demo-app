package com.auroralabs.tendarts.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.auroralabs.tendarts.app.helper.DateFormatter;

import java.util.Date;

/**
 * Created by luismacb on 12/2/18.
 */

public class LogEntity implements Parcelable {

    private static final String LOG_TAG = LogEntity.class.getSimpleName();

    private String category;
    private String type;
    private String message;
    private Date date;

    public LogEntity(String category, String type, String message) {
        this.category = category;
        this.type = type;
        this.message = message;
        this.date = new Date();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLogDate() {
        if (getDate() != null) {
            return DateFormatter.getThreadInstance().getLogDateFormat().format(getDate());
        }
        return "";
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
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected LogEntity(Parcel in) {
        this.category = in.readString();
        this.type = in.readString();
        this.message = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<LogEntity> CREATOR = new Creator<LogEntity>() {
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
