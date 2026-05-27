package com.app.earn.pojo;

import java.io.Serializable;

public class TaskStats implements Serializable {

private static final long serialVersionUID = 1L;

private Long taskId;

private String taskTitle;

private Integer totalSubmissions;
private Integer accepted;
private Integer rejected;
private Integer pending;


private String status;
private Integer submissionLimit;
private Double reward;
private String partnerName;
private Integer slotsLeft;
private Double completionRate;

private String description;

private String steps;

private String country;

private String url;

/* ---------------- TITLE ---------------- */

public String getTaskTitle() {
    return taskTitle;
}

public void setTaskTitle(String taskTitle) {
    this.taskTitle = taskTitle;
}

/* ---------------- TASK ID ---------------- */

public Long getTaskId() {
    return taskId;
}

public void setTaskId(Long taskId) {
    this.taskId = taskId;
}

/* ---------------- TOTAL ---------------- */

public Integer getTotalSubmissions() {
    return totalSubmissions == null ? 0 : totalSubmissions;
}

public void setTotalSubmissions(Integer totalSubmissions) {
    this.totalSubmissions = totalSubmissions;
}

/* ---------------- ACCEPTED ---------------- */

public Integer getAccepted() {
    return accepted == null ? 0 : accepted;
}

public void setAccepted(Integer accepted) {
    this.accepted = accepted;
}

/* ---------------- REJECTED ---------------- */

public Integer getRejected() {
    return rejected == null ? 0 : rejected;
}

public void setRejected(Integer rejected) {
    this.rejected = rejected;
}

/* ---------------- PENDING ---------------- */

public Integer getPending() {
    return pending == null ? 0 : pending;
}

public void setPending(Integer pending) {
    this.pending = pending;
}
/* ----------- Status ----------*/
public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}
/* ----------- Submition limit ----------*/
public Integer getSubmissionLimit() {
	return submissionLimit;
}

public void setSubmissionLimit(Integer submissionLimit) {
	this.submissionLimit = submissionLimit;
}

public Double getReward() {
	return reward;
}

public void setReward(Double reward) {
	this.reward = reward;
}

public String getPartnerName() {
	return partnerName;
}

public void setPartnerName(String partnerName) {
	this.partnerName = partnerName;
}

public Integer getSlotsLeft() {
	return slotsLeft;
}

public void setSlotsLeft(Integer slotsLeft) {
	this.slotsLeft = slotsLeft;
}

public Double getCompletionRate() {
	return completionRate;
}

public void setCompletionRate(Double completionRate) {
	this.completionRate = completionRate;
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

public String getUrl() {
    return url;
}

public void setUrl(String url) {
    this.url = url;
}


}