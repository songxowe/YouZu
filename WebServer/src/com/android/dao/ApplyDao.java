package com.android.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.Apply;
import com.android.util.DBUtil;


public class ApplyDao {

	Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	
	/**
	 * 添加
	 * @return
	 */
	public int addApply(Apply apply){
		int count = 0;
		try {
		conn=DBUtil.getConnection();
		String sql = "insert into apply values(seq_apply.nextval,?,?,?,?,?,?,?,?,?)";
		ps=conn.prepareStatement(sql);
		ps.setString(1, apply.getUsername());
		ps.setString(2, apply.getRealname());
		ps.setInt(3, apply.getHouseno());
		ps.setInt(4, apply.getHostno());
		ps.setString(5, apply.getTel());
		ps.setString(6, apply.getNumberpeople());
		ps.setString(7, apply.getStartdate());
		ps.setString(8, apply.getEnddate());
		ps.setString(9, apply.getStatus());
		count=ps.executeUpdate();
		}catch (SQLException e) {
		      e.printStackTrace();
		    } finally {
		      DBUtil.close(conn, ps, rs);
		    }

		return count;
	}
	/*
	 *拒绝请求
	 * 
	 */
	public int refused(int applyno){
		int count=0;
		try {
		conn=DBUtil.getConnection();
		String sql = "update apply set status = 'N' ";
		sql += " where applyno=?";
		ps=conn.prepareStatement(sql);
	    ps.setInt(1, applyno);
		count=ps.executeUpdate();
		}catch (SQLException e) {
		      e.printStackTrace();
		    } finally {
		      DBUtil.close(conn, ps, rs);
		    }
		return count;
	}
	
	/**
	 * 更新消息状态
	 * @param hostno
	 * @return
	 */
	public int updateApply(int applyno){
		int count = 0;
		try {
			conn=DBUtil.getConnection();
			String sql = "update apply set status = 'Y' ";
			sql += " where applyno=?";
			ps=conn.prepareStatement(sql);
		    ps.setInt(1, applyno);
			count=ps.executeUpdate();
			}catch (SQLException e) {
			      e.printStackTrace();
			} finally {
			   DBUtil.close(conn, ps, rs);
			}
		return count;
	}
	
	
	/*
	 *退订/到期
	 * 
	 */
	public int remove(int applyno){
		int count=0;
		try {
		conn=DBUtil.getConnection();
		String sql = "delete from apply ";
		sql += " where applyno = ?";
		ps=conn.prepareStatement(sql);
	    ps.setInt(1, applyno);
		count=ps.executeUpdate();
		}catch (SQLException e) {
		    e.printStackTrace();
		} finally {
		    DBUtil.close(conn, ps, rs);
		}
		return count;
	}
	
	
	/*
	 * 根据hostno查询消息
	 */
	public List<Apply> findById(int hostno){
		List<Apply> applies = new ArrayList<Apply>(); 
		try {
			conn=DBUtil.getConnection();
			String sql = "select * from apply ";
			sql += " where hostno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, hostno);
			rs=ps.executeQuery();
			while(rs.next()){
				Apply apply=new Apply();
				apply.setApplyno(rs.getInt(1));
				apply.setUsername(rs.getString(2));
				apply.setRealname(rs.getString(3));
				apply.setHouseno(rs.getInt(4));
				apply.setHostno(rs.getInt(5));
				apply.setTel(rs.getString(6));
				apply.setNumberpeople(rs.getString(7));
				apply.setStartdate(rs.getString(8));
				apply.setEnddate(rs.getString(9));
				apply.setStatus(rs.getString(10));
				
				applies.add(apply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return applies;
		
	}
	
	public Apply pollingFind(String userName){
		Apply apply = new Apply();
		try {
			conn=DBUtil.getConnection();
			String sql = "select * from apply ";
			sql += " where username = ?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, userName);
			rs=ps.executeQuery();
			if(rs.next()){
				apply.setApplyno(rs.getInt(1));
				apply.setUsername(rs.getString(2));
				apply.setRealname(rs.getString(3));
				apply.setHouseno(rs.getInt(4));
				apply.setHostno(rs.getInt(5));
				apply.setTel(rs.getString(6));
				apply.setNumberpeople(rs.getString(7));
				apply.setStartdate(rs.getString(8));
				apply.setEnddate(rs.getString(9));
				apply.setStatus(rs.getString(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return apply;
	}
	
}
