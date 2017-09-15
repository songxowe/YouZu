package com.android.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.Facility;
import com.android.util.DBUtil;



public class FacilityDao {
	Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    /**
     * 新增
     * @param facility
     * @return
     */
    public int addFacility(Facility facility){
    	int count=0;
    	try{
    		conn=DBUtil.getConnection();
    		String sql = "insert into facility ";
    		sql+=" values(SEQ_FACILITY.nextval,?,?,?,?,?,?,?)";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, facility.getPropertyno());
    		ps.setString(2, facility.getTv());
    		ps.setString(3, facility.getAircondition());
    		ps.setString(4, facility.getWasher());
    		ps.setString(5, facility.getNetwork());
    		ps.setString(6, facility.getComputer());
    		ps.setString(7, facility.getDryer());
    		
    		count=ps.executeUpdate();
    	}catch(SQLException e){
	    	e.printStackTrace();
	    }finally{
	    	DBUtil.close(conn, ps, rs);
	    }
		return count;
    }
    /**
     * 根据房产号查询房间设备
     * @param propertyno
     * @return
     */
    public Facility findFacility(int propertyno){
    	Facility facility=null;
    	try {
			conn=DBUtil.getConnection();
			String sql="select * from facility ";
			sql += " where propertyno=?";	
			ps=conn.prepareStatement(sql);
			ps.setInt(1, propertyno);
			rs=ps.executeQuery();
			if(rs.next()){
				facility=new Facility();
				facility.setPropertyno(rs.getInt("propertyno"));
				facility.setTv(rs.getString("tv"));
				facility.setAircondition(rs.getString("air_conditioning"));
				facility.setWasher(rs.getString("washer"));
				facility.setNetwork(rs.getString("network"));
				facility.setComputer(rs.getString("computer"));
				facility.setDryer(rs.getString("dryer"));
				facility.setPropertyid(rs.getInt("propertyid"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
    	
		return facility;
    	
    }
    
    /**
     * 查询所有房间的设备
     * @return
     */
    public List<Facility> allFindFacility(){
    	List<Facility> facilities = new ArrayList<Facility>();
    	try {
			conn=DBUtil.getConnection();
			String sql="select * from facility ";	
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				Facility facility=new Facility();
				facility.setPropertyno(rs.getInt("propertyno"));
				facility.setTv(rs.getString("tv"));
				facility.setAircondition(rs.getString("air_conditioning"));
				facility.setWasher(rs.getString("washer"));
				facility.setNetwork(rs.getString("network"));
				facility.setComputer(rs.getString("computer"));
				facility.setDryer(rs.getString("dryer"));
				facility.setPropertyid(rs.getInt("propertyid"));
				facilities.add(facility);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return facilities;
    	
    }
}
