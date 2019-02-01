package org.birdhelpline.app.model;

public class CaseUpdateVO {
    private Long caseId;
    private String caseUpdateDate;
    private Long assignedUserId;
    private String caseCloseReason;
    private String caseCloseReasonOther;
    private String caseSummary;
    private Double chargesIncurred;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseUpdateDate() {
        return caseUpdateDate;
    }

    public void setCaseUpdateDate(String caseUpdateDate) {
        this.caseUpdateDate = caseUpdateDate;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getCaseCloseReason() {
        return caseCloseReason;
    }

    public void setCaseCloseReason(String caseCloseReason) {
        this.caseCloseReason = caseCloseReason;
    }

    public String getCaseCloseReasonOther() {
        return caseCloseReasonOther;
    }

    public void setCaseCloseReasonOther(String caseCloseReasonOther) {
        this.caseCloseReasonOther = caseCloseReasonOther;
    }

    public String getCaseSummary() {
        return caseSummary;
    }

    public void setCaseSummary(String caseSummary) {
        this.caseSummary = caseSummary;
    }

    public Double getChargesIncurred() {
        return chargesIncurred;
    }

    public void setChargesIncurred(Double chargesIncurred) {
        this.chargesIncurred = chargesIncurred;
    }

    @Override
    public String toString() {
        return "CaseUpdateVO{" +
                "caseId=" + caseId +
                ", caseUpdateDate='" + caseUpdateDate + '\'' +
                ", assignedUserId=" + assignedUserId +
                ", caseCloseReason='" + caseCloseReason + '\'' +
                ", caseCloseReasonOther='" + caseCloseReasonOther + '\'' +
                ", caseSummary='" + caseSummary + '\'' +
                ", chargesIncurred=" + chargesIncurred +
                ", action='" + action + '\'' +
                '}';
    }
}
