package com.app.earn.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.Task;
import com.app.earn.pojo.User;
import com.app.earn.service.TaskService;

public class TaskController implements Serializable {
	private static final long serialVersionUID = 1L;

	private TaskService taskService;

	private Task task = new Task();
	private boolean showCreateTask;
	private List<Task> myTasks;

//	public List<Task> getMyTasks() {
//
//		User partner = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedUser");
//
//		if (myTasks == null) {
//
//			myTasks = taskService.getTasksByPartner(partner.getId());
//
//		}
//
//		return myTasks;
//
//	}

	public boolean isShowCreateTask() {
		return showCreateTask;
	}

	public void openCreateTask() {
		showCreateTask = true;
	}

	public void closeCreateTask() {
		showCreateTask = false;
	}

	public String createTask(){

	    if(task.getTitle() == null || task.getTitle().trim().isEmpty()){
	        FacesContext.getCurrentInstance()
	            .addMessage(null,new FacesMessage("Title is required"));
	        return null;
	    }

	    if(task.getDescription() == null || task.getDescription().length() < 10){
	        FacesContext.getCurrentInstance()
	            .addMessage(null,new FacesMessage("Description too short"));
	        return null;
	    }

	    if(task.getCountry() == null){
	        FacesContext.getCurrentInstance()
	            .addMessage(null,new FacesMessage("Country required"));
	        return null;
	    }

	    User partner = (User) FacesContext
	        .getCurrentInstance()
	        .getExternalContext()
	        .getSessionMap()
	        .get("loggedUser");

	    task.setPartnerId(partner.getId());
	    task.setCreatedBy(partner.getId());
	    task.setStatus("DRAFT");
	    task.setCreatedAt(new Date());

	    taskService.saveTask(task);

	    return "/partner/partnerDashboard.xhtml?faces-redirect=true";
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	private Task selectedTask;

	public String editTask(Task task) {

		selectedTask = task;

		return "/partner/editCampaign.xhtml?faces-redirect=true";

	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}
	
	
	public String updateTask(){

		if(selectedTask.getStatus().equals("REJECTED")){

		selectedTask.setStatus("DRAFT");

		selectedTask.setRejectionReason(null);

		}

		taskService.updateTask(selectedTask);

		FacesContext.getCurrentInstance()
		.addMessage(null,new FacesMessage("Campaign Updated"));

		return "/partner/myCampaigns.xhtml?faces-redirect=true";

		}

	public String openTask(Long id){

		selectedTask = taskService.getTaskById(id);

		return "/partner/editCampaign.xhtml";

		}
	
	private Long taskId;
	public Long getTaskId() {
	    return taskId;
	}

	public void setTaskId(Long taskId) {
	    this.taskId = taskId;
	}
	
	public void loadTask(){

		if(taskId != null){

		selectedTask = taskService.getTaskById(taskId);

		}

		}
	
//	public int getTotalCampaigns(){
//
//		User partner =
//		(User) FacesContext.getCurrentInstance()
//		.getExternalContext()
//		.getSessionMap()
//		.get("loggedUser");
//
//		return taskService.getTotalTasksByPartner(partner.getId());
//		}
	
	public int getTotalCampaigns(){

		User partner =
		(User) FacesContext.getCurrentInstance()
		.getExternalContext()
		.getSessionMap()
		.get("loggedUser");

		return taskService.getTotalTasksByPartner(
		partner.getId(),
		null,
		null
		);

		}
	public int getLiveCampaigns(){

		User partner =
		(User) FacesContext.getCurrentInstance()
		.getExternalContext()
		.getSessionMap()
		.get("loggedUser");

		return taskService.getTasksByStatus(partner.getId(),"LIVE");
		}
	
	public int getDraftCampaigns(){

		User partner =
		(User) FacesContext.getCurrentInstance()
		.getExternalContext()
		.getSessionMap()
		.get("loggedUser");

		return taskService.getTasksByStatus(partner.getId(),"DRAFT");
		}
	
	public int getRejectedCampaigns(){

		User partner =
		(User) FacesContext.getCurrentInstance()
		.getExternalContext()
		.getSessionMap()
		.get("loggedUser");

		return taskService.getTasksByStatus(partner.getId(),"REJECTED");

		}
	public int getUsersCompleted(){

		return 0;

		}
	
	// pagination sorting searching 
	
	private int page = 0;
	private int pageSize = 5;

	private int totalRecords;

	private String searchText;
	private String searchType = "contains"; // contains or starts

	//private String sortField = "created_at";
	
	private String sortField = "createdAt";
	private String sortOrder = "DESC";
	
	// geter seter 
	
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
	
//	public List<Task> getMyTasks(){
//
//		User partner = (User) FacesContext
//		.getCurrentInstance()
//		.getExternalContext()
//		.getSessionMap()
//		.get("loggedUser");
//
//		myTasks = taskService.getTasksByPartner(
//		partner.getId(),
//		page,
//		pageSize,
//		searchText,
//		searchType,
//		sortField,
//		sortOrder
//		);
//
//		totalRecords = taskService.getTotalTasksByPartner(
//		partner.getId(),
//		searchText,
//		searchType
//		);
//
//		return myTasks;
//		}
	
	public List<Task> getMyTasks(){

	    if(myTasks == null){
	        loadTasks();
	    }

	    return myTasks;
	}
	
//	public void nextPage(){
//
//		if((page+1)*pageSize < totalRecords){
//
//		page++;
//
//		}
//
//		}
	
	public void nextPage(){

	    if((page+1)*pageSize < totalRecords){
	        page++;
	        loadTasks();
	    }

	}

//		public void prevPage(){
//
//		if(page>0){
//
//		page--;
//
//		}
//
//		}
	
	public void prevPage(){

	    if(page>0){
	        page--;
	        loadTasks();
	    }

	}
		
//		public void sort(String field){
//
//			if(sortField.equals(field)){
//
//			sortOrder = sortOrder.equals("ASC") ? "DESC" : "ASC";
//
//			}else{
//
//			sortField = field;
//
//			sortOrder = "ASC";
//
//			}
//
//			}
	
	public void search(){

	    page = 0;
	    loadTasks();

	}
	
	public void sort(String field){

	    if(sortField.equals(field)){
	        sortOrder = sortOrder.equals("ASC") ? "DESC" : "ASC";
	    }else{
	        sortField = field;
	        sortOrder = "ASC";
	    }

	    loadTasks();
	}
		
		public int getStartRecord(){
		    return page * pageSize + 1;
		}

		public int getEndRecord(){
		    if(myTasks == null){
		        return 0;
		    }
		    return (page * pageSize) + myTasks.size();
		}

		public int getCurrentPage(){
		    return page + 1;
		}
		
		// reset button 
		
		public void resetFilters(){

		    searchText = null;
		    searchType = "contains";
		    sortField = "createdAt";
		    sortOrder = "DESC";
		    page = 0;

		    loadTasks();
		}
		
		public void loadTasks(){

		    User partner = (User) FacesContext
		        .getCurrentInstance()
		        .getExternalContext()
		        .getSessionMap()
		        .get("loggedUser");

		    myTasks = taskService.getTasksByPartner(
		            partner.getId(),
		            page,
		            pageSize,
		            searchText,
		            searchType,
		            sortField,
		            sortOrder
		    );

		    totalRecords = taskService.getTotalTasksByPartner(
		            partner.getId(),
		            searchText,
		            searchType
		    );
		}
		
		
}