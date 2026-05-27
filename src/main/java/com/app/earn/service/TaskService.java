package com.app.earn.service;

import java.util.List;
import com.app.earn.pojo.Task;

public interface TaskService {

    void saveTask(Task task);

    List<Task> getTasksByPartner(
            Long partnerId,
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder);

    int getTotalTasksByPartner(
            Long partnerId,
            String searchText,
            String searchType);

    void updateTask(Task task);

    Task getTaskById(Long id);

    int getTasksByStatus(Long partnerId,String status);
    
    
    
    
}