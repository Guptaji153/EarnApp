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
                .substring(req.getContextPath().length());


        // ✅ Get User object from session
        User loggedUser = null;

        if(session != null){
            loggedUser =
            (User) session.getAttribute("loggedUser");
        }


        boolean loggedIn =
                loggedUser != null;


//        boolean publicPage =
//
//                uri.contains("login.xhtml")
//             || uri.contains("verifyOtp.xhtml")
//             || uri.contains("partnerRegister.xhtml")
//             || uri.contains("index.xhtml")
//
//             || uri.contains("profile.xhtml")        
//             || uri.contains("resetPassword.xhtml")  
//             
//             || uri.contains("javax.faces.resource")
//             || uri.contains("resources")
//             || uri.contains("css")
//             || uri.contains("js");
        
        boolean publicPage =

                uri.contains("login.xhtml")
             || uri.contains("verifyOtp.xhtml")
             || uri.contains("partnerRegister.xhtml")
             || uri.contains("index.xhtml")
             || uri.contains("userRegistration.xhtml")

             || uri.contains("profile.xhtml")        
             || uri.contains("resetPassword.xhtml")  

             || uri.contains("javax.faces.resource")
             || uri.contains("resources")
             || uri.contains("css")
             || uri.contains("js")

             || uri.startsWith("/image");   // ✅ ADD THIS LINE


        // ✅ Allow public pages
        if (publicPage) {

            chain.doFilter(request,response);
            return;
        }


        // ✅ If not logged in → go login
        if (!loggedIn) {

            res.sendRedirect(
                    req.getContextPath()
                    + "/login.xhtml");

            return;
        }


        // ================= ROLE PROTECTION =================

        String role = loggedUser.getRole();


        // ADMIN pages
        if(uri.startsWith("/admin")
                && !"ADMIN".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }


        // PARTNER pages
        if(uri.startsWith("/partner")
                && !"PARTNER".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }


        // USER pages
        if(uri.startsWith("/user")
                && !"USER".equals(role)){

            res.sendRedirect(
                    req.getContextPath()
                    + "/index.xhtml");

            return;
        }


        chain.doFilter(request,response);

    }
}
