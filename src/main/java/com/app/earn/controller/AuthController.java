package com.app.earn.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.User;
import com.app.earn.service.UserService;

public class AuthController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private String role;

	private UserService userService;

	// ================= LOGIN =================

	public String login() {

		FacesContext context = FacesContext.getCurrentInstance();

		// String role = userService.login(email, password);

		String loginResult = userService.login(email, password, role);

		if ("INVALID".equals(loginResult)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", null));

			return null;
		}

		if ("PENDING_APPROVAL".equals(loginResult)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Waiting for admin approval", null));

			return null;
		}

		if ("BLOCKED".equals(loginResult)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account blocked", null));

			return null;
		}

		// Store session
//        FacesContext.getCurrentInstance()
//                .getExternalContext()
//                .getSessionMap()
//                .put("loggedUser", email);

		// User user = userService.getUser(email, password);

		User user = userService.getUser(email, password, role);

		if (user == null) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", null));

			return null;
		}

		// store FULL USER OBJECT
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedUser", user);

		// Redirect based on role

		if ("ADMIN".equals(role))
			return "/admin/adminDashboard.xhtml?faces-redirect=true";

		if ("PARTNER".equals(role))
			return "/partner/partnerDashboard.xhtml?faces-redirect=true";

		// return "/user/home.xhtml?faces-redirect=true";
		return "/user/userDashboard.xhtml?faces-redirect=true";
	}

	// ================= PREVENT BACK BUTTON =================

	public void preventBack() {

		FacesContext fc = FacesContext.getCurrentInstance();

		fc.getExternalContext().setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");

		fc.getExternalContext().setResponseHeader("Pragma", "no-cache");

		fc.getExternalContext().setResponseHeader("Expires", "0");
	}

	// ================= ALREADY LOGGED IN =================

	public void checkAlreadyLoggedIn() {

		User loggedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("loggedUser");

		if (loggedUser != null) {

			try {

				String role = loggedUser.getRole();

				if ("ADMIN".equals(role)) {

					FacesContext.getCurrentInstance().getExternalContext().redirect("admin/adminDashboard.xhtml");

				} else if ("PARTNER".equals(role)) {

					FacesContext.getCurrentInstance().getExternalContext().redirect("partner/partnerDashboard.xhtml");

				} else {

					FacesContext.getCurrentInstance().getExternalContext().redirect("user/userDashboard.xhtml");

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ================= LOGOUT =================

	public String logout() {

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/login.xhtml?faces-redirect=true";
	}

	// reset

	private String newPassword;

	public String updatePassword() {

		User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedUser");

		userService.updatePassword(user.getId(), newPassword);

		user.setPassword(newPassword);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Updated Successfully", null));

		return null;
	}

	// getters setters

	public String getEmail() {
		return email;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
