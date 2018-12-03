package org.birdhelpline.app.model;

public class UserServiceTimeInfo {
    private long pincodeId;
    private int fromTime;
    private int toTime;

    @Override
    public String toString() {
        return "UserServiceTimeInfo{" +
                "pincodeId=" + pincodeId +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }

    public long getPincodeId() {
        return pincodeId;
    }

    public void setPincodeId(long pincodeId) {
        this.pincodeId = pincodeId;
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
}
