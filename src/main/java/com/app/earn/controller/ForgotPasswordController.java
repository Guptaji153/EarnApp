
package com.app.earn.controller;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.User;
import com.app.earn.service.UserService;

public class ForgotPasswordController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;

	private String role;

	private String enteredOtp;

	private String newPassword;

	private String confirmPassword;

	private UserService userService;

	// ================= EMAIL VALIDATION =================

	private boolean isValidEmail(String email) {

		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		return Pattern.matches(regex, email);
	}

	// ================= PASSWORD VALIDATION =================

	private boolean isValidPassword(String password) {

		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

		return Pattern.matches(regex, password);
	}

	// ================= SEND OTP =================

	public String sendOtp() {

		FacesContext context = FacesContext.getCurrentInstance();

		// EMAIL REQUIRED

		if (email == null || email.trim().isEmpty()) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email required", null));

			return null;
		}

		// EMAIL FORMAT

		if (!isValidEmail(email)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid email", null));

			return null;
		}

		// ROLE REQUIRED

		if (role == null || role.trim().isEmpty()) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select role", null));

			return null;
		}

		// SERVICE CALL

		String result = userService.sendForgotPasswordOtp(email, role);

		if ("ACCOUNT_NOT_FOUND".equals(result)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account not found", null));

			return null;
		}

		if ("OTP_SENT".equals(result)) {

			context.getExternalContext().getSessionMap().put("resetEmail", email);

			context.getExternalContext().getSessionMap().put("resetRole", role);

			context.getExternalContext().getSessionMap().put("otpPurpose", "RESET_PASSWORD");

			return "/verifyOtp.xhtml?faces-redirect=true";
		}

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, null));

		return null;
	}

	// ================= RESET PASSWORD =================

	public String resetPassword() {

		FacesContext context = FacesContext.getCurrentInstance();

		// ================= SESSION SECURITY CHECK =================

		String email = (String) context.getExternalContext().getSessionMap().get("resetEmail");

		String role = (String) context.getExternalContext().getSessionMap().get("resetRole");

		Boolean verified = (Boolean) context.getExternalContext().getSessionMap().get("resetVerified");

		// Prevent direct URL access
		if (email == null || role == null || verified == null || !verified) {

			return "/forgotPassword.xhtml?faces-redirect=true";
		}

		// ================= PASSWORD REQUIRED =================

		if (newPassword == null || newPassword.trim().isEmpty()) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password required", null));

			return null;
		}

		// ================= STRONG PASSWORD VALIDATION =================

		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

		if (!Pattern.matches(regex, newPassword)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Password must contain uppercase, lowercase, number, special character and minimum 8 characters",
					null));

			return null;
		}

		// ================= CONFIRM PASSWORD =================

		if (confirmPassword == null || confirmPassword.trim().isEmpty()) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirm password required", null));

			return null;
		}

		if (!newPassword.equals(confirmPassword)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));

			return null;
		}

		// ================= FETCH USER =================

		User user = userService.getUserByEmailAndRole(email, role);

		if (user == null) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found", null));

			return null;
		}

		// ================= UPDATE PASSWORD =================

		userService.updatePassword(user.getId(), newPassword);

		// ================= CLEAR SESSION =================

		context.getExternalContext().getSessionMap().remove("resetEmail");

		context.getExternalContext().getSessionMap().remove("resetRole");

		context.getExternalContext().getSessionMap().remove("otpPurpose");

		context.getExternalContext().getSessionMap().remove("resetVerified");

		// ================= SUCCESS MESSAGE =================

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated successfully", null));

		return "/login.xhtml?faces-redirect=true";
	}

	// ================= GETTERS SETTERS =================

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEnteredOtp() {
		return enteredOtp;
	}

	public void setEnteredOtp(String enteredOtp) {
		this.enteredOtp = enteredOtp;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {

		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {

		this.confirmPassword = confirmPassword;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {

		this.userService = userService;
	}
}
