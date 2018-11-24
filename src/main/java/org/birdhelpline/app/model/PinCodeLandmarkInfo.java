package org.birdhelpline.app.model;

public class PinCodeLandmarkInfo {
    long pincodeId;
    long pincode;
    String landmark;


    public PinCodeLandmarkInfo(long pincodeId, long pincode, String landmark) {
        this.pincodeId = pincodeId;
        this.pincode = pincode;
        this.landmark = landmark;
    }

    @Override
    public String toString() {
        return "PinCodeLandmarkInfo{" +
                "pincodeId=" + pincodeId +
                ", pincode=" + pincode +
                ", landmark='" + landmark + '\'' +
                '}';
    }

    public long getPincodeId() {
        return pincodeId;
    }

    public void setPincodeId(long pincodeId) {
        this.pincodeId = pincodeId;
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
