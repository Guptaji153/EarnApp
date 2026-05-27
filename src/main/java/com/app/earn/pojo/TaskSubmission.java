package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class TaskSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long taskId;
    private Long userId;

    private String userName;      // NEW
    private String adminName;     // NEW

    private String proofPath;

    private String status; // PENDING, ACCEPTED, REJECTED
    private String rejectionReason;

    private Date submittedAt;

    private Long verifiedBy;
    private Date verifiedAt;
    
    private String proofDescription;
    
 // UI fields (NOT DB columns)
    private String taskTitle;
    private Double reward;
    private String taskUrl;
    private String description;
    
    
    private Integer submissionLimit;

    public Integer getSubmissionLimit() {
        return submissionLimit;
    }

    public void setSubmissionLimit(Integer submissionLimit) {
        this.submissionLimit = submissionLimit;
    }
    
  

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }
    
    public String getProofDescription() {
    	return proofDescription;
    	}

    	public void setProofDescription(String proofDescription) {
    	this.proofDescription = proofDescription;
    	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getProofPath() {
        return proofPath;
    }

    public void setProofPath(String proofPath) {
        this.proofPath = proofPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Long getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Date verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}