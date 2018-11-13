package org.birdhelpline.app.model;

import java.sql.Timestamp;

public class UserAddressInfo {
    private long userId;
    private String fullName;
    private String addrLine1;
    private String addrLine2;
    private long pincode;
    private String contactPrefix;
    private String contact;
    private String addressType;
    private String natureBusiness;
    private Timestamp lastUpdateTimestamp;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getAddrLine2() {
        return addrLine2;
    }

    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getAlternateContact() {
        return contactPrefix+"-"+contact;
    }


    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getNatureBusiness() {
        return natureBusiness;
    }

    public void setNatureBusiness(String natureBusiness) {
        this.natureBusiness = natureBusiness;
    }

    public Timestamp getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getContactPrefix() {
        return contactPrefix;
    }

    public void setContactPrefix(String contactPrefix) {
        this.contactPrefix = contactPrefix;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "UserAddressInfo{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", addrLine1='" + addrLine1 + '\'' +
                ", addrLine2='" + addrLine2 + '\'' +
                ", pincode=" + pincode +
                ", contactPrefix='" + contactPrefix + '\'' +
                ", contact='" + contact + '\'' +
                ", addressType='" + addressType + '\'' +
                ", natureBusiness='" + natureBusiness + '\'' +
                ", lastUpdateTimestamp=" + lastUpdateTimestamp +
                '}';
    }
}
