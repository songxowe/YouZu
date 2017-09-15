package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.pojo.User;
import com.android.util.MD5;
import com.android.dao.UserDao;
import com.google.gson.Gson;

public class UserJsonServlet extends HttpServlet {
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
		
		String action = request.getParameter("action");
		action = (action!=null) ? action : "add";
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String realname = request.getParameter("realname");
		String sex = request.getParameter("sex");
		String tel = request.getParameter("tel");
		String question = request.getParameter("question");
		String answer = request.getParameter("answer");
		String userimgpath = request.getParameter("userimgpath");
		//默认用户为普通用户,头像为默认图片
		String model = "A";
		
		System.out.println(action+"****************");
		System.out.println(username+"****************");
		System.out.println(userimgpath+"****************");
		System.out.println(password+"****************");
		
		if(action.equals("add")){
			userimgpath = (userimgpath!=null) ? userimgpath : "images/user_png_48px.png";
			if(username!=null & password!=null & realname!=null & sex!=null 
					& tel!=null & question!=null & answer!=null){
				User user = new User();
				user.setUsername(username);
				//给密码加密
				user.setPassword(MD5.getMD5(password));
				user.setModel(model);
				user.setRealname(realname);
				user.setSex(sex);
				user.setTel(tel);
				user.setQuestion(question);
				user.setAnswer(answer);
				user.setUserimgpath(userimgpath);
				
				UserDao userDao = new UserDao();
				User checkUser = userDao.find(user.getUsername());
				if(checkUser == null){
					int count = userDao.add(user);
				      if (count > 0) {
				    	//注册成功
				       out.println("{'flag':'success'}");
				      } else {
				    	//注册失败
				        out.println("{'flag':'error'}");
				      }
				    } else {
				    	//用户名已存在
				      out.println("{'flag':'exist'}");
				    }
			}else{
				
			}
		}if(action.equals("modify")){
			String json = "";
			UserDao userDao = new UserDao();
			if(username!=null&userimgpath!=null){
				int count = userDao.modify(username, userimgpath);
				if(count>0){
					//out.println("{'flag':'修改成功'}");
					json = "ok";
			      } else {
			    	//修改失败
			        //out.println("{'flag':'修改失败'}");
			    	  json = "error";
			      }
			}else if(username!=null&password==null){
				User user = userDao.find(username);
				gson = new Gson();
				json = gson.toJson(user,User.class);
			}else if(username!=null&password!=null){
				int count = userDao.modifyPass(username, MD5.getMD5(password));
				if(count>0){
					json="{'flag':'ok'}";
				}else{
					json="{'flag':'error'}";
				}
			}else{
				json="{'flag':'error'}";
			}
			out.write(json);
		}
		
		out.flush();
		out.close();
	}

}
