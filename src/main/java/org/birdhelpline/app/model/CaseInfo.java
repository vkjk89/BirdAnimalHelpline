package org.birdhelpline.app.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CaseInfo {

    private Long caseId;
    private Long userIdOpened;
    private Timestamp creationDate;
    private String creationDateStr;
    private Timestamp lastModificationDate;
    private Timestamp closeDate;
    private String desc;
    private Long userIdClosed;
    private String closeRemark;
    private String typeAnimal;
    private String birdOrAnimal;
    private String newBirdAnimal;
    private Long currentUserId;
    private boolean active;
    private String animalName;
    private String animalCondition;
    private String contactName;
    private String contactPrefix;
    private String contactNumber;
    private String location;
    private String locationPincode;
    private String locationLandMark;
    private String userNameOpened;
    private String userRoleOpened;
    private String userNameClosed;
    private String userRoleClosed;
    private String userNameCurrent;
    private String userRoleCurrent;
    private int isAck;

    private List<CaseTxn> caseTxnList = new ArrayList<>();

    public String getBirdOrAnimal() {
        return birdOrAnimal;
    }

    public void setBirdOrAnimal(String birdOrAnimal) {
        this.birdOrAnimal = birdOrAnimal;
    }

    public String getNewBirdAnimal() {
        return newBirdAnimal;
    }

    public void setNewBirdAnimal(String newBirdAnimal) {
        this.newBirdAnimal = newBirdAnimal;
    }

    public String getUserRoleOpened() {
        return userRoleOpened;
    }

    public void setUserRoleOpened(String userRoleOpened) {
        this.userRoleOpened = userRoleOpened;
    }

    public String getUserRoleClosed() {
        return userRoleClosed;
    }

    public void setUserRoleClosed(String userRoleClosed) {
        this.userRoleClosed = userRoleClosed;
    }

    public String getUserRoleCurrent() {
        return userRoleCurrent;
    }

    public void setUserRoleCurrent(String userRoleCurrent) {
        this.userRoleCurrent = userRoleCurrent;
    }

    public String getUserNameOpened() {
        return userNameOpened;
    }

    public void setUserNameOpened(String userNameOpened) {
        this.userNameOpened = userNameOpened;
    }

    public String getUserNameClosed() {
        return userNameClosed;
    }

    public void setUserNameClosed(String userNameClosed) {
        this.userNameClosed = userNameClosed;
    }

    public String getUserNameCurrent() {
        return userNameCurrent;
    }

    public void setUserNameCurrent(String userNameCurrent) {
        this.userNameCurrent = userNameCurrent;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAnimalCondition() {
        return animalCondition;
    }

    public void setAnimalCondition(String animalCondition) {
        this.animalCondition = animalCondition;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationPincode() {
        return locationPincode;
    }

    public void setLocationPincode(String locationPincode) {
        this.locationPincode = locationPincode;
    }

    public List<CaseTxn> getCaseTxnList() {
        return caseTxnList;
    }

    public void setCaseTxnList(List<CaseTxn> caseTxnList) {
        this.caseTxnList = caseTxnList;
    }

    public void addCaseTxnList(CaseTxn caseTxn) {
        this.caseTxnList.add(caseTxn);
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getUserIdOpened() {
        return userIdOpened;
    }

    public void setUserIdOpened(Long userIdOpened) {
        this.userIdOpened = userIdOpened;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Timestamp lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getUserIdClosed() {
        return userIdClosed;
    }

    public void setUserIdClosed(Long userIdClosed) {
        this.userIdClosed = userIdClosed;
    }

    public String getCloseRemark() {
        return closeRemark;
    }

    public void setCloseRemark(String closeRemark) {
        this.closeRemark = closeRemark;
    }

    public String getTypeAnimal() {
        return typeAnimal;
    }

    public void setTypeAnimal(String typeAnimal) {
        this.typeAnimal = typeAnimal;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getContactPrefix() {
        return contactPrefix;
    }

    public void setContactPrefix(String contactPrefix) {
        this.contactPrefix = contactPrefix;
    }

    public String getLocationLandMark() {
        return locationLandMark;
    }

    public void setLocationLandMark(String locationLandMark) {
        this.locationLandMark = locationLandMark;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public int getIsAck() {
        return isAck;
    }

    public void setIsAck(int isAck) {
        this.isAck = isAck;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", userIdOpened=" + userIdOpened +
                ", creationDate=" + creationDate +
                ", creationDateStr='" + creationDateStr + '\'' +
                ", lastModificationDate=" + lastModificationDate +
                ", closeDate=" + closeDate +
                ", desc='" + desc + '\'' +
                ", userIdClosed=" + userIdClosed +
                ", closeRemark='" + closeRemark + '\'' +
                ", typeAnimal='" + typeAnimal + '\'' +
                ", birdOrAnimal='" + birdOrAnimal + '\'' +
                ", newBirdAnimal='" + newBirdAnimal + '\'' +
                ", currentUserId=" + currentUserId +
                ", active=" + active +
                ", animalName='" + animalName + '\'' +
                ", animalCondition='" + animalCondition + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPrefix='" + contactPrefix + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", location='" + location + '\'' +
                ", locationPincode='" + locationPincode + '\'' +
                ", locationLandMark='" + locationLandMark + '\'' +
                ", userNameOpened='" + userNameOpened + '\'' +
                ", userRoleOpened='" + userRoleOpened + '\'' +
                ", userNameClosed='" + userNameClosed + '\'' +
                ", userRoleClosed='" + userRoleClosed + '\'' +
                ", userNameCurrent='" + userNameCurrent + '\'' +
                ", userRoleCurrent='" + userRoleCurrent + '\'' +
                ", isAck=" + isAck +
                ", caseTxnList=" + caseTxnList +
                '}';
    }
}
