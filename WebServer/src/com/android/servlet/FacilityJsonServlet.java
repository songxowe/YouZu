package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.FacilityDao;
import com.android.pojo.Facility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FacilityJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Gson gson;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String propertynos = request.getParameter("propertyno");
		String tv = request.getParameter("tv");
		String aircondition = request.getParameter("aircondition");
		String washer = request.getParameter("washer");
		String network = request.getParameter("network");
		String dryer = request.getParameter("dryer");
		String computer = request.getParameter("computer");
		
		Integer propertyno = Integer.parseInt(propertynos);
		
		Facility facility = new Facility();
		FacilityDao facilityDao = new FacilityDao();
		facility = facilityDao.findFacility(propertyno);
		
		String json = "";
		
		gson = new Gson();
		json = gson.toJson(facility, Facility.class);
		
		out.write(json);
		out.flush();
		out.close();
	}

}
