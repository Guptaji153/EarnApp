package com.app.earn.daoimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.UserTaskDao;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskSubmission;
import com.app.earn.util.SessionHelper;

public class UserTaskDaoImpl implements UserTaskDao {

	@Override
	public List<Task> getLiveTasks() {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String hql = "FROM Task WHERE status='LIVE' ORDER BY createdAt DESC";

			Query q = session.createQuery(hql);

			return q.list();

		} finally {

			session.close();

		}
	}

	@Override
	public void submitTaskProof(TaskSubmission submission) {

		Session session = null;

		Transaction tx = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			tx = session.beginTransaction();

			/*
			 * ------------------------------ FETCH TASK ------------------------------
			 */

			Task task = (Task) session.get(Task.class, submission.getTaskId());

			if (task == null) {

				throw new RuntimeException("Task not found");
			}

			/*
			 * ------------------------------ STATUS CHECK ------------------------------
			 */

			if (!"LIVE".equals(task.getStatus())) {

				throw new RuntimeException("Campaign is closed");
			}

			/*
			 * ------------------------------ CHECK ACTIVE SUBMISSIONS
			 * ------------------------------
			 */

			String limitSql =

					"SELECT " +

							"(IFNULL(ts.accepted,0) + " + "IFNULL(ts.pending,0)) " +

							"FROM tasks t " +

							"LEFT JOIN task_stats ts " + "ON t.id = ts.task_id " +

							"WHERE t.id=:tid";

			SQLQuery limitQuery = session.createSQLQuery(limitSql);

			limitQuery.setParameter("tid", submission.getTaskId());

			Number active = (Number) limitQuery.uniqueResult();

			int current = active == null ? 0 : active.intValue();

			/*
			 * ------------------------------ LIMIT REACHED ------------------------------
			 */

			if (task.getSubmissionLimit() != null && current >= task.getSubmissionLimit()) {

				task.setStatus("CLOSED");

				session.update(task);

				throw new RuntimeException("Campaign submission limit reached");
			}

			/*
			 * ------------------------------ SAVE SUBMISSION ------------------------------
			 */

			submission.setStatus("PENDING");

			submission.setSubmittedAt(new Date());

			session.save(submission);

			/*
			 * ------------------------------ UPDATE STATS ------------------------------
			 */

			String sql =

					"INSERT INTO task_stats " +

							"(task_id,total_submissions," + "accepted,rejected,pending) " +

							"VALUES(:tid,1,0,0,1) " +

							"ON DUPLICATE KEY UPDATE " +

							"total_submissions = " + "total_submissions + 1, " +

							"pending = pending + 1";

			SQLQuery q = session.createSQLQuery(sql);

			q.setParameter("tid", submission.getTaskId());

			q.executeUpdate();

			/*
			 * ------------------------------ AUTO CLOSE IF FULL
			 * ------------------------------
			 */

			String closeSql =

					"UPDATE tasks t " +

							"JOIN task_stats ts " + "ON t.id = ts.task_id " +

							"SET t.status='CLOSED' " +

							"WHERE t.id=:tid " +

							"AND " +

							"(IFNULL(ts.accepted,0) + " + "IFNULL(ts.pending,0)) " +

							">= t.submission_limit";

			SQLQuery closeQuery = session.createSQLQuery(closeSql);

			closeQuery.setParameter("tid", submission.getTaskId());

			closeQuery.executeUpdate();

			/*
			 * ------------------------------ COMMIT ------------------------------
			 */

			tx.commit();

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}

			throw e;

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

//	@Override
//	public void submitTaskProof(TaskSubmission submission) {
//
//		Session session = SessionHelper.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();
//
//		try {
//
//			/*
//			 * ------------------------------ SAVE USER SUBMISSION
//			 * ------------------------------
//			 */
//
//			submission.setStatus("PENDING");
//			submission.setSubmittedAt(new Date());
//
//			session.save(submission);
//
//			/*
//			 * ------------------------------ UPDATE TASK STATS INSERT if first submission
//			 * UPDATE otherwise ------------------------------
//			 */
//
//			String sql = "INSERT INTO task_stats(task_id,total_submissions,accepted,rejected,pending) "
//					+ "VALUES(:tid,1,0,0,1) " + "ON DUPLICATE KEY UPDATE "
//					+ "total_submissions = total_submissions + 1, " + "pending = pending + 1";
//
//			SQLQuery q = session.createSQLQuery(sql);
//			q.setParameter("tid", submission.getTaskId());
//
//			q.executeUpdate();
//
//			tx.commit();
//
//		} catch (Exception e) {
//
//			tx.rollback();
//			throw e;
//
//		} finally {
//
//			session.close();
//
//		}
//	}

	@Override
	public List<TaskSubmission> getUserSubmissions(Long userId) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String hql = "FROM TaskSubmission WHERE userId=:uid ORDER BY submittedAt DESC";

			Query q = session.createQuery(hql);

			q.setParameter("uid", userId);

			return q.list();

		} finally {

			session.close();

		}
	}

	@Override
	public boolean hasUserSubmitted(Long userId, Long taskId) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String sql = "SELECT COUNT(*) FROM task_submissions WHERE user_id=:uid AND task_id=:tid";

			SQLQuery q = session.createSQLQuery(sql);

			q.setParameter("uid", userId);
			q.setParameter("tid", taskId);

			Number n = (Number) q.uniqueResult();

			return n != null && n.intValue() > 0;

		} finally {

			session.close();

		}
	}

// new one -----------------

	@Override
	public List<Task> getNewTasks(Long userId) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String hql = "FROM Task t WHERE t.status='LIVE' AND t.id NOT IN "
					+ "(SELECT ts.taskId FROM TaskSubmission ts WHERE ts.userId=:uid) " + "ORDER BY t.createdAt DESC";

			Query q = session.createQuery(hql);
			q.setParameter("uid", userId);

			return q.list();

		} finally {
			session.close();
		}
	}

	@Override
	public List<TaskSubmission> getUserSubmissionsByStatus(Long userId, String status) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String hql = "FROM TaskSubmission WHERE userId=:uid AND status=:status ORDER BY submittedAt DESC";
//	String hql =
//			"SELECT ts, t.title, t.reward FROM TaskSubmission ts, Task t " +
//			"WHERE ts.taskId = t.id AND ts.userId=:uid AND ts.status=:status " +
//			"ORDER BY ts.submittedAt DESC";

			Query q = session.createQuery(hql);

			q.setParameter("uid", userId);
			q.setParameter("status", status);

			return q.list();

		} finally {
			session.close();
		}
	}

	@Override
	public TaskSubmission getSubmissionByUserAndTask(Long userId, Long taskId) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String hql = "FROM TaskSubmission WHERE userId=:uid AND taskId=:tid";

			Query q = session.createQuery(hql);
			q.setParameter("uid", userId);
			q.setParameter("tid", taskId);

			return (TaskSubmission) q.uniqueResult();

		} finally {
			session.close();
		}
	}

// image view 

	@Override
	public boolean canUserAccessProof(Long userId, String role, String path) {

		Session session = SessionHelper.getSessionFactory().openSession();

		try {

			String sql;

			if ("USER".equals(role)) {
				sql = "SELECT COUNT(*) FROM task_submissions WHERE user_id=:uid AND proof_path=:path";
			} else if ("PARTNER".equals(role)) {
				sql = "SELECT COUNT(*) FROM task_submissions ts " + "JOIN tasks t ON ts.task_id = t.id "
						+ "WHERE t.partner_id=:uid AND ts.proof_path=:path";
			} else {
				return true; // ADMIN
			}

			SQLQuery q = session.createSQLQuery(sql);
			q.setParameter("uid", userId);
			q.setParameter("path", path);

			Number n = (Number) q.uniqueResult();

			return n != null && n.intValue() > 0;

		} finally {
			session.close();
		}
	}
}