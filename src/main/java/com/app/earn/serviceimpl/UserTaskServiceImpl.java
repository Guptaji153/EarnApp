package com.app.earn.serviceimpl;

import java.util.List;

import com.app.earn.dao.UserTaskDao;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.service.UserTaskService;

public class UserTaskServiceImpl implements UserTaskService {

private UserTaskDao userTaskDao;

public void setUserTaskDao(UserTaskDao userTaskDao){
this.userTaskDao = userTaskDao;
}

@Override
public List<Task> getLiveTasks(){
return userTaskDao.getLiveTasks();
}

@Override
public void submitTaskProof(TaskSubmission submission){
userTaskDao.submitTaskProof(submission);
}

@Override
public List<TaskSubmission> getUserSubmissions(Long userId){
return userTaskDao.getUserSubmissions(userId);
}

@Override
public boolean hasUserSubmitted(Long userId, Long taskId){
return userTaskDao.hasUserSubmitted(userId, taskId);
}

@Override
public List<Task> getNewTasks(Long userId){
return userTaskDao.getNewTasks(userId);
}

@Override
public List<TaskSubmission> getSubmissionsByStatus(Long userId,String status){
return userTaskDao.getUserSubmissionsByStatus(userId,status);
}

@Override
public TaskSubmission getSubmissionByUserAndTask(Long userId, Long taskId){
    return userTaskDao.getSubmissionByUserAndTask(userId, taskId);
}

}