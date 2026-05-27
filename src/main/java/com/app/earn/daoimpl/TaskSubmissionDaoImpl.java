package com.app.earn.daoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.TaskSubmissionDao;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.util.SessionHelper;

public class TaskSubmissionDaoImpl implements TaskSubmissionDao {

    public List<TaskSubmission> getSubmissionsByTask(Long taskId){

        Session session = SessionHelper.getSessionFactory().openSession();

        String sql =
        "SELECT ts.*, " +
        "u.full_name AS user_name, " +
        "a.full_name AS admin_name " +
        "FROM task_submissions ts " +
        "LEFT JOIN users u ON ts.user_id = u.id " +
        "LEFT JOIN users a ON ts.verified_by = a.id " +
        "WHERE ts.task_id = :tid " +
        "ORDER BY ts.submitted_at DESC";

        SQLQuery q = session.createSQLQuery(sql);

        q.setParameter("tid", taskId);

        List<Object[]> rows = q.list();

        List<TaskSubmission> list = new ArrayList<>();

        for(Object[] r : rows){

            TaskSubmission s = new TaskSubmission();

            s.setId(((Number)r[0]).longValue());
            s.setTaskId(((Number)r[1]).longValue());
            s.setUserId(((Number)r[2]).longValue());
            s.setProofPath((String)r[3]);
            s.setStatus((String)r[4]);
            s.setRejectionReason((String)r[5]);
            s.setSubmittedAt((java.sql.Timestamp)r[6]);
            s.setVerifiedBy(r[7] != null ? ((Number)r[7]).longValue() : null);
            s.setVerifiedAt((java.sql.Timestamp)r[8]);

            s.setUserName((String)r[9]);
            s.setAdminName((String)r[10]);

            list.add(s);
        }

        session.close();

        return list;
    }
    
    // admin relatd 
//    
//    @Override
//    public List<TaskSubmission> getPendingSubmissions(){
//
//        Session session = SessionHelper.getSessionFactory().openSession();
//
////        String sql =
////        "SELECT ts.*, u.full_name " +
////        "FROM task_submissions ts " +
////        "JOIN users u ON ts.user_id = u.id " +
////        "WHERE ts.status='PENDING' " +
////        "ORDER BY ts.submitted_at DESC";
//        
//        String sql =
//        		"SELECT ts.*, u.full_name, t.title, t.reward, t.url " +
//        		"FROM task_submissions ts " +
//        		"JOIN users u ON ts.user_id = u.id " +
//        		"JOIN tasks t ON ts.task_id = t.id " +
//        		"WHERE ts.status='PENDING' " +
//        		"ORDER BY ts.submitted_at DESC";
//
//        SQLQuery q = session.createSQLQuery(sql);
//
//        List<Object[]> rows = q.list();
//
//        List<TaskSubmission> list = new ArrayList<>();
//
//        for(Object[] r : rows){
//
//            TaskSubmission s = new TaskSubmission();
//
//            s.setId(((Number)r[0]).longValue());
//            s.setTaskId(((Number)r[1]).longValue());
//            s.setUserId(((Number)r[2]).longValue());
//            s.setProofPath((String)r[3]);
//            s.setStatus((String)r[4]);
//            s.setSubmittedAt((java.sql.Timestamp)r[6]);
//           // s.setUserName((String)r[9]);
//            s.setUserName((String)r[9]);
//            s.setTaskTitle((String)r[10]);
//            s.setReward(((Number)r[11]).intValue());
//            s.setTaskUrl((String)r[12]);
//
//            list.add(s);
//        }
//
//        session.close();
//
//        return list;
//    }

    @Override
    public List<TaskSubmission> getPendingSubmissions(){

        Session session = SessionHelper.getSessionFactory().openSession();

        String sql =
        "SELECT " +
        "ts.id as id, " +
        "ts.task_id as taskId, " +
        "ts.user_id as userId, " +
        "ts.proof_path as proofPath, " +
        "ts.status as status, " +
        "ts.rejection_reason as rejectionReason, " +
        "ts.submitted_at as submittedAt, " +
        "ts.verified_by as verifiedBy, " +
        "ts.verified_at as verifiedAt, " +
        "u.full_name as userName, " +
        "t.title as taskTitle, " +
        "t.reward as reward, " +
        "t.url as taskUrl, " +
        "t.description as description "+
        "FROM task_submissions ts " +
        "JOIN users u ON ts.user_id = u.id " +
        "JOIN tasks t ON ts.task_id = t.id " +
        "WHERE ts.status='PENDING' " +
        "ORDER BY ts.submitted_at DESC";

        SQLQuery q = session.createSQLQuery(sql);

     // ✅ ADD THIS (MANDATORY)
     q.addScalar("id", org.hibernate.type.LongType.INSTANCE);
     q.addScalar("taskId", org.hibernate.type.LongType.INSTANCE);
     q.addScalar("userId", org.hibernate.type.LongType.INSTANCE);
     q.addScalar("verifiedBy", org.hibernate.type.LongType.INSTANCE);

     q.addScalar("reward", org.hibernate.type.DoubleType.INSTANCE);

     // ✅ IMPORTANT (strings also safer)
     q.addScalar("proofPath", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("status", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("rejectionReason", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("userName", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("taskTitle", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("taskUrl", org.hibernate.type.StringType.INSTANCE);
     q.addScalar("description", org.hibernate.type.StringType.INSTANCE);

     // ✅ DATE
     q.addScalar("submittedAt", org.hibernate.type.TimestampType.INSTANCE);
     q.addScalar("verifiedAt", org.hibernate.type.TimestampType.INSTANCE);

     // transformer
     q.setResultTransformer(
         org.hibernate.transform.Transformers.aliasToBean(TaskSubmission.class)
     );

        List<TaskSubmission> list = q.list();

        session.close();

        return list;
    }
    
    
    @Override
    public TaskSubmission getSubmissionById(Long id){

        Session session = SessionHelper.getSessionFactory().openSession();

        TaskSubmission s =
        (TaskSubmission) session.get(TaskSubmission.class,id);

        session.close();

        return s;
    }


    @Override
    public void approveSubmission(Long submissionId, Long adminId){

        Session session = SessionHelper.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

//        TaskSubmission s =
//        (TaskSubmission) session.get(TaskSubmission.class, submissionId);
//
//        s.setStatus("ACCEPTED");
//        s.setVerifiedBy(adminId);
//        s.setVerifiedAt(new Date());
//
//        session.update(s);

        String sql =
        		"UPDATE task_submissions " +
        		"SET status='ACCEPTED', verified_by=:admin, verified_at=NOW() " +
        		"WHERE id=:id AND status='PENDING'";

        		SQLQuery q=session.createSQLQuery(sql);

        		q.setParameter("admin", adminId);
        		q.setParameter("id", submissionId);

        		q.executeUpdate();
        tx.commit();
        session.close();
    }


    @Override
    public void rejectSubmission(Long submissionId, Long adminId, String reason){

        Session session = SessionHelper.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

//        TaskSubmission s =
//        (TaskSubmission) session.get(TaskSubmission.class, submissionId);
//
//        s.setStatus("REJECTED");
//        s.setRejectionReason(reason);
//        s.setVerifiedBy(adminId);
//        s.setVerifiedAt(new Date());
//
//        session.update(s);
        
        String sql =
        		"UPDATE task_submissions " +
        		"SET status='REJECTED', rejection_reason=:reason, verified_by=:admin, verified_at=NOW() " +
        		"WHERE id=:id AND status='PENDING'";

        		SQLQuery q=session.createSQLQuery(sql);

        		q.setParameter("reason", reason);
        		q.setParameter("admin", adminId);
        		q.setParameter("id", submissionId);

        		q.executeUpdate();

        tx.commit();
        session.close();
    }


//    @Override
//    public void updateTaskStats(Long taskId, String action){
//
//        Session session = SessionHelper.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//
//        String sql =
//        "UPDATE task_stats SET " +
//        "pending = pending - 1, " +
//        (action.equals("ACCEPT") ?
//        "accepted = accepted + 1 " :
//        "rejected = rejected + 1 ") +
//        "WHERE task_id = :tid";
//
//        SQLQuery q = session.createSQLQuery(sql);
//
//        q.setParameter("tid", taskId);
//
//        q.executeUpdate();
//
//        tx.commit();
//        session.close();
//    }
    
    @Override
    public void updateTaskStats(Long taskId){

    Session session = SessionHelper.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    String sql =
    "UPDATE task_stats SET " +
    "total_submissions=(SELECT COUNT(*) FROM task_submissions WHERE task_id=:tid)," +
    "accepted=(SELECT COUNT(*) FROM task_submissions WHERE task_id=:tid AND status='ACCEPTED')," +
    "rejected=(SELECT COUNT(*) FROM task_submissions WHERE task_id=:tid AND status='REJECTED')," +
    "pending=(SELECT COUNT(*) FROM task_submissions WHERE task_id=:tid AND status='PENDING') " +
    "WHERE task_id=:tid";

    SQLQuery q = session.createSQLQuery(sql);
    q.setParameter("tid", taskId);

    q.executeUpdate();

    tx.commit();
    session.close();
    }
    
//    @Override
//    public List<TaskSubmission> getAllSubmissions(){
//
//        Session session = SessionHelper.getSessionFactory().openSession();
//
//        String sql =
//        "SELECT ts.*, " +
//        "u.full_name AS user_name, " +
//        "a.full_name AS admin_name " +
//        "FROM task_submissions ts " +
//        "LEFT JOIN users u ON ts.user_id = u.id " +
//        "LEFT JOIN users a ON ts.verified_by = a.id " +
//        "ORDER BY ts.submitted_at DESC";
//
//        SQLQuery q = session.createSQLQuery(sql);
//
//        List<Object[]> rows = q.list();
//
//        List<TaskSubmission> list = new ArrayList<>();
//
//        for(Object[] r : rows){
//
//            TaskSubmission s = new TaskSubmission();
//
//            s.setId(((Number)r[0]).longValue());
//            s.setTaskId(((Number)r[1]).longValue());
//            s.setUserId(((Number)r[2]).longValue());
//            s.setProofPath((String)r[3]);
//            s.setStatus((String)r[4]);
//            s.setRejectionReason((String)r[5]);
//            s.setSubmittedAt((java.sql.Timestamp)r[6]);
//
//            s.setVerifiedBy(r[7] != null ? ((Number)r[7]).longValue() : null);
//            s.setVerifiedAt((java.sql.Timestamp)r[8]);
//
//            s.setUserName((String)r[9]);
//            s.setAdminName((String)r[10]);
//
//            list.add(s);
//        }
//
//        session.close();
//
//        return list;
//    }
    
    
    @Override
    public List<TaskSubmission> getAllSubmissions(
            int page,
            int pageSize,
            String searchText,
            String searchType,
            String sortField,
            String sortOrder){

        Session session = null;

        try{

            session =
            SessionHelper.getSessionFactory().openSession();

            /* SAFE SORTING */

            String sortColumn = "ts.submitted_at";

            if("taskId".equals(sortField)){
                sortColumn = "ts.task_id";
            }
            else if("userName".equals(sortField)){
                sortColumn = "u.full_name";
            }
            else if("status".equals(sortField)){
                sortColumn = "ts.status";
            }
            else if("submittedAt".equals(sortField)){
                sortColumn = "ts.submitted_at";
            }

            String order = "DESC";

            if("ASC".equalsIgnoreCase(sortOrder)){
                order = "ASC";
            }

            /* BASE QUERY */

            String sql =
            "SELECT " +
            "ts.id, " +
            "ts.task_id, " +
            "ts.user_id, " +
            "ts.proof_path, " +
            "ts.status, " +
            "ts.rejection_reason, " +
            "ts.submitted_at, " +
            "ts.verified_by, " +
            "ts.verified_at, " +
            "u.full_name AS user_name, " +
            "a.full_name AS admin_name " +
            "FROM task_submissions ts " +
            "LEFT JOIN users u ON ts.user_id = u.id " +
            "LEFT JOIN users a ON ts.verified_by = a.id " +
            "WHERE 1=1 ";

            /* SEARCH */

            boolean hasSearch =
            searchText != null &&
            !searchText.trim().isEmpty();

            if(hasSearch){

                sql +=
                " AND lower(u.full_name) LIKE :search ";

                if("starts".equals(searchType)){

                    searchText =
                    searchText.toLowerCase().trim() + "%";

                }else{

                    searchText =
                    "%" +
                    searchText.toLowerCase().trim() +
                    "%";
                }
            }

            /* SORT */

            sql +=
            " ORDER BY " +
            sortColumn +
            " " +
            order;

            SQLQuery q = session.createSQLQuery(sql);

            if(hasSearch){
                q.setParameter("search", searchText);
            }

            /* PAGINATION */

            q.setFirstResult(page * pageSize);

            q.setMaxResults(pageSize);

            List<Object[]> rows = q.list();

            List<TaskSubmission> list =
            new ArrayList<>();

            for(Object[] r : rows){

                TaskSubmission s =
                new TaskSubmission();

                s.setId(
                r[0] != null ?
                ((Number)r[0]).longValue() :
                null);

                s.setTaskId(
                r[1] != null ?
                ((Number)r[1]).longValue() :
                null);

                s.setUserId(
                r[2] != null ?
                ((Number)r[2]).longValue() :
                null);

                s.setProofPath((String)r[3]);

                s.setStatus((String)r[4]);

                s.setRejectionReason((String)r[5]);

                s.setSubmittedAt(
                r[6] != null ?
                (java.sql.Timestamp)r[6] :
                null);

                s.setVerifiedBy(
                r[7] != null ?
                ((Number)r[7]).longValue() :
                null);

                s.setVerifiedAt(
                r[8] != null ?
                (java.sql.Timestamp)r[8] :
                null);

                s.setUserName((String)r[9]);

                s.setAdminName((String)r[10]);

                list.add(s);
            }

            return list;

        }finally{

            if(session != null){
                session.close();
            }
        }
    }
    
    
    @Override
    public int getTotalSubmissions(
            String searchText,
            String searchType){

        Session session = null;

        try{

            session =
            SessionHelper.getSessionFactory().openSession();

            String sql =
            "SELECT COUNT(*) " +
            "FROM task_submissions ts " +
            "LEFT JOIN users u ON ts.user_id = u.id " +
            "WHERE 1=1 ";

            boolean hasSearch =
            searchText != null &&
            !searchText.trim().isEmpty();

            if(hasSearch){

                sql +=
                " AND lower(u.full_name) LIKE :search ";

                if("starts".equals(searchType)){

                    searchText =
                    searchText.toLowerCase().trim() + "%";

                }else{

                    searchText =
                    "%" +
                    searchText.toLowerCase().trim() +
                    "%";
                }
            }

            SQLQuery q =
            session.createSQLQuery(sql);

            if(hasSearch){
                q.setParameter("search", searchText);
            }

            Number n =
            (Number) q.uniqueResult();

            return n == null ? 0 : n.intValue();

        }finally{

            if(session != null){
                session.close();
            }
        }
    }
    
    @Override
    public TaskSubmission getSubmissionFullDetails(Long id){

        Session session = SessionHelper.getSessionFactory().openSession();

        String sql =
        "SELECT " +
        "ts.id as id, " +
        "ts.task_id as taskId, " +
        "ts.user_id as userId, " +
        "ts.proof_path as proofPath, " +
        "ts.status as status, " +
        "ts.rejection_reason as rejectionReason, " +
        "ts.submitted_at as submittedAt, " +
        "ts.verified_by as verifiedBy, " +
        "ts.verified_at as verifiedAt, " +
        "ts.proof_description as proofDescription, " +
        "u.full_name as userName, " +
        "a.full_name as adminName, " +
        "t.title as taskTitle, " +
        "t.reward as reward, " +
        "t.submission_limit as submissionLimit," +
        "t.url as taskUrl, " +
        "t.description as description " +
        "FROM task_submissions ts " +
        "JOIN users u ON ts.user_id = u.id " +
        "LEFT JOIN users a ON ts.verified_by = a.id " +
        "JOIN tasks t ON ts.task_id = t.id " +
        "WHERE ts.id = :id";

        SQLQuery q = session.createSQLQuery(sql);

        q.setParameter("id", id);

        // Scalars
        q.addScalar("id", org.hibernate.type.LongType.INSTANCE);
        q.addScalar("taskId", org.hibernate.type.LongType.INSTANCE);
        q.addScalar("userId", org.hibernate.type.LongType.INSTANCE);
        q.addScalar("verifiedBy", org.hibernate.type.LongType.INSTANCE);
        q.addScalar("reward", org.hibernate.type.DoubleType.INSTANCE);

        q.addScalar("proofPath", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("status", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("rejectionReason", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("userName", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("adminName", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("taskTitle", org.hibernate.type.StringType.INSTANCE);
        q.addScalar(
        		"submissionLimit",
        		org.hibernate.type.IntegerType.INSTANCE
        		);
        q.addScalar("taskUrl", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("description", org.hibernate.type.StringType.INSTANCE);
        q.addScalar("proofDescription", org.hibernate.type.StringType.INSTANCE);

        q.addScalar("submittedAt", org.hibernate.type.TimestampType.INSTANCE);
        q.addScalar("verifiedAt", org.hibernate.type.TimestampType.INSTANCE);

        q.setResultTransformer(
            org.hibernate.transform.Transformers.aliasToBean(TaskSubmission.class)
        );

        TaskSubmission result = (TaskSubmission) q.uniqueResult();

        session.close();

        return result;
    }
    
    @Override
    public int getTodayPendingSubmissions(){

        Session session = SessionHelper.getSessionFactory().openSession();

        String sql =
        "SELECT COUNT(*) FROM task_submissions " +
        "WHERE status='PENDING' " +
        "AND DATE(submitted_at)=CURDATE()";

        SQLQuery q = session.createSQLQuery(sql);

        Number n = (Number) q.uniqueResult();

        session.close();

        return n == null ? 0 : n.intValue();
    }
    
    // how may submition by users has been approved by admin
    @Override
    public double getAcceptanceRate(){

        Session session = SessionHelper.getSessionFactory().openSession();

//        String sql =
//        "SELECT " +
//        "(SUM(CASE WHEN status='ACCEPTED' THEN 1 ELSE 0 END) * 100.0) / COUNT(*) " +
//        "FROM task_submissions";
        
        String sql = "SELECT\r\n"
        		+ "IFNULL(\r\n"
        		+ "(SUM(CASE WHEN status='ACCEPTED' THEN 1 ELSE 0 END) * 100.0) / NULLIF(COUNT(*),0)\r\n"
        		+ ",0)\r\n"
        		+ "FROM task_submissions";

        SQLQuery q = session.createSQLQuery(sql);

        Number n = (Number) q.uniqueResult();

        session.close();

        return n == null ? 0 : n.doubleValue();
    }
    
    @Override
    public List<TaskStats> getTopCampaigns(){

        Session session = SessionHelper.getSessionFactory().openSession();

        String sql =
        "SELECT ts.task_id, t.title, ts.total_submissions " +
        "FROM task_stats ts " +
        "JOIN tasks t ON ts.task_id = t.id " +
        "ORDER BY ts.total_submissions DESC " +
        "LIMIT 5";

        SQLQuery q = session.createSQLQuery(sql);

        List<Object[]> rows = q.list();

        List<TaskStats> list = new ArrayList<>();

        for(Object[] r : rows){

            TaskStats s = new TaskStats();

            s.setTaskId(((Number)r[0]).longValue());
            s.setTaskTitle((String) r[1]);
            s.setTotalSubmissions(((Number)r[2]).intValue());

            list.add(s);
        }

        session.close();

        return list;
    }
    
    // users tasks pending notifications on admin dashboard

    @Override
    public int getPendingSubmissionCount(){

    Session session = SessionHelper.getSessionFactory().openSession();

    String sql="SELECT COUNT(*) FROM task_submissions WHERE status='PENDING'";

    SQLQuery q=session.createSQLQuery(sql);

    Number n=(Number) q.uniqueResult();

    session.close();

    return n==null?0:n.intValue();
    }
}