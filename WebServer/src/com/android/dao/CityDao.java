package com.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.City;
import com.android.util.DBUtil;

public class CityDao {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	/**
	   * 查询所有城市
	   * 
	   * @return
	   */
	  public List<City> find() {
	    List<City> citys = new ArrayList<City>();
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "select * from city ";
	      ps = conn.prepareStatement(sql);
	      rs = ps.executeQuery();

	      while (rs.next()) {
	    	City city = new City();
	    	city.setCityid(rs.getInt("cityid"));
	    	city.setCityname(rs.getString("cityname"));
	    	city.setCitypath(rs.getString("citypath"));
	    	citys.add(city);
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return citys;
	  }
	  
	  /**
	   * 根据城市名查询城市
	   * 
	   * @param city
	   * @return
	   */
	  public City find(String cityname) {
		  City city = null;
	    try {
	      conn = DBUtil.getConnection();
	      String sql = "select * from city ";
	      sql += " where cityname=?";
	      ps = conn.prepareStatement(sql);
	      ps.setString(1, cityname);

	      rs = ps.executeQuery();

	      if (rs.next()) {
	    	  city = new City();
	    	  city.setCityid(rs.getInt("cityid"));
		    	city.setCityname(rs.getString("cityname"));
		    	city.setCitypath(rs.getString("citypath"));
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }
	    return city;
	  }
	  
	  /**
	   * 新增城市
	   * 
	   * @param city
	   * @return
	   */
	  public int add(City city) {
	    int count = 0;

	    try {
	      conn = DBUtil.getConnection();
	      String sql = "insert into city(cityid,cityname,citypath) ";
	      sql += " values(seq_city.nextval,?,?,?)";
	      ps = conn.prepareStatement(sql);
	      ps.setInt(1, city.getCityid());
	      ps.setString(2, city.getCityname());
	      ps.setString(3, city.getCitypath());

	      count = ps.executeUpdate();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      DBUtil.close(conn, ps, rs);
	    }

	    return count;
	  }
}
