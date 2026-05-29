package com.app.earn.service;

import java.util.List;

import com.app.earn.pojo.User;

public interface UserService {

	String registerPartner(com.app.earn.pojo.User user);

	String verifyOtp(String email, String enteredOtp);

	// String login(String email, String password);
	String login(String email, String password, String role);

	User getUser(String email, String password, String role);

	String registerUser(User user);

	void finalRegisterUser(User user);

	void finalRegisterPartner(User user);

	// reset password
	// String resendOtp(String email);
	String resendOtp(String email, String purpose);

	String sendForgotPasswordOtp(String email, String role);

	User getUserByEmailAndRole(String email, String role);

	public void updatePassword(Long userId, String password);
}
