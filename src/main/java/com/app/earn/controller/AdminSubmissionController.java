package com.app.earn.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.pojo.User;
import com.app.earn.service.TaskSubmissionService;

public class AdminSubmissionController implements Serializable{

	 private static final long serialVersionUID = 1L;
	// task submited by users and review 
    private TaskSubmissionService taskSubmissionService;
	
	 public TaskSubmissionService getTaskSubmissionService() {
			return taskSubmissionService;
		}


    private List<TaskSubmission> submissions;

    private Long taskId;
    private TaskSubmission selectedSubmission;
    private Long submissionId;
    
    
    
    private int page = 0;

    private int pageSize = 5;

    private String searchText;

    private String searchType = "contains";

    private String sortField = "submittedAt";

    private String sortOrder = "DESC";
    
    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }
    public List<TaskSubmission> getPendingSubmissions(){

        if(submissions == null){
            submissions = taskSubmissionService.getPendingSubmissions();
        }

        return submissions;
    }


    public void approve(Long id){

        try{

            User admin = (User) FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .get("loggedUser");

            taskSubmissionService.approveSubmission(id, admin.getId());

            submissions = null;

            FacesContext.getCurrentInstance().addMessage(
            null,new FacesMessage("Submission approved"));

        }catch(Exception e){

            FacesContext.getCurrentInstance().addMessage(
            null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null));
        }
    }


    public void reject(Long id, String reason){

        try{

            User admin = (User) FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .get("loggedUser");

            taskSubmissionService.rejectSubmission(id, admin.getId(), reason);

            submissions = null;

            FacesContext.getCurrentInstance().addMessage(
            null,new FacesMessage("Submission rejected"));

        }catch(Exception e){

            FacesContext.getCurrentInstance().addMessage(
            null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null));
        }
    }
    
    public void loadSubmissionDetails(){

        if(taskId == null) return;

        for(TaskSubmission s : getPendingSubmissions()){
            if(s.getTaskId().equals(taskId)){
                selectedSubmission = s;
                break;
            }
        }
    }


    public void setTaskSubmissionService(TaskSubmissionService taskSubmissionService){
        this.taskSubmissionService = taskSubmissionService;
    }
    
    private List<TaskSubmission> allSubmissions;

    public List<TaskSubmission> getAllSubmissions(){

        if(allSubmissions == null){

            allSubmissions =
            taskSubmissionService.getAllSubmissions(
                page,
                pageSize,
                searchText,
                searchType,
                sortField,
                sortOrder
            );
        }

        return allSubmissions;
    }
    
    public void searchSubmissions(){

        page = 0;

        allSubmissions = null;
    }
    
    public void sortBy(String field){

        if(field.equals(sortField)){

            if("ASC".equals(sortOrder)){
                sortOrder = "DESC";
            }
            else{
                sortOrder = "ASC";
            }

        }else{

            sortField = field;
            sortOrder = "ASC";
        }

        allSubmissions = null;
    }
    
    public void nextPage(){

        if(page + 1 < getTotalPages()){

            page++;

            allSubmissions = null;
        }
    }

    public void previousPage(){

        if(page > 0){

            page--;

            allSubmissions = null;
        }
    }
    
    public int getTotalPages(){

        int total =
        taskSubmissionService.getTotalSubmissions(
            searchText,
            searchType
        );

        return (int)Math.ceil(
            (double) total / pageSize
        );
    }
    
    public int getTotalRecords(){

        return taskSubmissionService.getTotalSubmissions(
                searchText,
                searchType
        );
    }
    //today how much task completed by user 
    
    public int getTodayPending(){

        return taskSubmissionService.getTodayPendingSubmissions();

    }
    
    // tasks approved by admin 
    public double getAcceptanceRate(){

        return taskSubmissionService.getAcceptanceRate();

    }
    
    public List<TaskStats> getTopCampaigns(){
    	return taskSubmissionService.getTopCampaigns();
    }
    
    // users tasks pending notifications on admin dashboard
    public int getNotificationCount(){

    	return taskSubmissionService.getPendingSubmissionCount();

    	}
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskSubmission getSelectedSubmission() {
        return selectedSubmission;
    }
    
    
    public void loadHistorySubmissionDetails(){

        if(submissionId == null) return;

        selectedSubmission =
            taskSubmissionService.getSubmissionFullDetails(submissionId);
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
    
}
