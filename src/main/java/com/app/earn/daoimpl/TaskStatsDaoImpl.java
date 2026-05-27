package com.app.earn.daoimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.app.earn.dao.TaskStatsDao;
import com.app.earn.pojo.TaskStats;
import com.app.earn.util.SessionHelper;

public class TaskStatsDaoImpl implements TaskStatsDao {

    /* ------------------------------------------------
       GET STATS WITH PAGINATION + SEARCH + SORT
       ------------------------------------------------ */

    @Override
    public List<TaskStats> getStatsByPartner(
            Long partnerId,
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder) {

        Session session = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            /* ---------------- SAFE SORT COLUMN ---------------- */

            String sortColumn = "t.id";

            if ("taskTitle".equals(sortField)) {
                sortColumn = "t.title";
            }
            else if ("totalSubmissions".equals(sortField)) {
                sortColumn = "s.totalSubmissions";
            }
            else if ("accepted".equals(sortField)) {
                sortColumn = "s.accepted";
            }
            else if ("rejected".equals(sortField)) {
                sortColumn = "s.rejected";
            }
            else if ("pending".equals(sortField)) {
                sortColumn = "s.pending";
            }

            /* ---------------- SAFE SORT ORDER ---------------- */

            String order = "DESC";

            if ("ASC".equalsIgnoreCase(sortOrder)) {
                order = "ASC";
            }

            /* ---------------- BASE QUERY ---------------- */

            String hql =
            "select " +
            "t.id as taskId, " +
            "t.title as taskTitle, " +
            "s.totalSubmissions as totalSubmissions, " +
            "s.accepted as accepted, " +
            "s.rejected as rejected, " +
            "s.pending as pending " +
            "from Task t, TaskStats s " +
            "where s.taskId = t.id " +
            "and t.partnerId = :pid " +
            "and t.status in ('LIVE','CLOSED')";

            /* ---------------- SEARCH ---------------- */

            boolean hasSearch =
                    searchText != null &&
                    !searchText.trim().isEmpty();

            if (hasSearch) {

                hql += " and lower(t.title) like :search";

                if ("starts".equals(searchType)) {

                    searchText =
                    searchText.trim().toLowerCase() + "%";

                } else {

                    searchText =
                    "%" +
                    searchText.trim().toLowerCase() +
                    "%";
                }
            }

            /* ---------------- SORT ---------------- */

            hql += " order by " + sortColumn + " " + order;

            Query q = session.createQuery(hql);

            q.setParameter("pid", partnerId);

            if (hasSearch) {
                q.setParameter("search", searchText);
            }

            /* ---------------- PAGINATION ---------------- */

            q.setFirstResult(page * pageSize);

            q.setMaxResults(pageSize);

            /* ---------------- DTO MAPPING ---------------- */

            q.setResultTransformer(
            Transformers.aliasToBean(TaskStats.class));

            return q.list();

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    /* ------------------------------------------------
       TOTAL COUNT
       ------------------------------------------------ */

    @Override
    public int getTotalStatsByPartner(
            Long partnerId,
            String searchText,
            String searchType) {

        Session session = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            String hql =
            "select count(t.id) " +
            "from Task t " +
            "where t.partnerId = :pid " +
            "and t.status in ('LIVE','CLOSED')";

            boolean hasSearch =
                    searchText != null &&
                    !searchText.trim().isEmpty();

            if (hasSearch) {

                hql += " and lower(t.title) like :search";

                if ("starts".equals(searchType)) {

                    searchText =
                    searchText.trim().toLowerCase() + "%";

                } else {

                    searchText =
                    "%" +
                    searchText.trim().toLowerCase() +
                    "%";
                }
            }

            Query q = session.createQuery(hql);

            q.setParameter("pid", partnerId);

            if (hasSearch) {
                q.setParameter("search", searchText);
            }

            Long count = (Long) q.uniqueResult();

            return count.intValue();

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    /* ------------------------------------------------
       SINGLE TASK STATS
       ------------------------------------------------ */

    @Override
    public TaskStats getStatsByTask(Long taskId) {

        Session session = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            return (TaskStats)
                    session.get(TaskStats.class, taskId);

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
}