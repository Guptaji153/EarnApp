package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserOtp implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;
    private String email;
    private String mobile;

    private String otpValue;
    private String purpose;   // SIGNUP, LOGIN, RESET_PASSWORD

    private Date expiresAt;
    private Boolean isVerified;

    private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserOtp() {
		super();
		// TODO Auto-generated constructor stub
	}

    // getters and setters
    
    
}
