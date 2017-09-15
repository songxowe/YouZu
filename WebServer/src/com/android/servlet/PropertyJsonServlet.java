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
import com.android.dao.PropertyDao;
import com.android.dao.UserDao;
import com.android.pojo.Facility;
import com.android.pojo.Property;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class PropertyJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Gson gson;
	private Type propertyType;
	
	
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		gson = new Gson();
		propertyType = new TypeToken<List<Property>>(){
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
		
		String action = request.getParameter("action");
		action = (action!=null) ? action : "find";
		
		String city = request.getParameter("city");
		String address = request.getParameter("address");
		String homesize = request.getParameter("homesize");
		String prices = request.getParameter("price");
		String userids = request.getParameter("userid");
		String status = request.getParameter("status");
		String photo = request.getParameter("photo");
		String leaseTrem = request.getParameter("leaseTrem");
		String startDate = request.getParameter("startDate");
		String expireDate = request.getParameter("expireDate");
		String totalRent = request.getParameter("totalRent");
		String deposit = request.getParameter("deposit");
		String propertyname = request.getParameter("propertyname");
		String score = request.getParameter("score");
		
		String model = request.getParameter("model");
		
		
		String tv = request.getParameter("tv");
		String air = request.getParameter("air");
		String washer = request.getParameter("washer");
		String network = request.getParameter("network");
		String computer = request.getParameter("computer");
		String dryer = request.getParameter("dryer");
		
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
		String d = request.getParameter("d");
		String f = request.getParameter("f");
		
		address = (address!=null) ? address : "";
		homesize = (homesize!=null) ? homesize : "";
		prices = (prices!=null) ? prices : "";
		userids = (userids!=null) ? userids : "";
		status = (status!=null) ? status : "待租";
		photo = (photo!=null) ? photo : "images/house/king.jpg";
		leaseTrem = (leaseTrem!=null) ? leaseTrem : "0";
		startDate = (startDate!=null) ? startDate : "0";
		expireDate = (expireDate!=null) ? expireDate : "0";
		totalRent = (totalRent!=null) ? totalRent : "0";
		deposit = (deposit!=null) ? deposit : "0";
		propertyname = (propertyname!=null) ? propertyname : "";
		score = (score!=null) ? score : "3.0";
		
		Double price = 0.0;
		int userid = 0;
		
		if(userids.trim().length()>0){
			userid = Integer.parseInt(userids);
		}
		if(prices.trim().length()>0){
			price = Double.parseDouble(prices);
		}
		String json = "";
		PropertyDao propertyDao = new PropertyDao();
		if(action.equals("find")){
			int num = 1;
			if(a!=null | b!=null | c!=null | d!=null | f!=null){
				num +=1;
			}
			if(prices.trim().length()>0){
				price = Double.parseDouble(prices);
				num =3;
				if(!(a!=null | b!=null | c!=null | d!=null | f!=null)){
					num +=1;
				}
			}
			if(userids.trim().length()>0){
				userid = Integer.parseInt(userids);
				num =5;
			}

			List<Property> list = new ArrayList<Property>();
			
			switch(num){
				case 0:
					list = propertyDao.allFindProperty();
					break;
				case 1:
					list = propertyDao.findProperty(address);
					break;
				case 2:
					list = propertyDao.findProperty(address,a,b,c,d,f);
					break;
				case 3:
					list = propertyDao.findProperty(address,a,b,c,d,f,price);
					break;
				case 4:
					list = propertyDao.findProperty(address, price);
					break;
				case 5:
					list = propertyDao.find(userid);
					break;
			}
			json = gson.toJson(list, propertyType);
		}else if(action.equals("add")){
			address = city+address;
			int count = 0;
			Property propertys = propertyDao.findProperty(userid, propertyname);
			if(propertys!=null){
				json="{'out':exist}";
			}else{
				Property property = new Property();
				property.setUserid(userid);
				property.setStatus(status);
				property.setAddress(address);
				property.setHomesize(homesize);
				property.setPhoto(photo);
				property.setPrice(price);
				property.setPropertyname(propertyname);
				
				count = propertyDao.addProperty(property);
				
				if(count>0){
					if(model.equals("B")){
						
					}else {
						model = "B";
						UserDao userDao = new UserDao();
						int ucount = userDao.modify(userid, model);
						if(ucount>0){
							//成为房东成功
						}else{
							//失败
						}
					}
					
					property = propertyDao.findProperty(userid, propertyname);
					int propertyno = property.getPropertyno();
					
					FacilityDao facilityDao = new FacilityDao();
					Facility facility = new Facility();
					facility.setPropertyno(propertyno);
					facility.setTv(tv);
					facility.setAircondition(air);
					facility.setWasher(washer);
					facility.setNetwork(network);
					facility.setComputer(computer);
					facility.setDryer(dryer);
					
					int num = facilityDao.addFacility(facility);
					if(num>0){
						//成功
						json="{'out':success}";
					}else{
						//设备表失败
						json="{'out':error1}";
					}
				}else{
					//房子表失败
					json="{'out':error}";
				}
			}
		}
	
		out.write(json);
		out.flush();
		out.close();
	}

}
