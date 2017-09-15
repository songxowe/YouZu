package com.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.Collect;
import com.android.util.DBUtil;

public class CollectDao {

	Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	
	public int addCollect(Collect collect){
		int count = 0;
		try {
		conn=DBUtil.getConnection();
		String sql = "insert into collect values(seq_collect.nextval,?,?,?,?,?,?,?,?)";
		ps=conn.prepareStatement(sql);
		ps.setString(1, collect.getUsername());
		ps.setInt(2, collect.getUserid());
		ps.setString(3, collect.getHousename());
		ps.setInt(4, collect.getHouseno());
		ps.setString(5, collect.getUserimg());
		ps.setString(6, collect.getHousephoto());
		ps.setString(7, collect.getScore());
		ps.setDouble(8, collect.getPrice());
		
		count=ps.executeUpdate();
		}catch (SQLException e) {
		      e.printStackTrace();
		    } finally {
		      DBUtil.close(conn, ps, rs);
		    }

		return count;
	}
	
	/**
	 * ¸ù¾Ýid²éÑ¯
	 */
	
	public List<Collect> findById (int userId){
		List<Collect> list = new ArrayList<Collect>();
		try {
			conn=DBUtil.getConnection();
			String sql = "select * from collect where userid=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs=ps.executeQuery();
			while(rs.next()){
				Collect collect =new Collect();
				
				collect.setUsername(rs.getString("username"));
				collect.setUserid(rs.getInt("userid"));
				collect.setHousename(rs.getString("housename"));
				collect.setHouseno(rs.getInt("houseno"));
				collect.setUserimg(rs.getString("userimg"));
				collect.setHousephoto(rs.getString("housephoto"));
				collect.setScore(rs.getString("score"));
				collect.setPrice(Double.parseDouble(rs.getString("price")));
				
				list.add(collect);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public int removeCollect(int propertyno){
		int count=0;
		try {
		conn=DBUtil.getConnection();
		String sql = "delete from collect where houseno=?";
		ps=conn.prepareStatement(sql);
	    ps.setInt(1, propertyno);
		count=ps.executeUpdate();
		}catch (SQLException e) {
		      e.printStackTrace();
		    } finally {
		      DBUtil.close(conn, ps, rs);
		    }
		return count;
		
	}
	
	
}
