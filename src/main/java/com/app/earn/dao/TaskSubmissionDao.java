package com.app.earn.dao;

import java.util.List;

import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;

public interface TaskSubmissionDao {

    List<TaskSubmission> getSubmissionsByTask(Long taskId);
    
    // Admin
    List<TaskSubmission> getPendingSubmissions();

    public TaskSubmission getSubmissionById(Long id);

    void approveSubmission(Long submissionId, Long adminId);

    void rejectSubmission(Long submissionId, Long adminId, String reason);

  //  void updateTaskStats(Long taskId, String action);
    public void updateTaskStats(Long taskId);

   // List<TaskSubmission> getAllSubmissions();
    List<TaskSubmission> getAllSubmissions(
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder
    );

    int getTotalSubmissions(
            String searchText,
            String searchType
    );
    
    public TaskSubmission getSubmissionFullDetails(Long id);
    // admin ui div 
    int getTodayPendingSubmissions();
    // show how many submition approved by admin
    double getAcceptanceRate();
    List<TaskStats> getTopCampaigns();
    // users tasks pending notifications on admin dashboard
    int getPendingSubmissionCount();
}