package com.app.earn.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSend {

    private static final String FROM_EMAIL =
            "email";

    private static final String SMTP_USERNAME =
            "userName";

    private static final String SMTP_PASSWORD =
            "passWord";

    public static String sendInfo(
            String toEmail,
            String subject,
            String data) {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host",
                "smtp-relay.brevo.com");

        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
                props,
                new Authenticator() {

            protected PasswordAuthentication
            getPasswordAuthentication() {

                return new PasswordAuthentication(
                        SMTP_USERNAME,
                        SMTP_PASSWORD
                );
            }
        });

        try {

            Message message =
                    new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(FROM_EMAIL));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject(subject);

            // HTML EMAIL
//            String html =
//                    "<h2>Earn Platform OTP</h2>"
//                  + "<h3>" + data + "</h3>"
//                  + "<p>OTP valid for 2 minutes.</p>";
            String html =
            	    "<div style='font-family:Arial,sans-serif;"
            	  + "max-width:600px;margin:auto;"
            	  + "padding:20px;border:1px solid #ddd;"
            	  + "border-radius:10px;'>"

            	  + "<h1 style='color:#4F46E5;text-align:center;'>"
            	  + "EarnByApps</h1>"

            	  + "<p>Hello User,</p>"

            	  + "<p>Your OTP for verification is:</p>"

            	  + "<div style='text-align:center;"
            	  + "margin:30px 0;'>"

            	  + "<span style='font-size:32px;"
            	  + "letter-spacing:5px;"
            	  + "font-weight:bold;"
            	  + "color:#111827;'>"
            	  + data
            	  + "</span>"

            	  + "</div>"

            	  + "<p>This OTP is valid for "
            	  + "<b>2 minutes</b>.</p>"

            	  + "<p>If you did not request this,"
            	  + " please ignore this email.</p>"

            	  + "<hr>"

            	  + "<p style='font-size:12px;color:gray;'>"
            	  + "© 2026 EarnByApps. All rights reserved."
            	  + "</p>"

            	  + "</div>";

            message.setContent(html, "text/html");

            Transport.send(message);

            return "MAIL_SENT";

        } catch (Exception e) {

            e.printStackTrace();

            return e.getMessage();
        }
    }
}