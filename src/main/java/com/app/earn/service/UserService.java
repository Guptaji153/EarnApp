package com.app.earn.service;

import java.util.List;

import com.app.earn.pojo.User;

public interface UserService {

    String registerPartner(com.app.earn.pojo.User user);

    String verifyOtp(String email, String enteredOtp);
    String login(String email, String password);
    User getUser(String email,String password);
    
    String registerUser(User user);
    
   
    // reset password
    String resendOtp(String email);
    public void updatePassword(Long userId,String password);
}
