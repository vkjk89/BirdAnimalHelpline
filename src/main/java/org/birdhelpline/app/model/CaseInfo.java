package org.birdhelpline.app.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CaseInfo {

    private Long caseId;
    private Long userIdOpened;
    private Timestamp creationDate;
    private Timestamp lastModificationDate;
    private Timestamp closeDate;
    private String desc;
    private Long userIdClosed;
    private String closeRemark;
    private String typeAnimal;
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

    private List<CaseTxn> caseTxnList = new ArrayList<>();

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", userIdOpened=" + userIdOpened +
                ", creationDate=" + creationDate +
                ", lastModificationDate=" + lastModificationDate +
                ", closeDate=" + closeDate +
                ", desc='" + desc + '\'' +
                ", userIdClosed=" + userIdClosed +
                ", closeRemark='" + closeRemark + '\'' +
                ", typeAnimal='" + typeAnimal + '\'' +
                ", currentUserId=" + currentUserId +
                ", active=" + active +
                ", animalName='" + animalName + '\'' +
                ", animalCondition='" + animalCondition + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", location='" + location + '\'' +
                ", locationPincode='" + locationPincode + '\'' +
                ", caseTxnList=" + caseTxnList +
                '}';
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
}
