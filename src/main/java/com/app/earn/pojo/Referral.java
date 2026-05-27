package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class Referral implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long referrerId;
    private Long referredUserId;

    private String status; // SIGNED_UP, TASK_COMPLETED, PAID

    private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReferrerId() {
		return referrerId;
	}

	public void setReferrerId(Long referrerId) {
		this.referrerId = referrerId;
	}

	public Long getReferredUserId() {
		return referredUserId;
	}

	public void setReferredUserId(Long referredUserId) {
		this.referredUserId = referredUserId;
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

	public Referral() {
		super();
		// TODO Auto-generated constructor stub
	}

    // getters and setters
}
