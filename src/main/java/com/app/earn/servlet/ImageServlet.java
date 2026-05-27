package com.app.earn.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.app.earn.dao.UserTaskDao;
import com.app.earn.daoimpl.UserTaskDaoImpl;
import com.app.earn.pojo.User;

public class ImageServlet extends HttpServlet {

	private static final String BASE_PATH = "D:/earn_storage/";
	private UserTaskDao dao = new UserTaskDaoImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String path = request.getParameter("path");

//	    if (path == null || path.contains("..") || path.startsWith("/")) {
//	        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//	        return;
//	    }

	    if (path == null || path.trim().isEmpty()) {
	        response.sendRedirect(request.getContextPath() + "/images/no-proof.png");
	        return;
	    }

	    if (path.contains("..") || path.startsWith("/")) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	        return;
	    }
	    
	    HttpSession session = request.getSession(false);

	    if (session == null) {
	        response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        return;
	    }

	    User user = (User) session.getAttribute("loggedUser");

	    if (user == null) {
	        response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        return;
	    }

	    boolean allowed = dao.canUserAccessProof(
	        user.getId(),
	        user.getRole(),
	        path
	    );

	    if (!allowed) {
	        response.sendError(HttpServletResponse.SC_FORBIDDEN);
	        return;
	    }

	    File file = new File(BASE_PATH + path);

//	    if (!file.exists()) {
//	        response.sendError(HttpServletResponse.SC_NOT_FOUND);
//	        return;
//	    }
	    
	    if (!file.exists()) {

	        // redirect to default image
	        response.sendRedirect(request.getContextPath() + "/images/no-proof.png");
	        return;
	    }

	    String mime = getServletContext().getMimeType(file.getName());

	    if (mime == null) {
	        mime = "application/octet-stream";
	    }

	    response.setContentType(mime);
	    response.setContentLengthLong(file.length());
	    response.setHeader("Cache-Control", "public, max-age=86400");

	    try (FileInputStream fis = new FileInputStream(file);
	         OutputStream os = response.getOutputStream()) {

	        byte[] buffer = new byte[4096];
	        int bytes;

	        while ((bytes = fis.read(buffer)) != -1) {
	            os.write(buffer, 0, bytes);
	        }
	    }
	}
}