package com.app.earn.dao;

import java.util.List;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskSubmission;

public interface UserTaskDao {

    List<Task> getLiveTasks();

    void submitTaskProof(TaskSubmission submission);

    List<TaskSubmission> getUserSubmissions(Long userId);

    boolean hasUserSubmitted(Long userId, Long taskId);

    // -----------
    public List<Task> getNewTasks(Long userId);
    public List<TaskSubmission> getUserSubmissionsByStatus(Long userId, String status);
    public TaskSubmission getSubmissionByUserAndTask(Long userId, Long taskId);
    
    // view uploaded image
    public boolean canUserAccessProof(Long userId, String role, String path);
}