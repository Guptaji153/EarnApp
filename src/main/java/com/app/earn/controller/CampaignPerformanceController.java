package com.app.earn.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;

import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.pojo.User;
import com.app.earn.service.TaskStatsService;
import com.app.earn.service.TaskSubmissionService;

public class CampaignPerformanceController implements Serializable {

    private static final long serialVersionUID = 1L;

    private TaskStatsService taskStatsService;
    private TaskSubmissionService taskSubmissionService;

    private List<TaskStats> statsList;
    private List<TaskSubmission> submissions;

    private Long taskId;
    private TaskStats stats;

    /* ---------------------------
       PAGINATION
       --------------------------- */

    private int page = 0;
    private int pageSize = 5;
    private int totalRecords;

    /* ---------------------------
       SEARCH
       --------------------------- */

    private String searchText;
    private String searchType = "contains";

    /* ---------------------------
       SORTING
       --------------------------- */

    private String sortField = "taskId";
    private String sortOrder = "DESC";

    /* ---------------------------
       GETTERS / SETTERS
       --------------------------- */

    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }

    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }

    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
    public int getTotalRecords() { return totalRecords; }

    public String getSortField() { return sortField; }
    public String getSortOrder() { return sortOrder; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public TaskStats getStats() { return stats; }

    public List<TaskSubmission> getSubmissions() { return submissions; }

    public void setTaskStatsService(TaskStatsService taskStatsService) {
        this.taskStatsService = taskStatsService;
    }

    public void setTaskSubmissionService(TaskSubmissionService taskSubmissionService) {
        this.taskSubmissionService = taskSubmissionService;
    }

    /* ---------------------------
       LOAD CAMPAIGN STATS
       --------------------------- */

    public void loadStats(){

        User partner = (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedUser");

        statsList = taskStatsService.getStatsByPartner(
                partner.getId(),
                page,
                pageSize,
                searchText,
                searchType,
                sortField,
                sortOrder
        );

        totalRecords = taskStatsService.getTotalStatsByPartner(
                partner.getId(),
                searchText,
                searchType
        );
    }

    /* ---------------------------
       LAZY GETTER
       --------------------------- */

    public List<TaskStats> getStatsList(){

        if(statsList == null){
            loadStats();
        }

        return statsList;
    }

    /* ---------------------------
       PAGINATION
       --------------------------- */

    public void nextPage(){

        if((page + 1) * pageSize < totalRecords){
            page++;
            loadStats();
        }
    }

    public void prevPage(){

        if(page > 0){
            page--;
            loadStats();
        }
    }

    public int getCurrentPage(){
        return page + 1;
    }

    public int getStartRecord(){
        return page * pageSize + 1;
    }

    public int getEndRecord(){

        if(statsList == null){
            return 0;
        }

        return page * pageSize + statsList.size();
    }

    /* ---------------------------
       SORTING
       --------------------------- */

    public void sort(String field){

        if(sortField.equals(field)){
            sortOrder = sortOrder.equals("ASC") ? "DESC" : "ASC";
        }else{
            sortField = field;
            sortOrder = "ASC";
        }

        loadStats();
    }

    /* ---------------------------
       SEARCH
       --------------------------- */

    public void search(){

        page = 0;
        loadStats();
    }

    public void reset(){

        searchText = null;
        searchType = "contains";
        sortField = "taskId";
        sortOrder = "DESC";
        page = 0;

        loadStats();
    }

    /* ---------------------------
       LOAD SUBMISSIONS
       --------------------------- */

    public void loadSubmissions(){

        if(taskId != null){
            submissions = taskSubmissionService.getSubmissionsByTask(taskId);
        }
    }

}