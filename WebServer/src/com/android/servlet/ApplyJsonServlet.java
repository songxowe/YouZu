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


public class ApplyJsonServlet extends HttpServlet {
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
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String json = "";
		String action = request.getParameter("action");
		action = (action!=null)? action : "add"; 
		
		
		
		
      
		//将求租资料发送给房主
		 String realname = request.getParameter("realName");
		 String tel = request.getParameter("tel");
		 String numberpeople = request.getParameter("number");
		 String  startdate = request.getParameter("startDate");
		 String  enddate = request.getParameter("endDate");
		 String username = request.getParameter("username");
		 String houseNo = request.getParameter("propertyNo");
		 String hostNo = request.getParameter("userId");
		 houseNo = (houseNo!=null)? houseNo : "";
		 hostNo = (hostNo!=null)? hostNo : "";
		 
		 int houseno = 0;
		 int hostno = 0;
		 if(houseNo.trim().length()>0){
			 houseno=Integer.parseInt(houseNo);
		 }
		 if(hostNo.trim().length()>0){
			 hostno=Integer.parseInt(hostNo);
		 }
		 
		 System.out.println(hostno+"...............");
		 
		 
		 ApplyDao applyDao=new ApplyDao();
		 
		 if(action.equals("add")){
			 Apply apply=new Apply();
			 apply.setRealname(realname);
			 apply.setUsername(username);
			 apply.setHouseno(houseno);
			 apply.setHostno(hostno);
			 apply.setTel(tel);
			 apply.setNumberpeople(numberpeople);		 
			 apply.setStartdate(startdate);
			 apply.setEnddate(enddate);
			 apply.setStatus("U");
			 
			 int count=applyDao.addApply(apply);
			 if(count>0){
				 //成功发送申请
				 out.println("{'flag':'success'}");
			 }else{
				//发送申请失败
				 out.println("{'flag':'failed'}");
			 }
		 }else if(action.equals("find")){
		
			 List<Apply> list = applyDao.findById(hostno);
			 json = gson.toJson(list,applyType);
			 out.write(json);
		 }else if(action.equals("refused")){
			 String sApplyno = request.getParameter("applyno");
			 sApplyno = sApplyno!=null?sApplyno:"0";
			 int applyno = Integer.parseInt(sApplyno);
			 
			 int count = applyDao.refused(applyno);
			 if(count>0){
				 //成功发送申请
				 out.println("{'flag':'success'}");
			 }else{
				//发送申请失败
				 out.println("{'flag':'failed'}");
			 }
		 }else if(action.equals("update")){
			 String sApplyno = request.getParameter("applyno");
			 sApplyno = sApplyno!=null?sApplyno:"0";
			 int applyno = Integer.parseInt(sApplyno);
			 
			 int count = applyDao.updateApply(applyno);
			 if(count>0){
				 //成功发送申请
				 out.println("{'flag':'success'}");
			 }else{
				//发送申请失败
				 out.println("{'flag':'failed'}");
			 }
		 }else if(action.equals("remove")){
			 String sApplyno = request.getParameter("applyno");
			 sApplyno = sApplyno!=null?sApplyno:"0";
			 int applyno = Integer.parseInt(sApplyno);
			 //System.out.println("我进来了");
			 int count = applyDao.remove(applyno);
			 if(count>0){
				 //成功删除
				 out.println("{'flag':'success'}");
			 }else{
				//删除失败
				 out.println("{'flag':'failed'}");
			 }
		 }else if (action.equals("check")){
			 String uname = request.getParameter("uname");
			 Apply applyCheck = applyDao.pollingFind(uname);
			 System.out.println("我进来了"+applyCheck.getApplyno());
			 if(applyCheck.getApplyno()>0){
				 //存在数据
				 out.println("{'flag':'failed'}");
			 }else{
				//不存在数据
				 out.println("{'flag':'success'}");
			 }
		 }
		out.flush();
		out.close();

	}
}
