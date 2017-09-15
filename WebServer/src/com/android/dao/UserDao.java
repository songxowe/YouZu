package com.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.User;
import com.android.util.DBUtil;

public class UserDao {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	/**
	   * 查询所有用户
	   * 
	   * @return
	   */
	  public List<User> find() {
	    List<User> users = new ArrayList<User>();
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "select * from fhuser ";
	      ps = conn.prepareStatement(sql);
	      rs = ps.executeQuery();

	      while (rs.next()) {
	    	User user = new User();
	    	user.setUserid(rs.getInt("userid"));
	    	user.setUsername(rs.getString("username"));
	    	user.setPassword(rs.getString("password"));
	    	user.setModel(rs.getString("model"));
	    	user.setRealname(rs.getString("realname"));
	    	user.setSex(rs.getString("sex"));
	    	user.setTel(rs.getString("tel"));
	    	user.setCollect(rs.getString("collect"));
	    	user.setRecord(rs.getString("record"));
	    	user.setQuestion(rs.getString("question"));
	    	user.setAnswer(rs.getString("answer"));
	    	user.setUserimgpath(rs.getString("userimgpath"));
	    	users.add(user);
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return users;
	  }
	  
	  /**
	   * 根据用户名查询用户
	   * 
	   * @param fhuser
	   * @return
	   */
	  public User find(String username) {
		  User user = null;
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "select * from fhuser ";
	      sql += " where username=?";
	      ps = conn.prepareStatement(sql);
	      ps.setString(1, username);

	      rs = ps.executeQuery();

	      if (rs.next()) {
	    	  user = new User();
	    	  user.setUserid(rs.getInt("userid"));
		      user.setUsername(rs.getString("username"));
		      user.setPassword(rs.getString("password"));
		      user.setModel(rs.getString("model"));
		      user.setRealname(rs.getString("realname"));
		      user.setSex(rs.getString("sex"));
		      user.setTel(rs.getString("tel"));
		      user.setCollect(rs.getString("collect"));
		      user.setRecord(rs.getString("record"));
		      user.setQuestion(rs.getString("question"));
		      user.setAnswer(rs.getString("answer"));
		      user.setUserimgpath(rs.getString("userimgpath"));
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return user;
	  }
	  
	  
	  /**
	   * 根据Id查用户
	   */
	  public User find(int userId) {
		  User user = null;
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "select * from fhuser ";
	      sql += " where userid=?";
	      ps = conn.prepareStatement(sql);
	      ps.setInt(1, userId);

	      rs = ps.executeQuery();

	      if (rs.next()) {
	    	  user = new User();
	    	  user.setUserid(rs.getInt("userid"));
		      user.setUsername(rs.getString("username"));
		      user.setPassword(rs.getString("password"));
		      user.setModel(rs.getString("model"));
		      user.setRealname(rs.getString("realname"));
		      user.setSex(rs.getString("sex"));
		      user.setTel(rs.getString("tel"));
		      user.setCollect(rs.getString("collect"));
		      user.setRecord(rs.getString("record"));
		      user.setQuestion(rs.getString("question"));
		      user.setAnswer(rs.getString("answer"));
		      user.setUserimgpath(rs.getString("userimgpath"));
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return user;
	  }
	  
	  /**
	   * 登录
	   * 
	   * @param username
	   * @param password
	   * @return
	   */
	  public User find(String username, String password) {
	    User user = null;
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "SELECT * FROM fhuser";
	      sql += " WHERE USERNAME = ? ";
	      sql += " AND PASSWORD = ? ";

	      ps = conn.prepareStatement(sql);
	      ps.setString(1, username);
	      ps.setString(2, password);

	      rs = ps.executeQuery();
	      if (rs.next()) {
	        user = new User();
	        user.setUserid(rs.getInt("userid"));
		    user.setUsername(rs.getString("username"));
		    user.setModel(rs.getString("model"));
		    user.setRealname(rs.getString("realname"));
		    user.setUserimgpath(rs.getString("userimgpath"));
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return user;
	  }
	  
	  /**
	   * 新增用户
	   * 
	   * @param fhuser
	   * @return
	   */
	  public int add(User user) {
	    int count = 0;

	    try {
	      conn = DBUtil.getConnection();
	      String sql = "insert into fhuser(userid,username,password,model,realname,sex,tel,question,answer,userimgpath) ";
	      sql += " values(seq_fhuser.nextval,?,?,?,?,?,?,?,?,?)";
	      ps = conn.prepareStatement(sql);
	      ps.setString(1, user.getUsername());
	      ps.setString(2, user.getPassword());
	      ps.setString(3, user.getModel());
	      ps.setString(4, user.getRealname());
	      ps.setString(5, user.getSex());
	      ps.setString(6, user.getTel());
	      ps.setString(7, user.getQuestion());
	      ps.setString(8, user.getAnswer());
	      ps.setString(9, user.getUserimgpath());

	      count = ps.executeUpdate();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }

	    return count;
	  }
	  
	  public int modify(String username,String userimgpath){
		  int count = 0;
		  try{
			  conn = DBUtil.getConnection();
			  String sql = "update fhuser set userimgpath=? ";
			  sql += " where username=?";
			  ps = conn.prepareStatement(sql);
			  ps.setString(1, userimgpath);
			  ps.setString(2, username);
			  count = ps.executeUpdate();
		  }catch(SQLException e){
			  e.printStackTrace();
		  }finally{
			  DBUtil.close(conn, ps, rs);
		  }
		  return count;
	  }
	  
	  public int modify(int userid,String model){
		  int count = 0;
		  try{
			  conn = DBUtil.getConnection();
			  String sql = "update fhuser set model=? ";
			  sql += " where userid=?";
			  ps = conn.prepareStatement(sql);
			  ps.setString(1, model);
			  ps.setInt(2, userid);
			  count = ps.executeUpdate();
		  }catch(SQLException e){
			  e.printStackTrace();
		  }finally{
			  DBUtil.close(conn, ps, rs);
		  }
		  return count;
	  }
	  
	  public int modifyPass(String username,String password){
		  int count = 0;
		  try{
			  conn = DBUtil.getConnection();
			  String sql = "update fhuser set password=? ";
			  sql += " where username=?";
			  ps = conn.prepareStatement(sql);
			  ps.setString(1, password);
			  ps.setString(2, username);
			  count = ps.executeUpdate();
		  }catch(SQLException e){
			  e.printStackTrace();
		  }finally{
			  DBUtil.close(conn, ps, rs);
		  }
		  return count;
	  }
}
