package com.app.earn.serviceimpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.app.earn.dao.UserDao;
import com.app.earn.dao.UserOtpDao;
import com.app.earn.pojo.User;
import com.app.earn.pojo.UserOtp;
import com.app.earn.service.UserService;
import com.app.earn.util.MailSend;
import com.app.earn.util.OtpUtil;

public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private UserOtpDao userOtpDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserOtpDao(UserOtpDao userOtpDao) {
		this.userOtpDao = userOtpDao;
	}

	// ================= REGISTER =================
	@Override
	public String registerPartner(User user) {

//		String result = userDao.registerPartner(user);
//
//		if (!result.startsWith("Partner registered")) {
//			return result;
//		}
//
//		User savedUser = userDao.findByEmail(user.getEmail());
		
//		User existingUser =
//				userDao.findByEmail(user.getEmail());

		User existingUser =
				userDao.findByEmailAndRole(
				user.getEmail(),
				"PARTNER"
				);
				if(existingUser != null &&
				   "PARTNER".equals(existingUser.getRole())){

				    return "Partner already registered with this email";
				}

		String otpValue = OtpUtil.generateOtp();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 2);

		UserOtp otp = new UserOtp();
		otp.setUserId(null);
		otp.setEmail(user.getEmail());
		otp.setMobile(user.getPhone());
		otp.setOtpValue(otpValue);
		otp.setPurpose("SIGNUP");
		otp.setIsVerified(false);
		otp.setCreatedAt(new Date());
		otp.setExpiresAt(cal.getTime());

		UserOtp oldOtp =
				userOtpDao.getLatestOtpByEmail(user.getEmail());

				if(oldOtp != null){

				    userOtpDao.deleteOtp(oldOtp);
				}
		userOtpDao.saveOtp(otp);

//        MailSend.sendInfo(
//                savedUser.getEmail(),
//                "Earn Platform OTP Verification",
//                "Your OTP is: " + otpValue + "\nValid for 2 minutes."
//        );

		MailSend.sendInfo(user.getEmail(), "EarnByApps OTP Verification", otpValue);
		return "OTP_SENT";
	}

	
	@Override
	public void finalRegisterPartner(User user){

	    userDao.registerPartner(user);
	}
	
	@Override
	public String registerUser(User user) {

//		String result = userDao.registerUser(user);
//
//		if (!"USER_REGISTERED".equals(result)) {
//			return result;
//		}
//
//		User savedUser = userDao.findByEmail(user.getEmail());
		
//		User existingUser =
//				userDao.findByEmail(user.getEmail());
		User existingUser =
				userDao.findByEmailAndRole(
				user.getEmail(),
				"USER"
				);

				if(existingUser != null &&
				   "USER".equals(existingUser.getRole())){

				    return "User already registered with this email";
				}

		String otpValue = OtpUtil.generateOtp();

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MINUTE, 2);

		UserOtp otp = new UserOtp();

//		otp.setUserId(savedUser.getId());
//		otp.setEmail(savedUser.getEmail());
//		otp.setMobile(savedUser.getPhone());
		
		otp.setUserId(null);
		otp.setEmail(user.getEmail());
		otp.setMobile(user.getPhone());

		otp.setOtpValue(otpValue);

		otp.setPurpose("SIGNUP");

		otp.setIsVerified(false);

		otp.setCreatedAt(new Date());

		otp.setExpiresAt(cal.getTime());

		UserOtp oldOtp =
				userOtpDao.getLatestOtpByEmail(user.getEmail());

				if(oldOtp != null){

				    userOtpDao.deleteOtp(oldOtp);
				}
		userOtpDao.saveOtp(otp);

//        MailSend.sendInfo(
//        savedUser.getEmail(),
//        "Earn Platform User OTP",
//        "Your OTP is: "
//        + otpValue
//        + "\nValid for 2 minutes."
//        );

		MailSend.sendInfo(user.getEmail(), "EarnByApps OTP Verification", otpValue);

		return "OTP_SENT";
	}

	@Override
	public void finalRegisterUser(User user){

	    userDao.registerUser(user);
	}
	// ================= VERIFY OTP =================
	@Override
	public String verifyOtp(String email, String enteredOtp) {

		UserOtp otp = userOtpDao.getLatestOtpByEmail(email);

		if (otp == null) {
			return "OTP_NOT_FOUND";
		}

		if (otp.getExpiresAt().before(new Date())) {
			return "OTP_EXPIRED";
		}

		if (!otp.getOtpValue().equals(enteredOtp)) {
			return "INVALID_OTP";
		}

		userOtpDao.deleteOtp(otp);

		return "SUCCESS";
	}

	@Override
	public String login(String email, String password) {

		User user = userDao.findByEmailAndPassword(email, password);

		if (user == null) {
			return "INVALID";
		}

		if ("PARTNER".equals(user.getRole()) && !"ACTIVE".equalsIgnoreCase(user.getStatus())) {

			return "PENDING_APPROVAL";
		}

		if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
			return "BLOCKED";
		}

		// update last login 
		user.setLastLoginAt(new Date());

		userDao.updateUser(user);
		return user.getRole(); // ADMIN / PARTNER / USER
	}

	@Override
	public User getUser(String email, String password) {

		return userDao.findByEmailAndPassword(email, password);

	}

	// reset password
	@Override
	public void updatePassword(Long userId, String password) {

		userDao.updatePassword(userId, password);

	}

	@Override
	public String resendOtp(String email) {

//		User savedUser = userDao.findByEmail(email);
//
//		if (savedUser == null) {
//			return "USER_NOT_FOUND";
//		}

		// ================= CHECK LAST OTP =================

		UserOtp latestOtp = userOtpDao.getLatestOtpByEmail(email);

		if (latestOtp != null) {

			long diff = new Date().getTime() - latestOtp.getCreatedAt().getTime();

			// 30 seconds cooldown
			if (diff < 30000) {

				return "WAIT_30_SECONDS";
			}
		}

		// ================= GENERATE NEW OTP =================

		String otpValue = OtpUtil.generateOtp();

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MINUTE, 2);

		UserOtp otp = new UserOtp();

		otp.setUserId(null);

		otp.setEmail(email);

		otp.setMobile(null);

		otp.setOtpValue(otpValue);

		otp.setPurpose("SIGNUP");

		otp.setIsVerified(false);

		otp.setCreatedAt(new Date());

		otp.setExpiresAt(cal.getTime());

		if(latestOtp != null){

		    userOtpDao.deleteOtp(latestOtp);
		}
		userOtpDao.saveOtp(otp);

		MailSend.sendInfo(email, "EarnByApps OTP Verification", otpValue);

		return "OTP_SENT";
	}

}
