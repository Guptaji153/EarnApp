package com.app.earn.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.app.earn.pojo.User;

public class AuthFilter implements Filter {

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)

            throws IOException, ServletException {

        HttpServletRequest req =
                (HttpServletRequest) request;

        HttpServletResponse res =
                (HttpServletResponse) response;

        HttpSession session =
                req.getSession(false);

        String uri =
                req.getRequestURI()
                .substring(
                req.getContextPath().length()
                );

        // ================= GET LOGGED USER =================

        User loggedUser = null;

        if(session != null){

            loggedUser =
            (User) session
            .getAttribute("loggedUser");
        }

        boolean loggedIn =
                loggedUser != null;

        // ================= PUBLIC PAGES =================

        boolean publicPage =

                uri.contains("login.xhtml")

             || uri.contains("verifyOtp.xhtml")

             || uri.contains("partnerRegister.xhtml")

             || uri.contains("userRegistration.xhtml")

             || uri.contains("forgotPassword.xhtml")

             || uri.contains("index.xhtml")

             || uri.contains("profile.xhtml")

            // || uri.contains("resetPassword.xhtml")
             || uri.contains("adminLogin.xhtml")
             || uri.contains("javax.faces.resource")

             || uri.contains("resources")

             || uri.contains("css")

             || uri.contains("js")

             || uri.startsWith("/image");

        // ================= ALLOW PUBLIC =================

        if(publicPage){

            chain.doFilter(request,response);

            return;
        }

        // ================= RESET PASSWORD GLOBAL SECURITY =================

        if(uri.contains("resetPasswordGlobal.xhtml")){

            boolean resetVerified = false;

            if(session != null){

                Boolean verified =
                (Boolean) session.getAttribute(
                "resetVerified"
                );

                resetVerified =
                verified != null
                &&
                verified;
            }

            // If OTP not verified
            if(!resetVerified){

                res.sendRedirect(
                req.getContextPath()
                + "/forgotPassword.xhtml"
                );

                return;
            }

            chain.doFilter(request,response);

            return;
        }

        // ================= NOT LOGGED IN =================

        if(!loggedIn){

            res.sendRedirect(
                    req.getContextPath()
                    + "/login.xhtml");

            return;
        }

        // ================= ROLE PROTECTION =================

        String role =
                loggedUser.getRole();

        // ================= ADMIN =================

        if(uri.startsWith("/admin")
                &&
           !"ADMIN".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }

        // ================= PARTNER =================

        if(uri.startsWith("/partner")
                &&
           !"PARTNER".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }

        // ================= USER =================

        if(uri.startsWith("/user")
                &&
           !"USER".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }

        // ================= ALLOW =================

        chain.doFilter(request,response);
    }
}
