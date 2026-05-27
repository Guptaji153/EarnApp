package com.app.earn.service;

import java.util.List;

import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.User;

public interface AdminDashboardSearvice {

 // Admin
    
   // List<User> getPartners();
	
	List<User> getPartners(
	        int page,
	        int pageSize,
	        String searchText,
	        String searchType,
	        String sortField,
	        String sortOrder
	);

	int getTotalPartners(
	        String searchText,
	        String searchType
	);

    void approvePartner(Long id);

    void blockPartner(Long id);
    List<User> getAllUsers();
    // search
    
//    public List<User> searchPartnersStarts(String text);
//    public List<User> searchPartnersContains(String text);
    
    int getTotalUsersByRole(String role);
    public int getTotalTasks();
	
	// tasks related 
	 List<Task> getTasksByStatus(String status);
	// public void updateTask(Task task);
	 public void approveTask(Task task, Long adminId);
	 public void rejectTask(Task task, Long adminId);
	 
	 public Task getNextPendingTask(Long currentTaskId);
	 
	 // users
	 void blockUser(Long id);
	 //List<User> getUsers();
	 void unblockUser(Long id);
	 List<User> getUsers(
		        int page,
		        int pageSize,
		        String searchText,
		        String searchType,
		        String sortField,
		        String sortOrder
		);

		int getTotalUsers(
		        String searchText,
		        String searchType
		);
		
		/* -----------------------------
		   ADMIN CAMPAIGN ANALYTICS
		----------------------------- */

		List<TaskStats> getAllCampaignStats(
		        int page,
		        int pageSize,
		        String searchText,
		        String searchType,
		        String sortField,
		        String sortOrder
		);

		int getTotalCampaignStats(
		        String searchText,
		        String searchType
		);
		
		TaskStats getCampaignDetails(Long taskId);

		void updateCampaignLimit(
		Long taskId,
		Integer newLimit
		);

		void closeCampaign(Long taskId);

		void reopenCampaign(Long taskId);
}
