package com.android.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.android.dao.PropertyDao;
import com.android.pojo.Property;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * �������ݿ�Ĺ�����
 * 
 * @author Nick
 * 
 */
public class DBUtil {
  private static ComboPooledDataSource cpd;

  // ����ģʽ���� c3p0 ���ӳ�
  static {
    String drivername = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    String username = "scott";
    String password = "scott";

    try {
      // ���� c3p0 ���ӳ� ���ݿ�����
      cpd = new ComboPooledDataSource();
      cpd.setDriverClass(drivername);
      cpd.setJdbcUrl(url);
      cpd.setUser(username);
      cpd.setPassword(password);

      // ���� c3p0 ���ӳ� ����
      cpd.setMaxPoolSize(20);
      cpd.setInitialPoolSize(5);
      cpd.setMaxStatements(50);
      cpd.setAcquireIncrement(3);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
  }

  /**
   * ��c3p0���ӳػ�����ݿ�����
   * 
   * @return
   */
  public static Connection getConnection() {
    Connection conn = null;

    try {
      conn = cpd.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  /**
   * �ر����ݿ�����
   * 
   * @param conn
   * @param ps
   * @param rs
   */
  public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  
//  public static void main(String[] args) {
//    System.out.println(DBUtil.getConnection());
//  }
  
}