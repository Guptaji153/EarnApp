package com.app.earn.serviceimpl;

import java.util.List;

import com.app.earn.dao.TaskDao;
import com.app.earn.pojo.Task;
import com.app.earn.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private TaskDao taskDao;

    public void setTaskDao(TaskDao taskDao){
        this.taskDao = taskDao;
    }

    public void saveTask(Task task){

        taskDao.saveTask(task);

    }
    
//    public List<Task> getTasksByPartner(Long partnerId){
//
//    	return taskDao.getTasksByPartner(partnerId);
//
//    	}
    
    @Override
    public List<Task> getTasksByPartner(
    Long partnerId,
    int page,
    int pageSize,
    String searchText,
    String searchType,
    String sortField,
    String sortOrder){

    return taskDao.getTasksByPartner(
    partnerId,
    page,
    pageSize,
    searchText,
    searchType,
    sortField,
    sortOrder);
    }

    @Override
    public int getTotalTasksByPartner(
    Long partnerId,
    String searchText,
    String searchType){

    return taskDao.getTotalTasksByPartner(
    partnerId,
    searchText,
    searchType);
    }

    public void updateTask(Task task){

    	taskDao.updateTask(task);

    	}
    
    public Task getTaskById(Long id){

    	return taskDao.getTaskById(id);

    	}
    
//    public int getTotalTasksByPartner(Long partnerId){
//    	return taskDao.getTotalTasksByPartner(partnerId);
//    	}

    	public int getTasksByStatus(Long partnerId,String status){
    	return taskDao.getTasksByStatus(partnerId,status);
    	}

    	
    	
		
}