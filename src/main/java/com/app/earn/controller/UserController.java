package com.app.earn.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;

import com.app.earn.pojo.User;
import com.app.earn.service.UserService;

public class UserController implements Serializable {
	private static final long serialVersionUID = 1L;

	private User userBean;
	private UserService userService;
	private String enteredOtp;
	private String email;
	private String password;
	private String role;

//	@PostConstruct
//	public void init() {
//		System.out.println("Service injected: " + (userService != null));
//	}

	@PostConstruct
	public void initialize() {

		if (userBean == null) {

			userBean = new User();
		}
	}
	/*
	 * ----------------------------- VALIDATION HELPERS
	 * -----------------------------
	 */

	private boolean isValidEmail(String email) {

		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		return Pattern.matches(regex, email);
	}

	private boolean isValidPhone(String phone) {

		String regex = "^[1-9][0-9]{9}$";

		return Pattern.matches(regex, phone);
	}

	private boolean isValidPassword(String password) {

		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

		return Pattern.matches(regex, password);
	}

	private boolean isValidName(String name) {

		String regex = "^[A-Za-z ]+$";

		return Pattern.matches(regex, name);
	}

	// ================= REGISTER =================
	public String registerPartner() {

		if (userBean.getEmail() != null) {
			userBean.setEmail(userBean.getEmail().trim());
		}

		if (userBean.getFullName() != null) {
			userBean.setFullName(userBean.getFullName().trim());
		}

		if (userBean.getPhone() != null) {
			userBean.setPhone(userBean.getPhone().trim());
		}

		FacesContext context = FacesContext.getCurrentInstance();

		if (userBean.getEmail() == null || userBean.getEmail().trim().isEmpty()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email required", null));
			return null;
		}

		boolean hasError = false;

		/*
		 * ----------------------------- EMAIL -----------------------------
		 */

		if (userBean.getEmail() == null || userBean.getEmail().trim().isEmpty()) {

			context.addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));

			hasError = true;

		} else if (!isValidEmail(userBean.getEmail())) {

			context.addMessage("email",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid email address", null));

			hasError = true;
		}

		/*
		 * ----------------------------- PASSWORD -----------------------------
		 */

		if (userBean.getPassword() == null || userBean.getPassword().trim().isEmpty()) {

			context.addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));

			hasError = true;

		} else if (!isValidPassword(userBean.getPassword())) {

			context.addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Password must contain upper, lower, number and minimum 6 characters", null));

			hasError = true;
		}

		/*
		 * ----------------------------- FULL NAME -----------------------------
		 */

		if (userBean.getFullName() == null || userBean.getFullName().trim().isEmpty()) {

			context.addMessage("fullName",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Full name is required", null));

			hasError = true;

		} else if (!isValidName(userBean.getFullName())) {

			context.addMessage("fullName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Name cannot contain numbers or special characters", null));

			hasError = true;
		}

		/*
		 * ----------------------------- PHONE -----------------------------
		 */

		if (userBean.getPhone() == null || userBean.getPhone().trim().isEmpty()) {

			context.addMessage("phone",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required", null));

			hasError = true;

		} else if (!isValidPhone(userBean.getPhone())) {

			context.addMessage("phone", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Phone must be 10 digits and cannot start with 0", null));

			hasError = true;
		}

		/*
		 * ----------------------------- COUNTRY -----------------------------
		 */

		if (userBean.getCountry() == null || userBean.getCountry().trim().isEmpty()) {

			context.addMessage("country", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select country", null));

			hasError = true;
		}

		/*
		 * ----------------------------- STOP IF ANY ERROR -----------------------------
		 */

		if (hasError) {

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please correct the highlighted fields", null));

			return null;
		}

		String result = userService.registerPartner(userBean);

//		if ("OTP_SENT".equals(result)) {
//
//			context.getExternalContext().getSessionMap().put("otpEmail", userBean.getEmail());
//
//			return "/verifyOtp.xhtml?faces-redirect=true";
//		}

		if ("OTP_SENT".equals(result)) {

			context.getExternalContext().getSessionMap().put("pendingPartner", userBean);

			context.getExternalContext().getSessionMap().put("otpEmail", userBean.getEmail());

			return "/verifyOtp.xhtml?faces-redirect=true";
		}
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, null));

		return null;
	}

	public String registerUser() {

		if (userBean.getEmail() != null) {
			userBean.setEmail(userBean.getEmail().trim());
		}

		if (userBean.getFullName() != null) {
			userBean.setFullName(userBean.getFullName().trim());
		}

		if (userBean.getPhone() != null) {
			userBean.setPhone(userBean.getPhone().trim());
		}

		FacesContext context = FacesContext.getCurrentInstance();

		if (userBean.getEmail() == null || userBean.getEmail().trim().isEmpty()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email required", null));
			return null;
		}

		boolean hasError = false;

		/*
		 * ----------------------------- EMAIL -----------------------------
		 */

		if (userBean.getEmail() == null || userBean.getEmail().trim().isEmpty()) {

			context.addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));

			hasError = true;

		} else if (!isValidEmail(userBean.getEmail())) {

			context.addMessage("email",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid email address", null));

			hasError = true;
		}

		/*
		 * ----------------------------- PASSWORD -----------------------------
		 */

		if (userBean.getPassword() == null || userBean.getPassword().trim().isEmpty()) {

			context.addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));

			hasError = true;

		} else if (!isValidPassword(userBean.getPassword())) {

			context.addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Password must contain upper, lower, number and minimum 6 characters", null));

			hasError = true;
		}

		/*
		 * ----------------------------- FULL NAME -----------------------------
		 */

		if (userBean.getFullName() == null || userBean.getFullName().trim().isEmpty()) {

			context.addMessage("fullName",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Full name is required", null));

			hasError = true;

		} else if (!isValidName(userBean.getFullName())) {

			context.addMessage("fullName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Name cannot contain numbers or special characters", null));

			hasError = true;
		}

		/*
		 * ----------------------------- PHONE -----------------------------
		 */

		if (userBean.getPhone() == null || userBean.getPhone().trim().isEmpty()) {

			context.addMessage("phone",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number is required", null));

			hasError = true;

		} else if (!isValidPhone(userBean.getPhone())) {

			context.addMessage("phone", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Phone must be 10 digits and cannot start with 0", null));

			hasError = true;
		}

		/*
		 * ----------------------------- COUNTRY -----------------------------
		 */

		if (userBean.getCountry() == null || userBean.getCountry().trim().isEmpty()) {

			context.addMessage("country", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select country", null));

			hasError = true;
		}

		/*
		 * ----------------------------- STOP IF ANY ERROR -----------------------------
		 */

		if (hasError) {

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please correct the highlighted fields", null));

			return null;
		}

		String result = userService.registerUser(userBean);

//		if ("OTP_SENT".equals(result)) {
//
//			context.getExternalContext().getSessionMap().put("otpEmail", userBean.getEmail());
//
//			return "/verifyOtp.xhtml?faces-redirect=true";
//		}

		if ("OTP_SENT".equals(result)) {

			context.getExternalContext().getSessionMap().put("pendingUser", userBean);

			context.getExternalContext().getSessionMap().put("otpEmail", userBean.getEmail());

			return "/verifyOtp.xhtml?faces-redirect=true";
		}
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, null));

		return null;
	}

	// ================= VERIFY OTP =================
	public String verifyOtp() {

		FacesContext context = FacesContext.getCurrentInstance();

		// String email = (String)
		// context.getExternalContext().getSessionMap().get("otpEmail");

		String purpose = (String) context.getExternalContext().getSessionMap().get("otpPurpose");

		String email = null;

		if ("RESET_PASSWORD".equals(purpose)) {

			email = (String) context.getExternalContext().getSessionMap().get("resetEmail");

		} else {

			email = (String) context.getExternalContext().getSessionMap().get("otpEmail");
		}

		if (email == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session expired", null));
			return null;
		}

		String result = userService.verifyOtp(email, enteredOtp);

		switch (result) {

//		case "SUCCESS":
//			context.getExternalContext().getSessionMap().remove("otpEmail");
//			return "/login.xhtml?faces-redirect=true";

		case "SUCCESS":

			String otpPurpose = (String) context.getExternalContext().getSessionMap().get("otpPurpose");

			if ("RESET_PASSWORD".equals(otpPurpose)) {

				context.getExternalContext().getSessionMap().put("resetVerified", true);

				return "/resetPasswordGlobal.xhtml?faces-redirect=true";
			}

			User pendingUser = (User) context.getExternalContext().getSessionMap().get("pendingUser");

			User pendingPartner = (User) context.getExternalContext().getSessionMap().get("pendingPartner");

			// debug
			System.out.println("Pending User = " + pendingUser);

			System.out.println("Pending Partner = " + pendingPartner);
			if (pendingUser != null && pendingPartner == null) {

				userService.finalRegisterUser(pendingUser);

				context.getExternalContext().getSessionMap().remove("pendingUser");
			}

			if (pendingPartner != null && pendingUser == null) {

				userService.finalRegisterPartner(pendingPartner);

				context.getExternalContext().getSessionMap().remove("pendingPartner");
			}

			context.getExternalContext().getSessionMap().remove("otpEmail");
			context.getExternalContext().getSessionMap().remove("otpPurpose");

			userBean = new User();
			return "/login.xhtml?faces-redirect=true";

		case "OTP_EXPIRED":
			context.getExternalContext().getSessionMap().remove("pendingUser");

			context.getExternalContext().getSessionMap().remove("pendingPartner");
			context.getExternalContext().getSessionMap().remove("otpPurpose");

			context.getExternalContext().getSessionMap().remove("otpEmail");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP expired", null));
			break;

		case "INVALID_OTP":
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid OTP", null));
			break;

		default:
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP not found", null));
		}

		return null;
	}

	// resend otp

	public String resendOtp() {

		FacesContext context = FacesContext.getCurrentInstance();

		// String email = (String)
		// context.getExternalContext().getSessionMap().get("otpEmail");

		String purpose = (String) context.getExternalContext().getSessionMap().get("otpPurpose");

		String email = null;

		if ("RESET_PASSWORD".equals(purpose)) {

			email = (String) context.getExternalContext().getSessionMap().get("resetEmail");

		} else {

			email = (String) context.getExternalContext().getSessionMap().get("otpEmail");
		}

		if (email == null) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session expired", null));

			return null;
		}

		// String result = userService.resendOtp(email);

		String otpPurpose = (String) context.getExternalContext().getSessionMap().get("otpPurpose");

		if (otpPurpose == null) {

			otpPurpose = "SIGNUP";
		}

		String result = userService.resendOtp(email, otpPurpose);

		if ("OTP_SENT".equals(result)) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OTP resent successfully", null));

		}

		else if ("WAIT_30_SECONDS".equals(result)) {

			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Please wait 30 seconds before resending", null));
		}
		return null;
	}
//    public String login() {
//
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (email == null || email.trim().isEmpty()) {
//            context.addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "Email required", null));
//            return null;
//        }
//
//        if (password == null || password.trim().isEmpty()) {
//            context.addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "Password required", null));
//            return null;
//        }
//
//        String result = userService.login(email, password);
//
//        switch (result) {
//
//            case "ADMIN":
//                context.getExternalContext()
//                        .getSessionMap()
//                        .put("loggedUser", email);
//                return "/admin/adminDashboard.xhtml?faces-redirect=true";
//
//            case "PARTNER":
//                context.getExternalContext()
//                        .getSessionMap()
//                        .put("loggedUser", email);
//                return "/partner/partnerDashboard.xhtml?faces-redirect=true";
//
//            case "USER":
//                context.getExternalContext()
//                        .getSessionMap()
//                        .put("loggedUser", email);
//                return "/user/home.xhtml?faces-redirect=true";
//
//            case "PENDING_APPROVAL":
//                context.addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_WARN,
//                                "Account waiting for admin approval", null));
//                break;
//
//            case "BLOCKED":
//                context.addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                                "Account is blocked", null));
//                break;
//
//            default:
//                context.addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                                "Invalid credentials", null));
//        }
//
//        return null;
//    }

	// getters setters
	public User getUser() {
		return userBean;
	}

	public void setUser(User user) {
		this.userBean = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getEnteredOtp() {
		return enteredOtp;
	}

	public void setEnteredOtp(String enteredOtp) {
		this.enteredOtp = enteredOtp;
	}

	public String getEmail() {
		return email;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
