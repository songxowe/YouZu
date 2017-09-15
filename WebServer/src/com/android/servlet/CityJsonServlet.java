package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.CityDao;
import com.android.pojo.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CityJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson;
	private Type cityListType;
	
	@Override
	public void init() throws ServletException {
		super.init();
		gson = new Gson();
		cityListType = new TypeToken<List<City>>() {
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
		
		CityDao cityDao = new CityDao();
		List<City> list = cityDao.find();
		String json = "";
		json = gson.toJson(list, cityListType);
	
		
		out.write(json);
		out.flush();
		out.close();
	}

}
