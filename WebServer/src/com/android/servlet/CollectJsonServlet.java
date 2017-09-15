package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.dao.CollectDao;
import com.android.pojo.Apply;
import com.android.pojo.Collect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CollectJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson;
	private Type collectType;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		gson = new Gson();
		collectType = new TypeToken<List<Collect>>(){
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
		action = action !=null?action:"add";
		
		String json = "";
		
		String housephoto = request.getParameter("housephoto");
        String shouseno = request.getParameter("houseno");
        shouseno = shouseno!=null?shouseno:"0";
        int houseno = Integer.parseInt(shouseno);
        
        String userimg = request.getParameter("userimg");
        String sprice = request.getParameter("price");
        sprice = sprice!=null?sprice:"0";
        double price = Double.parseDouble(sprice);
        
        String housename = request.getParameter("housename");
        String score = request.getParameter("score");
        String suserid = request.getParameter("userid");
        suserid = suserid!=null?suserid:"0";
        int userid = Integer.parseInt(suserid);
        String username = request.getParameter("username");
        
        System.out.println(username);
        
        CollectDao dao = new CollectDao();
        
        Collect collect = new Collect();
        collect.setHousename(housename);
        collect.setHouseno(houseno);
        collect.setHousephoto(housephoto);
        collect.setPrice(price);
        collect.setScore(score);
        collect.setUserid(userid);
        collect.setUserimg(userimg);
        collect.setUsername(username);
        
        if(action.equals("add")){
        	int count = dao.addCollect(collect);
        	 if(count>0){
				 //³É¹¦·¢ËÍÉêÇë
				 out.println("{'flag':'success'}");
			 }else{
				//·¢ËÍÉêÇëÊ§°Ü
				 out.println("{'flag':'failed'}");
			 }
        	
        }else if(action.equals("select")){
        	
        	List<Collect> list = dao.findById(userid);
        	json = gson.toJson(list,collectType);
			out.write(json);
			
        }else if(action.equals("delete")){
        	String no = request.getParameter("shouseno");
        	no = no!=null?no:"0";
        	int house = Integer.parseInt(no);
        	int count = dao.removeCollect(house);
        	
        	if(count>0){
				 //³É¹¦·¢ËÍÉêÇë
				 out.println("{'flag':'success'}");
			 }else{
				//·¢ËÍÉêÇëÊ§°Ü
				 out.println("{'flag':'failed'}");
			 }
        }
       
		out.flush();
		out.close();
	}

}
