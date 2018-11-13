package org.birdhelpline.app.model;

import java.sql.Timestamp;

public class UserServiceTimeInfo {
    private long userId;
    private long pincode;
    private int fromTime;
    private int toTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "UserServiceTimeInfo{" +
                "userId=" + userId +
                ", pincode=" + pincode +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
