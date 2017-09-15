package com.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.pojo.Property;
import com.android.util.DBUtil;


public class PropertyDao {
	Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    /**
     * 申请为房主（增加）
     * @param property
     * @return
     */
     public int addProperty(Property property){
    	int count =0;
    	try {
			conn=DBUtil.getConnection();
			String sql = "insert into property ";
			sql+=" values(SEQ_PROPERTY.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			ps=conn.prepareStatement(sql);
			ps.setInt(1, property.getUserid());
			ps.setString(2, property.getStatus());
			ps.setString(3, property.getAddress());
			ps.setString(4, property.getHomesize());
			ps.setString(5, property.getPhoto());
			ps.setDouble(6, property.getPrice());
			ps.setString(7, "0");
			ps.setString(8, "0");
			ps.setString(9, "0");
			ps.setString(10, "0");
			ps.setString(11, "0");
			ps.setString(12, property.getPropertyname());
			ps.setFloat(13, 3.0f);
			
			count=ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
    	
		return count;
    }
     
     /**
      *更改房产状态
      */
     public int updateStatus(String status,int propertyno){
    	 int count = 0;
    	 try {
 			conn=DBUtil.getConnection();
 			String sql="update property set status=? ";
 			sql += " where propertyno=?";
 			ps=conn.prepareStatement(sql);
 			ps.setString(1, status);
 			ps.setInt(2, propertyno);
 			count=ps.executeUpdate();
    	} catch (SQLException e) {
 			e.printStackTrace();
 		}finally{
 			DBUtil.close(conn, ps, rs);
 		}
    	 return count;
     }
     
     
     
     
     
    /**
     * 修改房产资料
     * @param property
     * @return
     */
    public int modify(Property property){
    	int count = 0;
    	try {
			conn=DBUtil.getConnection();
			String sql="update property set status=?, address=?,homesize=?,photo=?,price=?,lease_trem=?,start_date=?,expire_date=?,total_rent=?,deposit=?,propertyname=?,score=? ";
			sql += " where propertyno=?";
			ps.setString(1, property.getStatus());
			ps.setString(2, property.getAddress());
			ps.setString(3, property.getHomesize());
			ps.setString(4, property.getPhoto());
			ps.setDouble(5, property.getPrice());
			ps.setString(6, property.getLeaseTrem());
			ps.setString(7, property.getStartDate());
			ps.setString(8, property.getExpireDate());
			ps.setString(9, property.getTotalRent());
			ps.setString(10, property.getDeposit());
			ps.setString(11, property.getPropertyname());
			ps.setDouble(12, property.getScore());
			ps.setInt(13, property.getPropertyno());
			
			count=ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
    }
    
    public Property findProperty(int userid,String propertyname){
    	Property property = null;
    	try {
			conn=DBUtil.getConnection();
			String sql = "select * from property ";
			sql += " where userid=? and propertyname=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,userid);
			ps.setString(2, propertyname);
			rs=ps.executeQuery();
			if(rs.next()){
				property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return property;
    }
    
    /**
     * 根据房产号查询房子资料
     * @param propertyno
     * @return
     */
    public Property findProperty(int propertyno){
    	Property property = null;
    	try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where propertyno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,propertyno);
			rs=ps.executeQuery();
			if(rs.next()){
				property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return property;
    }
    /**
     * 查询所有房产的信息
     * @return
     */
    public List<Property> allFindProperty(){
    	List<Property> propertys = new ArrayList<Property>();
    	try {
			conn=DBUtil.getConnection();
			String sql = "select * from property";
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}	
		return propertys;	
    }
    
    
    public List<Property> findProperty(String address){
    	List<Property> propertys = new ArrayList<Property>();
    	try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where address like ?";
			ps=conn.prepareStatement(sql);
			ps.setString(1,"%"+address+"%");
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return propertys;
    }
    
    public List<Property> findProperty(String address,String a,String b,String c,String d,String f){
        List<Property> propertys = new ArrayList<Property>();
        try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where address like ? and homesize in (?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1,"%"+address+"%");
			ps.setString(2, a);
			ps.setString(3, b);
			ps.setString(4, c);
			ps.setString(5, d);
			ps.setString(6, f);
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return propertys;
    }
    
    public List<Property> findProperty(String address,Double price){
        List<Property> propertys = new ArrayList<Property>();
        try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where address like ? and (price<? and price>?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1,"%"+address+"%");
			Double min = 0.0;
			Double max = 0.0;
			if(price>500){
				min=500.0;
				max=1000.0;
				if(price>1000){
					min=1000.0;
					max=1500.0;
					if(price>1500){
						min=1500.0;
						max=5000.0;
					}
				}
			}else{
				max=price;
			}
			ps.setDouble(2,max);
			ps.setDouble(3,min);
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return propertys;
    }
    
    public List<Property> findProperty(String address,String a,String b,String c,String d,String f,Double price){
        List<Property> propertys = new ArrayList<Property>();
        try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where address like ? and homesize in (?,?,?,?,?) and (price<? and price>?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1,"%"+address+"%");
			ps.setString(2,a);
			ps.setString(3,b);
			ps.setString(4,c);
			ps.setString(5,d);
			ps.setString(6,f);
			Double min = 0.0;
			Double max = 0.0;
			if(price>500){
				min=500.0;
				max=1000.0;
				if(price>1000){
					min=1000.0;
					max=1500.0;
					if(price>1500){
						min=1500.0;
						max=5000.0;
					}
				}
			}else{
				max=price;
			}
			ps.setDouble(7,max);
			ps.setDouble(8,min);
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return propertys;
    }
    
    
    
    public List<Property> find(int userid){
    	List<Property> propertys = new ArrayList<Property>();
    	try {
			conn=DBUtil.getConnection();
			String sql = "select * from (select u.userimgpath,u.username,u.tel,p.* from fhuser u inner join property p on u.userid=p.userid) ";
			sql += " where userid=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,userid);
			rs=ps.executeQuery();
			while(rs.next()){
				Property property=new Property();
				property.setPropertyno(rs.getInt("propertyno"));
				property.setUserid(rs.getInt("userid"));
				property.setStatus(rs.getString("status"));
				property.setAddress(rs.getString("address"));
				property.setHomesize(rs.getString("homesize"));
				property.setPhoto(rs.getString("photo"));
				property.setPrice(rs.getDouble("price"));
				property.setLeaseTrem(rs.getString("lease_trem"));
				property.setStartDate(rs.getString("start_lease_date"));
				property.setExpireDate(rs.getString("expire_date"));
				property.setTotalRent(rs.getString("total_rent"));
				property.setDeposit(rs.getString("deposit"));
				property.setPropertyname(rs.getString("propertyname"));
				property.setScore(rs.getDouble("score"));
				property.setUserimgpath(rs.getString("userimgpath"));
				property.setUsername(rs.getString("username"));
				property.setTel(rs.getString("tel"));
				propertys.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return propertys;
    }
}