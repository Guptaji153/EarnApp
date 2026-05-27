package com.app.earn.util;

import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000); // 5-digit
        return String.valueOf(otp);
    }
}
