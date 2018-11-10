package org.birdhelpline.app.model;

public class PinCodeLandmarkInfo {
    long pincode;
    String landmark;

    public PinCodeLandmarkInfo(long pinCode, String landMark) {
        this.pincode=pinCode;
        this.landmark=landMark;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
