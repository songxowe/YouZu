package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.UserDao;
import com.android.pojo.User;
import com.android.util.MD5;
import com.google.gson.Gson;
public class LoginJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
	    UserDao userDao = new UserDao();
	    User user = userDao.find(username,MD5.getMD5(password));
	   
	    String json = "";
	    if (user == null) {
	      json = "{'userid':0}";
	    }else {
	      Gson gson = new Gson();
	      json = gson.toJson(user, User.class);
	    }
	    out.write(json);
		
		out.flush();
		out.close();
	}

}
