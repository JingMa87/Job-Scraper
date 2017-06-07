package com.wixsite.jingmacv.model;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Register extends DBUtil {

	/*
	 *  Registers a user in the database.
	 */
	public static String registerUsernamePassword(String username, String password) {
		// Returns the status.
		String status = null;
		
		try {
			// Hashes the password.
			password = BCrypt.hashpw(password, BCrypt.gensalt());	    	
	    	// Inserts username and password into the database.
	    	conn = DBUtil.getConn();
	    	pstmt = conn.prepareStatement("INSERT INTO jbs_user (id, username, password) VALUES " +
	    								  "((SELECT NVL(MAX(id) + 1, 1) FROM jbs_user), ?, ?)");
	    	pstmt.setString(1, username);
	    	pstmt.setString(2, password);
	    	pstmt.executeUpdate();
	    	// Inserts a register audit into the database.
	    	pstmt = conn.prepareStatement("INSERT INTO jbs_register_audit (id, time, dba_user) VALUES " +
	    	"((SELECT id FROM jbs_user WHERE username = ?), TO_CHAR(SYSDATE, 'DD/MM/YYYY HH:MI:SSAM'), (SELECT USER FROM dual))");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
	    	status = "registered";
	    } catch (SQLIntegrityConstraintViolationException se) {
	    	status = "uniqueConstraint"; 
	    	se.printStackTrace();
	    } catch (SQLException se) {
	    	status = "incorrectUsernamePassword";
	    	se.printStackTrace();
	    } finally {
	    	// Closes all transaction objects.
		    DBUtil.close(pstmt);
		    DBUtil.close(conn);
	    }
		return status;
	}
}
