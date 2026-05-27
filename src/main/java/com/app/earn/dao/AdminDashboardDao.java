package com.app.earn.dao;

import java.util.List;

import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.User;

public interface AdminDashboardDao {
//users related
	
	//List<User> getPartners();
    public User getUserById(Long id);
    void updateStatus(Long id,String status);
    List<User> getAllUsers();
    int getTotalUsersByRole(String role);
    // search
//    public List<User> searchPartnersContains(String text);
//    public List<User> searchPartnersStarts(String text);
    
    
 // tasks related
	  List<Task> getTasksByStatus(String status);
	  public int getTotalTasks();
	  public void updateTask(Task task);
	  
	  public Task getNextPendingTask(Long currentTaskId);
	  
	  // users related 
	//  List<User> getUsers();

	  void blockUser(Long id);

	  void unblockUser(Long id);
	  
	  
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

						void updateCampaignStatus(
						Long taskId,
						String status
						);
}
