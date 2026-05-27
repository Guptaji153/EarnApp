package com.app.earn.serviceimpl;

import java.util.List;

import com.app.earn.dao.TaskStatsDao;
import com.app.earn.pojo.TaskStats;
import com.app.earn.service.TaskStatsService;

public class TaskStatsServiceImpl implements TaskStatsService {

private TaskStatsDao taskStatsDao;

public void setTaskStatsDao(TaskStatsDao taskStatsDao){
    this.taskStatsDao = taskStatsDao;
}

@Override
public List<TaskStats> getStatsByPartner(
Long partnerId,
int page,
int pageSize,
String searchText,
String searchType,
String sortField,
String sortOrder){

return taskStatsDao.getStatsByPartner(
partnerId,
page,
pageSize,
searchText,
searchType,
sortField,
sortOrder
);
}

@Override
public int getTotalStatsByPartner(
Long partnerId,
String searchText,
String searchType){

return taskStatsDao.getTotalStatsByPartner(
partnerId,
searchText,
searchType
);
}

@Override
public TaskStats getStatsByTask(Long taskId){
return taskStatsDao.getStatsByTask(taskId);
}

}