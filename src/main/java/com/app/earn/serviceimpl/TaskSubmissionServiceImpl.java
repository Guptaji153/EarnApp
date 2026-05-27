package com.app.earn.serviceimpl;

import java.util.List;

import com.app.earn.dao.TaskSubmissionDao;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.service.TaskSubmissionService;

public class TaskSubmissionServiceImpl implements TaskSubmissionService {

    private transient  TaskSubmissionDao taskSubmissionDao;

    public void setTaskSubmissionDao(TaskSubmissionDao taskSubmissionDao){
        this.taskSubmissionDao = taskSubmissionDao;
    }

    public List<TaskSubmission> getSubmissionsByTask(Long taskId){
        return taskSubmissionDao.getSubmissionsByTask(taskId);
    }

    
    // admin
    
  

    @Override
    public List<TaskSubmission> getPendingSubmissions(){

        return taskSubmissionDao.getPendingSubmissions();
    }


    @Override
    public void approveSubmission(Long submissionId, Long adminId){

    	
//        TaskSubmission s =
//        taskSubmissionDao.getSubmissionById(submissionId);
    	TaskSubmission s =
    			taskSubmissionDao.getSubmissionById(submissionId);

    			if(s == null){
    			throw new RuntimeException("Submission not found");
    			}

        if(!"PENDING".equals(s.getStatus())){
            throw new RuntimeException("Submission already verified");
        }

        taskSubmissionDao.approveSubmission(submissionId, adminId);

       // taskSubmissionDao.updateTaskStats(s.getTaskId(),"ACCEPT");
        taskSubmissionDao.updateTaskStats(s.getTaskId());
    }


    @Override
    public void rejectSubmission(Long submissionId, Long adminId, String reason){

        if(reason == null || reason.trim().isEmpty()){
            throw new RuntimeException("Rejection reason required");
        }

//        TaskSubmission s =
//        taskSubmissionDao.getSubmissionById(submissionId);
        TaskSubmission s =
        		taskSubmissionDao.getSubmissionById(submissionId);

        		if(s == null){
        		throw new RuntimeException("Submission not found");
        		}

        if(!"PENDING".equals(s.getStatus())){
            throw new RuntimeException("Submission already verified");
        }

        taskSubmissionDao.rejectSubmission(submissionId,adminId,reason);

      //  taskSubmissionDao.updateTaskStats(s.getTaskId(),"REJECT");
        taskSubmissionDao.updateTaskStats(s.getTaskId());
    }
    
    @Override
    public List<TaskSubmission> getAllSubmissions(
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder){

        return taskSubmissionDao.getAllSubmissions(
                page,
                pageSize,
                searchText,
                searchType,
                sortField,
                sortOrder
        );
    }

    @Override
    public int getTotalSubmissions(
            String searchText,
            String searchType){

        return taskSubmissionDao.getTotalSubmissions(
                searchText,
                searchType
        );
    }
    
    @Override
    public TaskSubmission getSubmissionFullDetails(Long id){
        return taskSubmissionDao.getSubmissionFullDetails(id);
    }
    
    @Override
    public int getTodayPendingSubmissions(){

        return taskSubmissionDao.getTodayPendingSubmissions();

    }
    
    @Override
    public double getAcceptanceRate(){

        return taskSubmissionDao.getAcceptanceRate();

    }
    
    @Override
    public List<TaskStats> getTopCampaigns(){
    	return taskSubmissionDao.getTopCampaigns();
    }
    
    // users tasks pending notifications on admin dashboard

    @Override
    public int getPendingSubmissionCount(){

    return taskSubmissionDao.getPendingSubmissionCount();

    }
    
}