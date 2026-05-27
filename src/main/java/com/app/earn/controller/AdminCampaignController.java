package com.app.earn.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.TaskStats;
import com.app.earn.service.AdminDashboardSearvice;

public class AdminCampaignController {

	private AdminDashboardSearvice adminDashboardSearvice;

	private TaskStats selectedCampaign;

	private Integer limitChange;

	/*
	 * ----------------------------- DATA -----------------------------
	 */

	private List<TaskStats> campaigns;

	private int page = 0;

	private int pageSize = 10;

	private int totalRecords;

	private int totalPages;

	/*
	 * ----------------------------- SEARCH -----------------------------
	 */

	private String searchText;

	private String searchType = "contains";

	/*
	 * ----------------------------- SORT -----------------------------
	 */

	private String sortField = "taskId";

	private String sortOrder = "DESC";

	/*
	 * ----------------------------- SELECTED -----------------------------
	 */

	private Long taskId;

	/*
	 * ----------------------------- LOAD -----------------------------
	 */

	public void loadCampaigns() {

		campaigns = adminDashboardSearvice.getAllCampaignStats(page, pageSize, searchText, searchType, sortField,
				sortOrder);

		totalRecords = adminDashboardSearvice.getTotalCampaignStats(searchText, searchType);

		totalPages = (int) Math.ceil((double) totalRecords / pageSize);
	}

	/*
	 * ----------------------------- PAGINATION -----------------------------
	 */

	public void nextPage() {

		if (page + 1 < totalPages) {

			page++;

			loadCampaigns();
		}
	}

	public void previousPage() {

		if (page > 0) {

			page--;

			loadCampaigns();
		}
	}

	public void goToPage(int p) {

		page = p;

		loadCampaigns();
	}

	/*
	 * ----------------------------- SEARCH -----------------------------
	 */

	public void search() {

		page = 0;

		loadCampaigns();
	}

	/*
	 * ----------------------------- SORT -----------------------------
	 */

	public void sortBy(String field) {

		if (field.equals(sortField)) {

			if ("ASC".equals(sortOrder)) {
				sortOrder = "DESC";
			} else {
				sortOrder = "ASC";
			}

		} else {

			sortField = field;

			sortOrder = "ASC";
		}

		loadCampaigns();
	}

	/*
	 * -------- next page feature
	 * 
	 * public void loadCampaignDetails() {
	 * 
	 * selectedCampaign = adminDashboardSearvice.getCampaignDetails(taskId);
	 * 
	 * }
	 */

	/*
	 * ----------------------------- DETAILS -----------------------------
	 */

	public void loadCampaignDetails() {

		selectedCampaign = adminDashboardSearvice.getCampaignDetails(taskId);
	}

	/*
	 * ----------------------------- LIMIT MANAGEMENT -----------------------------
	 */

	public void increaseLimit() {

		if (limitChange == null || limitChange <= 0) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid increase amount", null));

			return;
		}

		

		int newLimit = selectedCampaign.getSubmissionLimit() + limitChange;

		adminDashboardSearvice.updateCampaignLimit(taskId, newLimit);
		
		/* AUTO REOPEN */

		int active =
		selectedCampaign.getAccepted()
		+
		selectedCampaign.getPending();

		if(active < newLimit &&
		"CLOSED".equals(
		selectedCampaign.getStatus())){

		adminDashboardSearvice.reopenCampaign(taskId);
		}

		/* SUCCESS */

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Campaign limit increased successfully", null));

		/* RESET INPUT */

		limitChange = null;

		loadCampaignDetails();
	}

	public void decreaseLimit() {

		if (limitChange == null || limitChange <= 0) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid decrease amount", null));

			return;
		}

		/* CLOSED VALIDATION */

		if ("CLOSED".equals(selectedCampaign.getStatus())) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reopen campaign before modifying limit", null));

			return;
		}

		int newLimit = selectedCampaign.getSubmissionLimit() - limitChange;

		try {

			adminDashboardSearvice.updateCampaignLimit(taskId, newLimit);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Campaign limit decreased successfully", null));

			/* RESET */

			limitChange = null;

			loadCampaignDetails();

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}

	/*
	 * ----------------------------- STATUS -----------------------------
	 */

	public void closeCampaign() {

		adminDashboardSearvice.closeCampaign(taskId);

		loadCampaignDetails();
	}

	public void reopenCampaign() {

		int active = selectedCampaign.getAccepted() + selectedCampaign.getPending();

		if (active >= selectedCampaign.getSubmissionLimit()) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Increase submission limit before reopening", null));

			return;
		}
		
		if("REJECTED".equals(
				selectedCampaign.getStatus())){

				FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(
				FacesMessage.SEVERITY_ERROR,
				"Rejected campaign cannot be reopened",
				null
				)
				);

				return;
				}

		adminDashboardSearvice.reopenCampaign(taskId);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Campaign reopened successfully", null));

		loadCampaignDetails();
	}
	/*
	 * ----------------------------- GETTERS / SETTERS -----------------------------
	 */

	public AdminDashboardSearvice getAdminDashboardSearvice() {
		return adminDashboardSearvice;
	}

	public void setAdminDashboardSearvice(AdminDashboardSearvice adminDashboardSearvice) {

		this.adminDashboardSearvice = adminDashboardSearvice;
	}

	public List<TaskStats> getCampaigns() {

		if (campaigns == null) {
			loadCampaigns();
		}

		return campaigns;
	}

	public void setCampaigns(List<TaskStats> campaigns) {
		this.campaigns = campaigns;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public TaskStats getSelectedCampaign() {
		return selectedCampaign;
	}

	public void setSelectedCampaign(TaskStats selectedCampaign) {
		this.selectedCampaign = selectedCampaign;
	}

	public Integer getLimitChange() {
		return limitChange;
	}

	public void setLimitChange(Integer limitChange) {
		this.limitChange = limitChange;
	}

}