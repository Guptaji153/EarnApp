package com.app.earn.dao;

import com.app.earn.pojo.UserOtp;

public interface UserOtpDao {
    void saveOtp(UserOtp otp);
    UserOtp getLatestOtpByEmail(String email);
    void deleteOtp(UserOtp otp);

}
