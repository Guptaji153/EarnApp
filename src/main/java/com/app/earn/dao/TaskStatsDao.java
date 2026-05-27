package com.app.earn.dao;

import java.util.List;
import com.app.earn.pojo.TaskStats;

public interface TaskStatsDao {

    List<TaskStats> getStatsByPartner(
        Long partnerId,
        int page,
        int pageSize,
        String searchText,
        String searchType,
        String sortField,
        String sortOrder
    );

    int getTotalStatsByPartner(
        Long partnerId,
        String searchText,
        String searchType
    );

    TaskStats getStatsByTask(Long taskId);
}