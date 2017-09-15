package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.UserDao;
import com.android.pojo.User;
import com.google.gson.Gson;

public class FindPropertyJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Gson gson;
	private Type propertyType;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		
		gson = new Gson();
		String json = "";
		String hostno = request.getParameter("hostno");
		hostno = hostno!=null?hostno:"0";
		int host = Integer.parseInt(hostno);
		UserDao dao = new UserDao();
		
		User user = dao.find(host);
		json = gson.toJson(user,User.class);
		
		out.write(json);
		
		out.flush();
		out.close();
	}

}
