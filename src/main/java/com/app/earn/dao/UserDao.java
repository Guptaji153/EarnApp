package com.app.earn.dao;

import java.util.List;

import com.app.earn.pojo.User;

public interface UserDao {

	String registerPartner(User user);

	User findByEmail(String email);

	User findByEmailAndRole(String email, String role);

	User findByEmailPasswordAndRole(String email, String password, String role);

	String registerUser(User user);

	// update last login
	void updateUser(User user);
	// admin

	// reset password

	public void updatePassword(Long userId, String password);

}
