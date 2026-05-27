package com.app.earn.dao;

import java.util.List;
import com.app.earn.pojo.Task;

public interface TaskDao {


	    void saveTask(Task task);
	  //  List<Task> getTasksByPartner(Long partnerId);
	    public List<Task> getTasksByPartner(
	    		Long partnerId,
	    		int page,
	    		int pageSize,
	    		String searchText,
	    		String searchType,
	    		String sortField,
	    		String sortOrder);
	    void updateTask(Task task);
	    Task getTaskById(Long id);
	  //  int getTotalTasksByPartner(Long partnerId);
	    
	    int getTotalTasksByPartner(
	    		Long partnerId,
	    		String searchText,
	    		String searchType);

	    int getTasksByStatus(Long partnerId,String status);
	    
	    
	    
	   
}