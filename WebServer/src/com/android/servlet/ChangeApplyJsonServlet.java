package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.ApplyDao;
import com.android.pojo.Apply;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChangeApplyJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;
	private Type applyType;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		gson = new Gson();
		applyType = new TypeToken<List<Apply>>(){
		}.getType();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String json = "";
		String puser = request.getParameter("puser");
		
		ApplyDao applyDao=new ApplyDao();
		
		Apply apply = applyDao.pollingFind(puser);
		json = gson.toJson(apply,Apply.class);
		out.write(json);
		
		
		out.flush();
		out.close();
	}

}
