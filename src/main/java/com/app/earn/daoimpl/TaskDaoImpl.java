package com.app.earn.daoimpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.TaskDao;
import com.app.earn.pojo.Task;
import com.app.earn.util.SessionHelper;

public class TaskDaoImpl implements TaskDao {

    /* ------------------------------------------------
       SAVE TASK
       ------------------------------------------------ */

    @Override
    public void saveTask(Task task) {

        Session session = null;
        Transaction tx = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            tx = session.beginTransaction();

            session.save(task);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    /* ------------------------------------------------
       GET TASKS WITH PAGINATION + SEARCH + SORT
       ------------------------------------------------ */

    @Override
    public List<Task> getTasksByPartner(
            Long partnerId,
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder) {

        Session session = null;

        List<Task> list = new ArrayList<>();

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            /* -------------------------------
               1. SAFE SORT COLUMN MAPPING
               ------------------------------- */

            String sortColumn = "created_at";

            if ("title".equals(sortField)) {
                sortColumn = "title";
            }
            else if ("country".equals(sortField)) {
                sortColumn = "country";
            }
            else if ("reward".equals(sortField)) {
                sortColumn = "reward";
            }
            else if ("status".equals(sortField)) {
                sortColumn = "status";
            }
            else if ("createdAt".equals(sortField)) {
                sortColumn = "created_at";
            }
            else if ("approvedAt".equals(sortField)) {
                sortColumn = "approved_at";
            }

            /* -------------------------------
               SAFE SORT ORDER
               ------------------------------- */

            String order = "DESC";

            if ("ASC".equalsIgnoreCase(sortOrder)) {
                order = "ASC";
            }

            /* -------------------------------
               BASE SQL
               ------------------------------- */

            String sql =
            "SELECT " +
            "t.id, " +
            "t.title, " +
            "t.description, " +
            "t.reward, " +
            "t.country, " +
            "t.status, " +
            "t.created_at, " +
            "t.approved_at, " +
            "u.full_name " +
            "FROM tasks t " +
            "LEFT JOIN users u ON t.approved_by = u.id " +
            "WHERE t.partner_id = :pid ";

            /* -------------------------------
               SEARCH FILTER
               ------------------------------- */

            boolean hasSearch =
                    searchText != null &&
                    !searchText.trim().isEmpty();

            if (hasSearch) {

                sql += " AND t.title LIKE :search ";

                if ("starts".equals(searchType)) {
                    searchText = searchText.trim() + "%";
                } else {
                    searchText = "%" + searchText.trim() + "%";
                }
            }

            /* -------------------------------
               SORTING
               ------------------------------- */

            sql += " ORDER BY t." + sortColumn + " " + order;

            /* -------------------------------
               EXECUTE QUERY
               ------------------------------- */

            SQLQuery q = session.createSQLQuery(sql);

            q.setParameter("pid", partnerId);

            if (hasSearch) {
                q.setParameter("search", searchText);
            }

            /* -------------------------------
               PAGINATION
               ------------------------------- */

            q.setFirstResult(page * pageSize);
            q.setMaxResults(pageSize);

            List<Object[]> rows = q.list();

            /* -------------------------------
               MAP RESULT TO OBJECT
               ------------------------------- */

            for (Object[] r : rows) {

                Task t = new Task();

                t.setId(((Number) r[0]).longValue());

                t.setTitle((String) r[1]);

                t.setDescription((String) r[2]);

                if (r[3] != null) {
                    t.setReward(
                            ((Number) r[3]).doubleValue());
                }

                t.setCountry((String) r[4]);

                t.setStatus((String) r[5]);

                if (r[6] != null) {

                    t.setCreatedAt(
                            new java.util.Date(
                                    ((java.sql.Timestamp) r[6])
                                    .getTime()));
                }

                if (r[7] != null) {

                    t.setApprovedAt(
                            new java.util.Date(
                                    ((java.sql.Timestamp) r[7])
                                    .getTime()));
                }

                if (r[8] != null) {
                    t.setApprovedByName((String) r[8]);
                }

                list.add(t);
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }

        return list;
    }

    /* ------------------------------------------------
       UPDATE TASK
       ------------------------------------------------ */

    @Override
    public void updateTask(Task task) {

        Session session = null;
        Transaction tx = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            tx = session.beginTransaction();

            session.update(task);

            tx.commit();

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    /* ------------------------------------------------
       GET TASK BY ID
       ------------------------------------------------ */

    @Override
    public Task getTaskById(Long id) {

        Session session = null;

        Task task = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            task = (Task) session.get(Task.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        } finally {

            if (session != null) {
                session.close();
            }
        }

        return task;
    }

    /* ------------------------------------------------
       TOTAL TASK COUNT
       ------------------------------------------------ */

    @Override
    public int getTotalTasksByPartner(
            Long partnerId,
            String searchText,
            String searchType) {

        Session session = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            String hql =
            "SELECT count(*) FROM Task WHERE partnerId = :pid";

            boolean hasSearch =
                    searchText != null &&
                    !searchText.trim().isEmpty();

            if (hasSearch) {

                hql += " AND title LIKE :search";

                if ("starts".equals(searchType)) {
                    searchText = searchText.trim() + "%";
                } else {
                    searchText = "%" + searchText.trim() + "%";
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
       TASK COUNT BY STATUS
       ------------------------------------------------ */

    @Override
    public int getTasksByStatus(
            Long partnerId,
            String status) {

        Session session = null;

        try {

            session = SessionHelper
                    .getSessionFactory()
                    .openSession();

            Query q = session.createQuery(
            "select count(*) from Task " +
            "where partnerId=:pid and status=:status");

            q.setParameter("pid", partnerId);

            q.setParameter("status", status);

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
}