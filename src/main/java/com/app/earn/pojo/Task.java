package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;
    private String description;
    private String steps;

    private Double reward;
    private String country;

    private Long partnerId;
    private Long createdBy;

    private String status; // DRAFT, LIVE, CLOSED

    private Date createdAt;

    private Long approvedBy;
    private Date approvedAt;
    
    private String rejectionReason;
    
    private String approvedByName;
    private String url;
    private Integer submissionLimit;

    public Integer getSubmissionLimit() {
        return submissionLimit;
    }

    public void setSubmissionLimit(Integer submissionLimit) {
        this.submissionLimit = submissionLimit;
    }
    
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }
    
	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRejectionReason() {
	    return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
	    this.rejectionReason = rejectionReason;
	}
	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

    // getters and setters
    
    
}
