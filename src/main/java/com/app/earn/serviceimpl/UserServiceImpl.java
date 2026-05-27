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

		String result = userDao.registerPartner(user);

		if (!result.startsWith("Partner registered")) {
			return result;
		}

		User savedUser = userDao.findByEmail(user.getEmail());

		String otpValue = OtpUtil.generateOtp();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 2);

		UserOtp otp = new UserOtp();
		otp.setUserId(savedUser.getId());
		otp.setEmail(savedUser.getEmail());
		otp.setMobile(savedUser.getPhone());
		otp.setOtpValue(otpValue);
		otp.setPurpose("SIGNUP");
		otp.setIsVerified(false);
		otp.setCreatedAt(new Date());
		otp.setExpiresAt(cal.getTime());

		userOtpDao.saveOtp(otp);

//        MailSend.sendInfo(
//                savedUser.getEmail(),
//                "Earn Platform OTP Verification",
//                "Your OTP is: " + otpValue + "\nValid for 2 minutes."
//        );

		MailSend.sendInfo(savedUser.getEmail(), "EarnByApps OTP Verification", otpValue);
		return "OTP_SENT";
	}

	@Override
	public String registerUser(User user) {

		String result = userDao.registerUser(user);

		if (!"USER_REGISTERED".equals(result)) {
			return result;
		}

		User savedUser = userDao.findByEmail(user.getEmail());

		String otpValue = OtpUtil.generateOtp();

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MINUTE, 2);

		UserOtp otp = new UserOtp();

		otp.setUserId(savedUser.getId());
		otp.setEmail(savedUser.getEmail());
		otp.setMobile(savedUser.getPhone());

		otp.setOtpValue(otpValue);

		otp.setPurpose("SIGNUP");

		otp.setIsVerified(false);

		otp.setCreatedAt(new Date());

		otp.setExpiresAt(cal.getTime());

		userOtpDao.saveOtp(otp);

//        MailSend.sendInfo(
//        savedUser.getEmail(),
//        "Earn Platform User OTP",
//        "Your OTP is: "
//        + otpValue
//        + "\nValid for 2 minutes."
//        );

		MailSend.sendInfo(savedUser.getEmail(), "EarnByApps OTP Verification", otpValue);

		return "OTP_SENT";
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

		User savedUser = userDao.findByEmail(email);

		if (savedUser == null) {
			return "USER_NOT_FOUND";
		}

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

		otp.setUserId(savedUser.getId());

		otp.setEmail(savedUser.getEmail());

		otp.setMobile(savedUser.getPhone());

		otp.setOtpValue(otpValue);

		otp.setPurpose("SIGNUP");

		otp.setIsVerified(false);

		otp.setCreatedAt(new Date());

		otp.setExpiresAt(cal.getTime());

		userOtpDao.saveOtp(otp);

		MailSend.sendInfo(savedUser.getEmail(), "EarnByApps OTP Verification", otpValue);

		return "OTP_SENT";
	}

}
