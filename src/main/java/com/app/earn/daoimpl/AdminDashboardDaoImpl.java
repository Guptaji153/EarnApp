package com.app.earn.daoimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.AdminDashboardDao;
import com.app.earn.pojo.Task;
import com.app.earn.pojo.TaskStats;
import com.app.earn.pojo.User;
import com.app.earn.util.SessionHelper;

public class AdminDashboardDaoImpl implements AdminDashboardDao {

// admin

//users related
//	@Override
//    public List<User> getPartners(){
//
//        Session session =
//        SessionHelper.getSessionFactory().openSession();
//
//        Query query =
//        session.createQuery(
//        "from User where role='PARTNER'");
//
//        List<User> list = query.list();
//
//        session.close();
//
//        return list;
//    }

	@Override
	public List<User> getPartners(int page, int pageSize, String searchText, String searchType, String sortField,
			String sortOrder) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			String sortColumn = "createdAt";

			if ("id".equals(sortField)) {
				sortColumn = "id";
			} else if ("fullName".equals(sortField)) {
				sortColumn = "fullName";
			} else if ("email".equals(sortField)) {
				sortColumn = "email";
			} else if ("status".equals(sortField)) {
				sortColumn = "status";
			} else if ("phone".equals(sortField)) {
				sortColumn = "phone";
			}

			String order = "DESC";

			if ("ASC".equalsIgnoreCase(sortOrder)) {
				order = "ASC";
			}

			String hql = "FROM User " + "WHERE role='PARTNER' ";

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				hql += " AND lower(fullName) LIKE :search";

				if ("starts".equals(searchType)) {
					searchText = searchText.toLowerCase().trim() + "%";
				} else {
					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			hql += " ORDER BY " + sortColumn + " " + order;

			Query q = session.createQuery(hql);

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			q.setFirstResult(page * pageSize);

			q.setMaxResults(pageSize);

			return q.list();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public int getTotalPartners(String searchText, String searchType) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			String hql = "SELECT COUNT(*) " + "FROM User " + "WHERE role='PARTNER'";

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				hql += " AND lower(fullName) LIKE :search";

				if ("starts".equals(searchType)) {
					searchText = searchText.toLowerCase().trim() + "%";
				} else {
					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			Query q = session.createQuery(hql);

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			Long count = (Long) q.uniqueResult();

			return count.intValue();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public void updateStatus(Long id, String status) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		User user = (User) session.get(User.class, id);

		user.setStatus(status);

		session.update(user);

		tx.commit();

		session.close();
	}

	@Override
	public User getUserById(Long id) {

		Session session = SessionHelper.getSessionFactory().openSession();

		User user = (User) session.get(User.class, id);

		session.close();

		return user;
	}

	// search
//    @Override
//    public List<User> searchPartnersStarts(String text){
//
//    Session session =
//    SessionHelper.getSessionFactory().openSession();
//
//    Query query =
//    session.createQuery(
//    "from User where role='PARTNER' and fullName like :name");
//
//    query.setParameter("name", text+"%");
//
//    List<User> list = query.list();
//
//    session.close();
//
//    return list;
//
//    }

//    @Override
//    public List<User> searchPartnersContains(String text){
//
//    Session session =
//    SessionHelper.getSessionFactory().openSession();
//
//    Query query =
//    session.createQuery(
//    "from User where role='PARTNER' and fullName like :name");
//
//    query.setParameter("name", "%"+text+"%");
//
//    List<User> list = query.list();
//
//    session.close();
//
//    return list;
//
//    }
//    

	public List<User> getAllUsers() {

		Session session = SessionHelper.getSessionFactory().openSession();

		Query query = session.createQuery("from User where role='USER'");

		List<User> list = query.list();

		session.close();

		return list;

	}

	@Override
	public int getTotalUsersByRole(String role) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Query query = session.createQuery("select count(*) from User where role = :role");

		query.setParameter("role", role);

		Long count = (Long) query.uniqueResult();

		session.close();

		return count.intValue();
	}

	// tasks related
	@Override
	public List<Task> getTasksByStatus(String status) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Query query = session.createQuery("from Task where status = :status order by createdAt desc");

		query.setParameter("status", status);

		List<Task> list = query.list();

		session.close();

		return list;
	}

	@Override
	public int getTotalTasks() {

		Session session = SessionHelper.getSessionFactory().openSession();

		Query query = session.createQuery("select count(*) from Task");

		Long count = (Long) query.uniqueResult();

		session.close();

		return count.intValue();
	}

	public void updateTask(Task task) {

		Session session = null;
		Transaction tx = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			session.update(task);

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

	// fetch next task after aproving or rejecting insted of go back to list table
	// and open another task
	public Task getNextPendingTask(Long currentTaskId) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Query q = session.createQuery("from Task where status='DRAFT' and id > :id order by id asc");

		q.setParameter("id", currentTaskId);
		q.setMaxResults(1);

		Task task = (Task) q.uniqueResult();

		session.close();

		return task;
	}

	// users related
	@Override
	public List<User> getUsers(int page, int pageSize, String searchText, String searchType, String sortField,
			String sortOrder) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();
			String sortColumn = "createdAt";

			if ("id".equals(sortField)) {
				sortColumn = "id";
			} else if ("fullName".equals(sortField)) {
				sortColumn = "fullName";
			} else if ("email".equals(sortField)) {
				sortColumn = "email";
			} else if ("phone".equals(sortField)) {
				sortColumn = "phone";
			} else if ("country".equals(sortField)) {
				sortColumn = "country";
			} else if ("status".equals(sortField)) {
				sortColumn = "status";
			}

			String order = "DESC";

			if ("ASC".equalsIgnoreCase(sortOrder)) {
				order = "ASC";
			}

			String hql = "FROM User WHERE role='USER' ";

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				hql += " AND lower(fullName) LIKE :search";

				if ("starts".equals(searchType)) {

					searchText = searchText.toLowerCase().trim() + "%";

				} else {

					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			hql += " ORDER BY " + sortColumn + " " + order;

			Query q = session.createQuery(hql);

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			q.setFirstResult(page * pageSize);

			q.setMaxResults(pageSize);

			return q.list();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public int getTotalUsers(String searchText, String searchType) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			String hql = "SELECT COUNT(*) " + "FROM User WHERE role='USER'";

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				hql += " AND lower(fullName) LIKE :search";

				if ("starts".equals(searchType)) {

					searchText = searchText.toLowerCase().trim() + "%";

				} else {

					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			Query q = session.createQuery(hql);

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			Long count = (Long) q.uniqueResult();

			return count.intValue();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	/* -admin dashboard campine performance result- */

	@Override
	public List<TaskStats> getAllCampaignStats(int page, int pageSize, String searchText, String searchType,
			String sortField, String sortOrder) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			/* SAFE SORT */

			String sortColumn = "t.created_at";

			if ("taskTitle".equals(sortField)) {
				sortColumn = "t.title";
			} else if ("status".equals(sortField)) {
				sortColumn = "t.status";
			} else if ("totalSubmissions".equals(sortField)) {
				sortColumn = "ts.total_submissions";
			} else if ("accepted".equals(sortField)) {
				sortColumn = "ts.accepted";
			} else if ("pending".equals(sortField)) {
				sortColumn = "ts.pending";
			}

			/* ORDER */

			String order = "DESC";

			if ("ASC".equalsIgnoreCase(sortOrder)) {
				order = "ASC";
			}

			/* QUERY */

			String sql = "SELECT " + "t.id as taskId, " + "t.title as taskTitle, " + "t.status as status, "
					+ "t.reward as reward, " + "t.submission_limit as submissionLimit, "
					+ "u.full_name as partnerName, " +

					"IFNULL(ts.total_submissions,0) as totalSubmissions, " + "IFNULL(ts.accepted,0) as accepted, "
					+ "IFNULL(ts.rejected,0) as rejected, " + "IFNULL(ts.pending,0) as pending, " +

					"(t.submission_limit - " + "(IFNULL(ts.accepted,0) + IFNULL(ts.pending,0))) " + "as slotsLeft " +

					"FROM tasks t " +

					"LEFT JOIN users u " + "ON t.partner_id = u.id " +

					"LEFT JOIN task_stats ts " + "ON t.id = ts.task_id " +

					"WHERE 1=1 ";

			/* SEARCH */

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				sql += " AND lower(t.title) LIKE :search ";

				if ("starts".equals(searchType)) {

					searchText = searchText.toLowerCase().trim() + "%";

				} else {

					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			/* SORT */

			sql += " ORDER BY " + sortColumn + " " + order;

			SQLQuery q = session.createSQLQuery(sql);

			/* SCALARS */

			q.addScalar("taskId", org.hibernate.type.LongType.INSTANCE);

			q.addScalar("taskTitle", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("status", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("reward", org.hibernate.type.DoubleType.INSTANCE);

			q.addScalar("submissionLimit", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("partnerName", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("totalSubmissions", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("accepted", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("rejected", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("pending", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("slotsLeft", org.hibernate.type.IntegerType.INSTANCE);

			/* SEARCH PARAM */

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			/* PAGINATION */

			q.setFirstResult(page * pageSize);

			q.setMaxResults(pageSize);

			/* DTO */

			q.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(TaskStats.class));

			return q.list();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public int getTotalCampaignStats(String searchText, String searchType) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			String sql = "SELECT COUNT(*) FROM tasks t WHERE 1=1 ";

			boolean hasSearch = searchText != null && !searchText.trim().isEmpty();

			if (hasSearch) {

				sql += " AND lower(t.title) LIKE :search ";

				if ("starts".equals(searchType)) {

					searchText = searchText.toLowerCase().trim() + "%";

				} else {

					searchText = "%" + searchText.toLowerCase().trim() + "%";
				}
			}

			SQLQuery q = session.createSQLQuery(sql);

			if (hasSearch) {
				q.setParameter("search", searchText);
			}

			Number n = (Number) q.uniqueResult();

			return n == null ? 0 : n.intValue();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * ----------------------------------- CAMPAIGN DETAILS
	 * -----------------------------------
	 */

	@Override
	public TaskStats getCampaignDetails(Long taskId) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			String sql =

					"SELECT " +

							"t.id as taskId, " + "t.title as taskTitle, " + "t.description as description, "
							+ "t.steps as steps, " + "t.country as country, " + "t.url as url, " +

							"t.status as status, " + "t.reward as reward, " + "t.submission_limit as submissionLimit, "
							+

							"u.full_name as partnerName, " +

							"IFNULL(ts.total_submissions,0) as totalSubmissions, "
							+ "IFNULL(ts.accepted,0) as accepted, " + "IFNULL(ts.rejected,0) as rejected, "
							+ "IFNULL(ts.pending,0) as pending, " +

							"(t.submission_limit - " + "(IFNULL(ts.accepted,0) + IFNULL(ts.pending,0))) "
							+ "as slotsLeft " +

							"FROM tasks t " +

							"LEFT JOIN users u " + "ON t.partner_id = u.id " +

							"LEFT JOIN task_stats ts " + "ON t.id = ts.task_id " +

							"WHERE t.id=:taskId";

			SQLQuery q = session.createSQLQuery(sql);

			q.setParameter("taskId", taskId);

			q.addScalar("taskId", org.hibernate.type.LongType.INSTANCE);

			q.addScalar("taskTitle", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("description", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("steps", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("country", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("url", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("status", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("reward", org.hibernate.type.DoubleType.INSTANCE);

			q.addScalar("submissionLimit", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("partnerName", org.hibernate.type.StringType.INSTANCE);

			q.addScalar("totalSubmissions", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("accepted", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("rejected", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("pending", org.hibernate.type.IntegerType.INSTANCE);

			q.addScalar("slotsLeft", org.hibernate.type.IntegerType.INSTANCE);

			q.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(TaskStats.class));

			return (TaskStats) q.uniqueResult();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * ----------------------------------- UPDATE LIMIT
	 * -----------------------------------
	 */

	@Override
	public void updateCampaignLimit(Long taskId, Integer newLimit) {

		Session session = null;

		Transaction tx = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			tx = session.beginTransaction();

			String sql = "UPDATE tasks " + "SET submission_limit=:limit " + "WHERE id=:id";

			SQLQuery q = session.createSQLQuery(sql);

			q.setParameter("limit", newLimit);

			q.setParameter("id", taskId);

			q.executeUpdate();

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

	/*
	 * ----------------------------------- UPDATE STATUS
	 * -----------------------------------
	 */

	@Override
	public void updateCampaignStatus(Long taskId, String status) {

		Session session = null;

		Transaction tx = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			tx = session.beginTransaction();

			String sql = "UPDATE tasks " + "SET status=:status " + "WHERE id=:id";

			SQLQuery q = session.createSQLQuery(sql);

			q.setParameter("status", status);

			q.setParameter("id", taskId);

			q.executeUpdate();

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

	// block users
	@Override
	public void blockUser(Long id) {

		Session session = SessionHelper.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		String sql = "UPDATE users SET status='BLOCKED' WHERE id=:id";

		SQLQuery q = session.createSQLQuery(sql);

		q.setParameter("id", id);

		q.executeUpdate();

		tx.commit();
		session.close();
	}

	// unblockusers
	@Override
	public void unblockUser(Long id) {

		Session session = SessionHelper.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		String sql = "UPDATE users SET status='ACTIVE' WHERE id=:id";

		SQLQuery q = session.createSQLQuery(sql);

		q.setParameter("id", id);

		q.executeUpdate();

		tx.commit();
		session.close();
	}
}
