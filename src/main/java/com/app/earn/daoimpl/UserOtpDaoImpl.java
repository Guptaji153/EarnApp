package com.app.earn.daoimpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.app.earn.dao.UserOtpDao;
import com.app.earn.pojo.User;
import com.app.earn.pojo.UserOtp;
import com.app.earn.util.SessionHelper;

public class UserOtpDaoImpl implements UserOtpDao {

	@Override
	public void saveOtp(UserOtp otp) {
		Session session = null;
		Transaction tx = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(otp);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public UserOtp getLatestOtpByEmail(String email) {

		Session session = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();

			Query query = session.createQuery("from UserOtp where email = :email order by createdAt desc");

			query.setParameter("email", email);
			query.setMaxResults(1);

			return (UserOtp) query.uniqueResult();

		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public void deleteOtp(UserOtp otp) {

		Session session = null;
		Transaction tx = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			session.delete(otp);

			tx.commit();

		} catch (Exception e) {

			if (tx != null)
				tx.rollback();

			throw e;

		} finally {

			if (session != null)
				session.close();
		}
	}


	@Override
    		public UserOtp getLatestOtpByEmailAndPurpose(
    		        String email,
    		        String purpose) {

    		    Session session = null;

    		    try {

    		        session =
    		        SessionHelper
    		        .getSessionFactory()
    		        .openSession();

    		        Query query =
    		        session.createQuery(
    		        "from UserOtp " +
    		        "where email = :email " +
    		        "and purpose = :purpose " +
    		        "order by createdAt desc"
    		        );

    		        query.setParameter(
    		        "email",
    		        email
    		        );

    		        query.setParameter(
    		        "purpose",
    		        purpose
    		        );

    		        query.setMaxResults(1);

    		        return (UserOtp)
    		        query.uniqueResult();

    		    } finally {

    		        if(session != null){

    		            session.close();
    		        }
    		    }
    		}

}
