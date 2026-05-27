package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserPaymentDetails implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;
    private Long paymentOptionId;

    private String paymentValue;

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

	public Long getPaymentOptionId() {
		return paymentOptionId;
	}

	public void setPaymentOptionId(Long paymentOptionId) {
		this.paymentOptionId = paymentOptionId;
	}

	public String getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(String paymentValue) {
		this.paymentValue = paymentValue;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserPaymentDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

    // getters and setters
}
