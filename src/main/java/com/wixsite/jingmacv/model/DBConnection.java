package com.wixsite.jingmacv.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection {

	// JDBC database URL.
	private final String DB_URL = "jdbc:oracle:thin:jing/jing@localhost:1521:xe";
	// Connection objects.
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement ppsm;
	private static ResultSet rs;
	
	public DBConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
	    	conn = DriverManager.getConnection(DB_URL);
	    	stmt = conn.createStatement();
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Checks if the entered username and password exist in the database.
	public boolean checkUsernamePassword(String username, String password) {
    	String dbUsername = null;
    	String dbPassword = null;
    	boolean match = false;
    	
	    try {
	    	// Query.
	    	ppsm = conn.prepareStatement("SELECT username, password FROM jbs_user WHERE username = ?");
	    	ppsm.setString(1, username);
	    	rs = ppsm.executeQuery();
	    	// Loops over database rows.
	    	while (rs.next()) {
	    		dbUsername = rs.getString("username");
	    		dbPassword = rs.getString("password");
	    	}
	    	if (password == null || dbPassword == null)
	    		return match;
	    	match = username.equals(dbUsername) && BCrypt.checkpw(password, dbPassword);
		    if (match) {
		    	ppsm = conn.prepareStatement("INSERT INTO jbs_login_audit (id, time, dba_user, user_id)" +
		    						   "VALUES ((SELECT NVL(MAX(id) + 1, 1) FROM jbs_login_audit), TO_CHAR(SYSDATE, 'DD/MM/YYYY HH:MI:SSAM')," + 
		    						   "(SELECT USER FROM dual), (SELECT id FROM jbs_user WHERE username = ?))");
		    	ppsm.setString(1, username);
		    	ppsm.executeUpdate();
		    }
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return match;
	}
	
	// Adds a username and password to the database.
	public static String registerUsernamePassword(String username, String password) {
		String status = null;
		try {
			password = BCrypt.hashpw(password, BCrypt.gensalt());
	    	
	    	// Insert username and password into database.
	    	ppsm = conn.prepareStatement("INSERT INTO jbs_user (id, username, password) VALUES ((SELECT NVL(MAX(id) + 1, 1) FROM jbs_user), ?, ?)");
	    	ppsm.setString(1, username);
	    	ppsm.setString(2, password);
	    	ppsm.executeUpdate();
	    	
	    	ppsm = conn.prepareStatement("INSERT INTO jbs_register_audit (id, time, dba_user) VALUES " +
	    	"((SELECT id FROM jbs_user WHERE username = ?), TO_CHAR(SYSDATE, 'DD/MM/YYYY HH:MI:SSAM'), (SELECT USER FROM dual))");
			ppsm.setString(1, username);
			ppsm.executeUpdate();
	    	status = "registered";
	    } catch (SQLIntegrityConstraintViolationException se) {
	    	status = "uniqueConstraint"; 
	    	se.printStackTrace();
	    } catch (SQLException se) {
	    	status = "incorrectUsernamePassword";
	    	se.printStackTrace();
	    }
		return status;
	}
	
	// Adds a vacancy object to the database.
	public static String saveVacancy(String jobTitle, String company, String location) {
		String status = null;
		PreparedStatement ppsm = null;
		try {
			// Insert vacancy into database.
	    	ppsm = conn.prepareStatement("INSERT INTO jbs_vacancy (id, job_title, company, location) " +
	    								 "VALUES ((SELECT NVL(MAX(id) + 1, 1) FROM jbs_vacancy), ?, ?, ?)");
	    	ppsm.setString(1, jobTitle);
	    	ppsm.setString(2, company);
	    	ppsm.setString(3, location);
	    	ppsm.executeUpdate();
	    	status = "saved";
	    } catch (SQLIntegrityConstraintViolationException se) {
	    	se.printStackTrace();
	    } catch (SQLException se) {
	    	status = "noData";
	    	se.printStackTrace();
	    } finally {
	    	try {
				ppsm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			};
	    }
		return status;
	}
	
	public static ArrayList<Vacancy> getResultSet() {
		ArrayList<Vacancy> vacancies = new ArrayList<>();
		try {
	    	// Retrieves vacancy objects into the ResultSet.
	    	String sqlGetData = "SELECT id, job_title, company, location FROM jbs_vacancy ORDER BY id";
	    	rs = stmt.executeQuery(sqlGetData);
	    	while (rs.next()) {
	            Vacancy vacancy = new Vacancy(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
	            vacancies.add(vacancy);
	    	}
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    }
		return vacancies;
	}
	
	public static void resetTable() {
		String sqlResetTable = "DELETE FROM jbs_vacancy";
		try {
			stmt.executeQuery(sqlResetTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Closes Connection, Statement and ResultSet.
	public void closeConnection() {
		try {
    		if (rs != null)
    			rs.close();
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
		try {
    		if (ppsm != null)
    			ppsm.close();
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
    	try {
    		if (stmt != null)
    			stmt.close();
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
    	try {
    		if (conn != null)
    			conn.close();
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
	}
}
