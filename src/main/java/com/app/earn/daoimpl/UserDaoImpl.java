package com.app.earn.daoimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.UserDao;
import com.app.earn.pojo.User;
import com.app.earn.util.SessionHelper;

public class UserDaoImpl implements UserDao {

	@Override
	public String registerPartner(User user) {

		Session session = null;
		Transaction tx = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// Check if email already exists
			Query query = session.createQuery("from User where email = :email and role = :role");

			query.setParameter("email", user.getEmail());

			query.setParameter("role", "PARTNER");

			List list = query.list();

			if (list != null && !list.isEmpty()) {

				return "Partner already registered with this email";
			}
			user.setRole("PARTNER");
			user.setStatus("PENDING"); // 🔴 Pending admin approval
			user.setCreatedAt(new java.util.Date());

			session.save(user);
			tx.commit();

			return "Partner registered successfully. Waiting for admin approval.";

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return "Registration failed";

		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public String registerUser(User user) {

		Session session = null;
		Transaction tx = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			tx = session.beginTransaction();

			Query query = session.createQuery("from User where email=:email and role=:role");

			query.setParameter("email", user.getEmail());

			query.setParameter("role", "USER");

			List list = query.list();

			if (list != null && !list.isEmpty()) {

				return "User already registered with this email";
			}

			user.setRole("USER");

			user.setStatus("ACTIVE");

			user.setCreatedAt(new java.util.Date());

			session.save(user);

			tx.commit();

			return "USER_REGISTERED";

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}

			e.printStackTrace();

			return "Registration failed";

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public User findByEmail(String email) {

		Session session = null;
		try {
			session = SessionHelper.getSessionFactory().openSession();

			Query query = session.createQuery("from User where email = :email");
			query.setParameter("email", email);

			return (User) query.uniqueResult();

		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public User findByEmailAndRole(String email, String role) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			Query query = session.createQuery("from User where email=:email and role=:role");

			query.setParameter("email", email);

			query.setParameter("role", role);

			return (User) query.uniqueResult();

		} finally {

			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public User findByEmailPasswordAndRole(String email, String password, String role) {

		Session session = null;

		try {

			session = SessionHelper.getSessionFactory().openSession();

			Query query = session
					.createQuery("from User where email = :email " + "and password = :password " + "and role = :role");

			query.setParameter("email", email);

			query.setParameter("password", password);

			query.setParameter("role", role);

			return (User) query.uniqueResult();

		} finally {

			if (session != null) {

				session.close();
			}
		}
	}

	@Override
	public void updateUser(User user) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		// session.update(user);
		User existing = (User) session.get(User.class, user.getId());

		if (existing != null) {

			existing.setLastLoginAt(user.getLastLoginAt());

			session.update(existing);
		}

		tx.commit();

		session.close();
	}

	// Admin

	// reset password

	@Override
	public void updatePassword(Long userId, String password) {

		Session session = SessionHelper.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		User user = (User) session.get(User.class, userId);

		user.setPassword(password);

		session.update(user);

		tx.commit();

		session.close();
	}
}
