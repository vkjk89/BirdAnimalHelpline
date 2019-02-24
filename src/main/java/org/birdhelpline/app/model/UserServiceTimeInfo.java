package org.birdhelpline.app.model;

public class UserServiceTimeInfo {

    private long pincodeId;
    private long pincode;
    private String area;
    private int fromTime;
    private int toTime;

    public int timeForCompare() {
        return Integer.valueOf(fromTime + "" + toTime);
    }

    public String getTimeStr() {
        StringBuilder sb = new StringBuilder();
        if (fromTime == 0 && toTime == 24) {
            return "12 AM - 12 AM";
        }
        if (fromTime < 12) {
            sb.append(fromTime + " AM - ");
        } else if (fromTime == 12) {
            sb.append("12 PM - ");
        } else {
            sb.append(fromTime % 12 + " PM - ");
        }
        if (toTime < 12) {
            sb.append(toTime + " AM");
        } else if (toTime == 12) {
            sb.append("12 PM");
        } else if (toTime == 24) {
            sb.append("12 AM");
        } else {
            sb.append(toTime % 12 + " PM");
        }
        return sb.toString();
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
