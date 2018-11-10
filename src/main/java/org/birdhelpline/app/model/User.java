package org.birdhelpline.app.model;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

//@Entity
//@Table(name = "user")
public class User implements Principal, Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="user_id")
    private long userId;
    //@Column(name="user_name")
    private String userName;
    //@Column(name="password")
    private String password;
    private String encryptedPassword;
    //@Column(name="mobile")
    private Long mobile;
   // @Column(name="email")
    private String email;
   // @Column(name="enabled")
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String dob;
    private Timestamp creationDate;
    private Timestamp lastLoginDate;
    private String role;
    private int securityQId;
    private String securityQAns;
    private byte[] image;
    private UserAddressInfo homeAddr;
    private UserAddressInfo officeAddr;
    private List<UserServiceTimeInfo> userServiceTimeInfos;

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
                ", image=" + (image == null ? "empty" : "nonEmpty") +
                ", homeAddr=" + homeAddr +
                ", officeAddr=" + officeAddr +
                ", userServiceTimeInfos=" + userServiceTimeInfos +
                '}';
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

    public int getSecurityQId() {
        return securityQId;
    }

    public void setSecurityQId(int securityQId) {
        this.securityQId = securityQId;
    }

    public String getSecurityQAns() {
        return securityQAns;
    }

    public void setSecurityQAns(String securityQAns) {
        this.securityQAns = securityQAns;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public byte[] getImage() {
        return image;
    }

    public String getEncodedImage() {
        return Base64.encode(image);
   }

    public void setImage(byte[] image) {
        this.image = image;
    }

   public Long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
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

}