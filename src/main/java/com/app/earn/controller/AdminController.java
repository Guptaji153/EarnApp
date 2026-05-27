package com.app.earn.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.exception.ValidationException;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.User;
import com.app.earn.service.AdminDashboardSearvice;
import com.app.earn.service.TaskService;


public class AdminController implements Serializable {

	 private static final long serialVersionUID = 1L;

    private AdminDashboardSearvice adminDashboardSearvice;
    private TaskService taskService;

    private Long taskId;

    public Long getTaskId(){
        return taskId;
    }

    public void setTaskId(Long taskId){
        this.taskId = taskId;
    }
  //  private TaskService taskService;

    private List<Task> draftTasks;
    private Task selectedTask;

    /* ===============================
       DASHBOARD STATS
       =============================== */

    public int getTotalPartners(){
        return adminDashboardSearvice.getTotalUsersByRole("PARTNER");
    }

    public int getTotalUsers(){
        return adminDashboardSearvice.getTotalUsersByRole("USER");
    }

    public int getTotalTasks(){
        return adminDashboardSearvice.getTotalTasks();
    }
    
    

    public int getPendingTasks(){

        List<Task> list = adminDashboardSearvice.getTasksByStatus("DRAFT");

        if(list == null){
            return 0;
        }

        return list.size();
    }

    public List<Task> getDraftTasks(){

        if(draftTasks == null){
            draftTasks = adminDashboardSearvice.getTasksByStatus("DRAFT");
        }

        return draftTasks;
    }

    /* ===============================
       OPEN TASK REVIEW PAGE
       =============================== */

//    public String openTask(){
//
//        FacesContext.getCurrentInstance()
//        .getExternalContext()
//        .getSessionMap()
//        .put("selectedTask", selectedTask);
//
//        return "/admin/reviewTask.xhtml?faces-redirect=true";
//    }

//    /* ===============================
//       APPROVE TASK
//       =============================== */
//
    
    
    
    public String approveTask(){

        try{

            User admin = (User) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedUser");

            adminDashboardSearvice.approveTask(selectedTask, admin.getId());

            // SUCCESS MESSAGE ONLY AFTER SUCCESS
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Previous task approved successfully",
                    null
                )
            );

            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .setKeepMessages(true);

            Task nextTask =
                adminDashboardSearvice.getNextPendingTask(selectedTask.getId());

            if(nextTask == null){
                return "/admin/taskList.xhtml?faces-redirect=true";
            }

            selectedTask = nextTask;
            taskId = nextTask.getId();

            return "/admin/reviewTask.xhtml?faces-redirect=true&taskId=" + taskId;

        }
        catch(ValidationException e){

            // ERROR GLOBAL
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    null
                )
            );

            // ERROR FIELD
            FacesContext.getCurrentInstance().addMessage(
                "reviewForm:" + e.getField(),
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    null
                )
            );

            return null;
        }
    }
//
//    /* ===============================
//       REJECT TASK
//       =============================== */
//
    public String rejectTask(){

        try{

            User admin = (User) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedUser");

            adminDashboardSearvice.rejectTask(selectedTask, admin.getId());

            // SUCCESS MESSAGE ONLY AFTER SUCCESS
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Previous task rejected successfully",
                    null
                )
            );

            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .setKeepMessages(true);

            Task nextTask =
                adminDashboardSearvice.getNextPendingTask(selectedTask.getId());

            if(nextTask == null){
                return "/admin/taskList.xhtml?faces-redirect=true";
            }

            selectedTask = nextTask;
            taskId = nextTask.getId();

            return "/admin/reviewTask.xhtml?faces-redirect=true&taskId=" + taskId;

        }
        catch(ValidationException e){

            // ERROR GLOBAL
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    null
                )
            );

            // ERROR FIELD
            FacesContext.getCurrentInstance().addMessage(
                "reviewForm:" + e.getField(),
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    null
                )
            );

            return null;
        }
    }
    /* ===============================
       PARTNER MANAGEMENT
       =============================== */
    private int page = 0;
    private int pageSize = 5;

    private String sortField = "createdAt";
    private String sortOrder = "DESC";
    
    private List<User> partners;
    private String searchText;
    private String searchType = "contains";

//    public List<User> getPartners(){
//
//        if(partners == null){
//            partners = adminDashboardSearvice.getPartners();
//        }
//
//        return partners;
//    }
    
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
    
    public List<User> getPartners(){

        if(partners == null){

            partners =
            adminDashboardSearvice.getPartners(
                page,
                pageSize,
                searchText,
                searchType,
                sortField,
                sortOrder
            );
        }

        return partners;
    }
    
    public int getTotalPartnerRecords(){

        return adminDashboardSearvice
                .getTotalPartners(
                        searchText,
                        searchType
                );
    }
    
    public int getTotalPages(){

        int total =
        adminDashboardSearvice.getTotalPartners(
            searchText,
            searchType
        );

        return (int) Math.ceil(
            (double) total / pageSize
        );
    }
    
    public void nextPage(){

        if(page + 1 < getTotalPages()){

            page++;

            partners = null;
        }
    }

    public void previousPage(){

        if(page > 0){

            page--;

            partners = null;
        }
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

        partners = null;
    }

//    public void searchPartners(){
//
//        if(searchText == null || searchText.trim().isEmpty()){
//            partners = adminDashboardSearvice.getPartners();
//            return;
//        }
//
//        if("starts".equals(searchType)){
//            partners = adminDashboardSearvice.searchPartnersStarts(searchText);
//        }else{
//            partners = adminDashboardSearvice.searchPartnersContains(searchText);
//        }
//    }
    
    public void searchPartners(){

        page = 0;

        partners = null;
    }
    
//    public void showAllPartners(){
//
//        partners = adminDashboardSearvice.getPartners();
//        searchText = null;
//    }
    
    public void showAllPartners(){

        searchText = null;
        searchType = "contains";

        page = 0;

        sortField = "createdAt";
        sortOrder = "DESC";
        partners = null;
    }

    public void approvePartner(Long id){

    	adminDashboardSearvice.approvePartner(id);

        partners = null;

        FacesContext.getCurrentInstance()
        .addMessage(null,new FacesMessage("Partner Approved"));
    }

    public void blockPartner(Long id){

    	adminDashboardSearvice.blockPartner(id);

        partners = null;

        FacesContext.getCurrentInstance()
        .addMessage(null,new FacesMessage("Partner Blocked"));
    }

    // open tasks details for review 
    public void loadTask(){

        if(taskId != null){

            selectedTask = taskService.getTaskById(taskId);

        }

    }
    /* ===============================
       GETTERS / SETTERS
       =============================== */

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public AdminDashboardSearvice getAdminDashboardSearvice() {
        return adminDashboardSearvice;
    }

    public void setAdminDashboardSearvice(AdminDashboardSearvice adminDashboardSearvice) {
        this.adminDashboardSearvice = adminDashboardSearvice;
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
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
   
    public int getLiveTasks(){

        List<Task> list = adminDashboardSearvice.getTasksByStatus("LIVE");

        if(list == null){
            return 0;
        }

        return list.size();
    }

    public int getRejectedTasks(){

        List<Task> list = adminDashboardSearvice.getTasksByStatus("REJECTED");

        if(list == null){
            return 0;
        }

        return list.size();
    }
    
    // users table 
    
    /* USER TABLE */

    private int userPage = 0;

    private int userPageSize = 5;

    private String userSortField = "createdAt";

    private String userSortOrder = "DESC";

    private String userSearchText;

    private String userSearchType = "contains";
    
    
    private List<User> users;
    
    public List<User> getUsers(){

        if(users == null){

            users =
            adminDashboardSearvice.getUsers(
                userPage,
                userPageSize,
                userSearchText,
                userSearchType,
                userSortField,
                userSortOrder
            );
        }

        return users;
    }
    
    public void showAllUsers(){

        userSearchText = null;

        userSearchType = "contains";

        userSortField = "createdAt";

        userSortOrder = "DESC";

        userPage = 0;

        users = null;
    }
    
    public void sortUsers(String field){

        if(field.equals(userSortField)){

            if("ASC".equals(userSortOrder)){
                userSortOrder = "DESC";
            }else{
                userSortOrder = "ASC";
            }

        }else{

            userSortField = field;
            userSortOrder = "ASC";
        }

        users = null;
    }
    
    public void searchUsers(){

        userPage = 0;

        users = null;
    }
    
    public void nextUserPage(){

        if(userPage + 1 < getTotalUserPages()){

            userPage++;

            users = null;
        }
    }

    public void previousUserPage(){

        if(userPage > 0){

            userPage--;

            users = null;
        }
    }
    
    public int getTotalUserPages(){

        int total =
        adminDashboardSearvice.getTotalUsers(
            userSearchText,
            userSearchType
        );

        return (int)Math.ceil(
            (double) total / userPageSize
        );
    }
    
    public int getTotalUserRecords(){

        return adminDashboardSearvice.getTotalUsers(
                userSearchText,
                userSearchType
        );
    }
    public void blockUser(Long id){

    	adminDashboardSearvice.blockUser(id);

    	users = null;

    	FacesContext.getCurrentInstance().addMessage(
    	null,
    	new FacesMessage("User blocked")
    	);

    	}
    
    public void unblockUser(Long id){

    	adminDashboardSearvice.unblockUser(id);

    	users = null;

    	FacesContext.getCurrentInstance().addMessage(
    	null,
    	new FacesMessage("User unblocked")
    	);

    	}
   
    public int getUserPage() {
        return userPage;
    }

    public void setUserPage(int userPage) {
        this.userPage = userPage;
    }

    public int getUserPageSize() {
        return userPageSize;
    }

    public void setUserPageSize(int userPageSize) {
        this.userPageSize = userPageSize;
    }

    public String getUserSortField() {
        return userSortField;
    }

    public void setUserSortField(String userSortField) {
        this.userSortField = userSortField;
    }

    public String getUserSortOrder() {
        return userSortOrder;
    }

    public void setUserSortOrder(String userSortOrder) {
        this.userSortOrder = userSortOrder;
    }

    public String getUserSearchText() {
        return userSearchText;
    }

    public void setUserSearchText(String userSearchText) {
        this.userSearchText = userSearchText;
    }

    public String getUserSearchType() {
        return userSearchType;
    }

    public void setUserSearchType(String userSearchType) {
        this.userSearchType = userSearchType;
    }
}