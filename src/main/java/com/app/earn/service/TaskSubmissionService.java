package com.app.earn.service;

import java.util.List;

import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;

public interface TaskSubmissionService {

    List<TaskSubmission> getSubmissionsByTask(Long taskId);
    
    // Admin
    


    List<TaskSubmission> getPendingSubmissions();

    void approveSubmission(Long submissionId, Long adminId);

    void rejectSubmission(Long submissionId, Long adminId, String reason);
    
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
    
    int getTodayPendingSubmissions();
    public double getAcceptanceRate();
    List<TaskStats> getTopCampaigns();
    // users tasks pending notifications on admin dashboard

    int getPendingSubmissionCount();
}