package org.birdhelpline.app.model;

import java.sql.Timestamp;

public class CaseTxn {
    private Long caseTxnId;
    private Long caseId;
    private Long fromUserId;
    private Long toUserId;
    private String fromUser;
    private String fromUserRole;
    private String toUser;
    private String toUserRole;
    private String status;
    private Double amount;
    private Timestamp transferDate;
    private String transferDateStr;
    private int isAck;
    private Timestamp updateTimeStamp;
    private String desc;

    public String getFromUserRole() {
        return fromUserRole;
    }

    public void setFromUserRole(String fromUserRole) {
        this.fromUserRole = fromUserRole;
    }

    public String getToUserRole() {
        return toUserRole;
    }

    public void setToUserRole(String toUserRole) {
        this.toUserRole = toUserRole;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Long getCaseTxnId() {
        return caseTxnId;
    }

    public void setCaseTxnId(Long caseTxnId) {
        this.caseTxnId = caseTxnId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Timestamp transferDate) {
        this.transferDate = transferDate;
    }

    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIsAck() {
        return isAck;
    }

    public void setIsAck(int isAck) {
        this.isAck = isAck;
    }

    public String getTransferDateStr() {
        return transferDateStr;
    }

    public void setTransferDateStr(String transferDateStr) {
        this.transferDateStr = transferDateStr;
    }

    @Override
    public String toString() {
        return "CaseTxn{" +
                "caseTxnId=" + caseTxnId +
                ", caseId=" + caseId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", fromUser='" + fromUser + '\'' +
                ", fromUserRole='" + fromUserRole + '\'' +
                ", toUser='" + toUser + '\'' +
                ", toUserRole='" + toUserRole + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", transferDate=" + transferDate +
                ", transferDateStr='" + transferDateStr + '\'' +
                ", isAck=" + isAck +
                ", updateTimeStamp=" + updateTimeStamp +
                ", desc='" + desc + '\'' +
                '}';
    }
}
