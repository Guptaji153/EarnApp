package com.app.earn.pojo;

import java.io.Serializable;
import java.util.Date;

public class TaskRequest implements Serializable {
    private static final long serialVersionUID = 1L;

private Long id;

private String title;
private String description;
private String steps;

private String country;

private Long partnerId;

private Double requestedReward;

private String notes;

private String status;

private Date createdAt;

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

public Double getRequestedReward() {
	return requestedReward;
}

public void setRequestedReward(Double requestedReward) {
	this.requestedReward = requestedReward;
}

public String getNotes() {
	return notes;
}

public void setNotes(String notes) {
	this.notes = notes;
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

public TaskRequest() {
	super();
	// TODO Auto-generated constructor stub
}



}