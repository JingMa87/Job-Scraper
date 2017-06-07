package com.wixsite.jingmacv.model;

import java.sql.SQLException;

public class Login extends DBUtil {

	/*
	 *  Checks if the user is registered in the database.
	 */
	public static boolean checkUsernamePassword(String username, String password) {
		// Is the user registered?
    	boolean registered = false;
    	
	    try {
	    	String hashedPassword = null;
	    	// Gets the password from the database.
	    	conn = DBUtil.getConn();
	    	pstmt = conn.prepareStatement("SELECT password FROM jbs_user WHERE username = ?");
	    	pstmt.setString(1, username);
	    	rs = pstmt.executeQuery();
	    	while (rs.next()) {
	    		hashedPassword = rs.getString("password");
	    	}
	    	// Return false if the user doesn't exist.
	    	if (password == null || hashedPassword == null)
	    		registered = false;
	    	// If the user is registered, inserts a login audit to the database.
	    	else if (BCrypt.checkpw(password, hashedPassword)) {
		    	pstmt = conn.prepareStatement("INSERT INTO jbs_login_audit (id, time, dba_user, user_id) VALUES " +
		    						   		  "((SELECT NVL(MAX(id) + 1, 1) FROM jbs_login_audit), " +
		    						   		  "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH:MI:SSAM'), " + 
		    						   		  "(SELECT USER FROM dual), " +
		    						   		  "(SELECT id FROM jbs_user WHERE username = ?))");
		    	pstmt.setString(1, username);
		    	pstmt.executeUpdate();
		    	registered = true;
		    }		    
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    } finally {
	    	// Closes all transaction objects.
		    DBUtil.close(rs);
		    DBUtil.close(pstmt);
		    DBUtil.close(conn);
	    }
	    return registered;
	}
}
