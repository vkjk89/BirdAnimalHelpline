package org.birdhelpline.app.model;

import org.springframework.web.bind.annotation.ModelAttribute;

public class DonateVO {
    /*id bigint primary key auto_increment,
    name varchar(35),
    address1 varchar(200),
    address2 varchar(200),
    mobile int,
    pan varchar(20),
    amount double,
    donate_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP*/
    Long id;
    String name;
    String address1;
    String address2;
    String mobile;
    String pan;
    double finalAmount;
    String orderId;

    @Override
    public String toString() {
        return "DonateVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", mobile='" + mobile + '\'' +
                ", pan='" + pan + '\'' +
                ", finalAmount=" + finalAmount +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    public DonateVO(Long id, String name, String address1, String address2, String mobile, String pan, double finalAmount, String orderId) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.mobile = mobile;
        this.pan = pan;
        this.finalAmount = finalAmount;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }
}
