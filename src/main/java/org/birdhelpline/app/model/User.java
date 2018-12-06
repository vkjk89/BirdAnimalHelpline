package org.birdhelpline.app.model;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

public class User implements Principal, Serializable {
    private Long userId;
    private String userName;
    private String password;
    private String encryptedPassword;
    private Long mobile;
    private String email;
    private Boolean enabled;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String dob;
    private Timestamp creationDate;
    private Timestamp lastLoginDate;
    private String role;
    private Integer securityQId;
    private String securityQAns;
    private Long caseAcceptedCount;
    private Long caseRejectedCount;
    private UserAddressInfo homeAddr;
    private UserAddressInfo officeAddr;
    private List<UserServiceTimeInfo> userServiceTimeInfos;
    private UserImage userImage = new UserImage();


    public String getEncodedImage() {
        if (userImage != null && userImage.getImage() != null)
            return Base64.encode(userImage.getImage());
        return null;
    }

    public String getOldEncodedImage() {
        if (userImage != null && userImage.getOldImage() != null)
            return Base64.encode(userImage.getOldImage());
        return null;
    }


    @Override
    public String getName() {
        return userName;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getSecurityQId() {
        return securityQId;
    }

    public void setSecurityQId(Integer securityQId) {
        this.securityQId = securityQId;
    }

    public String getSecurityQAns() {
        return securityQAns;
    }

    public void setSecurityQAns(String securityQAns) {
        this.securityQAns = securityQAns;
    }

    public Long getCaseAcceptedCount() {
        return caseAcceptedCount;
    }

    public void setCaseAcceptedCount(Long caseAcceptedCount) {
        this.caseAcceptedCount = caseAcceptedCount;
    }

    public Long getCaseRejectedCount() {
        return caseRejectedCount;
    }

    public void setCaseRejectedCount(Long caseRejectedCount) {
        this.caseRejectedCount = caseRejectedCount;
    }

    public UserAddressInfo getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(UserAddressInfo homeAddr) {
        this.homeAddr = homeAddr;
    }

    public UserAddressInfo getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(UserAddressInfo officeAddr) {
        this.officeAddr = officeAddr;
    }

    public List<UserServiceTimeInfo> getUserServiceTimeInfos() {
        return userServiceTimeInfos;
    }

    public void setUserServiceTimeInfos(List<UserServiceTimeInfo> userServiceTimeInfos) {
        this.userServiceTimeInfos = userServiceTimeInfos;
    }

    public UserImage getUserImage() {
        return userImage;
    }

    public void setUserImage(UserImage userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", mobile=" + mobile +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", creationDate=" + creationDate +
                ", lastLoginDate=" + lastLoginDate +
                ", role='" + role + '\'' +
                ", securityQId=" + securityQId +
                ", securityQAns='" + securityQAns + '\'' +
                ", caseAcceptedCount=" + caseAcceptedCount +
                ", caseRejectedCount=" + caseRejectedCount +
                ", homeAddr=" + homeAddr +
                ", officeAddr=" + officeAddr +
                ", userServiceTimeInfos=" + userServiceTimeInfos +
                ", userImage=" + userImage +
                '}';
    }
}