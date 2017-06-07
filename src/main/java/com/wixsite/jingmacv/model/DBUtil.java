package com.wixsite.jingmacv.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
 * This class provides database variables and methods for other classes.
 */
public abstract class DBUtil {
	
	// Transaction objects.
	private static DataSource ds = null;
	public static Connection conn = null;
	public static Statement stmt = null;
	public static PreparedStatement pstmt = null;
	public static ResultSet rs = null;

	/*
	 * Initializes the DataSource object.
	 */
	public static void initDataSource() {
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/ds");
	    } catch (NamingException e) {
			e.printStackTrace();
	    }
	}
	
	/*
	 * Returns a connection from the DataSource's connection pool.
	 */
	public static Connection getConn() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
	 *  Closes Connection.
	 */
	public static void close(Connection conn) {
		if (conn != null)
			try {
	    		conn.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes Statement.
	 */
	public static void close(Statement stmt) {
		if (stmt != null)
			try {
	    		stmt.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes PreparedStatement.
	 */
	public static void close(PreparedStatement pstmt) {
    	if (pstmt != null)
    		try {
    			pstmt.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes ResultSet.
	 */
	public static void close(ResultSet rs) {
		if (rs != null)
			try {
	    		rs.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
}
