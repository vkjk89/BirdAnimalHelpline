package org.birdhelpline.app.model;

import java.sql.Timestamp;

public class CaseTxn {
    private Long caseTxnId;
    private Long caseId;
    private Long fromUserId;
    private Long toUserId;
    private String status;
    private Double amount;
    private Timestamp transferDate;
    private boolean acked;
    private Timestamp updateTimeStamp;
    private String desc;

    @Override
    public String toString() {
        return "CaseTxn{" +
                "caseTxnId=" + caseTxnId +
                ", caseId=" + caseId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", transferDate=" + transferDate +
                ", acked=" + acked +
                ", updateTimeStamp=" + updateTimeStamp +
                ", desc='" + desc + '\'' +
                '}';
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

    public boolean isAcked() {
        return acked;
    }

    public void setAcked(boolean acked) {
        this.acked = acked;
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
}
