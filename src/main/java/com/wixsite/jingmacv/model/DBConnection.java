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
	    } catch(SQLException se) {
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
	    	ppsm = conn.prepareStatement("SELECT jng_usr_username, jng_usr_password FROM jng_user WHERE jng_usr_username = ?");
	    	ppsm.setString(1, username);
	    	rs = ppsm.executeQuery();
	    	// Loops over database rows.
	    	while(rs.next()) {
	    		dbUsername = rs.getString("jng_usr_username");
	    		dbPassword = rs.getString("jng_usr_password");
	    	}
	    	if (password == null || dbPassword == null)
	    		return match;
	    	match = username.equals(dbUsername) && BCrypt.checkpw(password, dbPassword);
		    if (match) {
		    	ppsm = conn.prepareStatement("INSERT INTO jng_login_audit (login_audit_id, lga_time, lga_dba_user, lga_user_id)" +
		    						   "VALUES ((SELECT COUNT(*)+1 FROM jng_login_audit), TO_CHAR(SYSDATE, 'DD/MM/YYYY, HH:MI:SS AM')," + 
		    						   "(SELECT USER FROM dual), (SELECT user_id FROM jng_user WHERE jng_usr_username = ?))");
		    	ppsm.setString(1, username);
		    	ppsm.executeUpdate();
		    }
	    } catch(SQLException se) {
	    	se.printStackTrace();
	    } catch(Exception e) {
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
	    	ppsm = conn.prepareStatement("INSERT INTO jng_user (user_id, jng_usr_username, jng_usr_password) VALUES ((SELECT COUNT(*)+1 FROM jng_user), ?, ?)");
	    	ppsm.setString(1, username);
	    	ppsm.setString(2, password);
	    	ppsm.executeUpdate();
	    	
	    	ppsm = conn.prepareStatement("INSERT INTO jng_register_audit (user_id, rga_time, rga_dba_user) VALUES " +
	    	"((SELECT user_id FROM jng_user WHERE jng_usr_username = ?), TO_CHAR(SYSDATE, 'DD/MM/YYYY, HH:MI:SS AM'), (SELECT USER FROM dual))");
			ppsm.setString(1, username);
			ppsm.executeUpdate();
	    	status = "registered";
	    } catch(SQLIntegrityConstraintViolationException se) {
	    	status = "uniqueConstraint"; 
	    	se.printStackTrace();
	    } catch(SQLException se) {
	    	status = "incorrectUsernamePassword";
	    	se.printStackTrace();
	    }
		return status;
	}
	
	// Adds a word to the jng_word table.
	public static String addWord(String word) {
		String status = null;
		try {
			// Calls the insert_word procedure from the jng_word_pk package.
			ppsm = conn.prepareStatement("INSERT INTO jng_word (word_id, wrd_word, wrd_is_palin) VALUES ((SELECT COUNT(*)+1 FROM jng_word), ?, ?)");
			ppsm.setString(1, word);
			ppsm.setString(2, PalindromeChecker.isPalindrome(word));
			ppsm.executeUpdate();
			status = "wordAdded";
		} catch(SQLException se) {
			se.printStackTrace();
			status = "wordAlreadyInList";
		}
		return status;
	}
	
	public static ArrayList<Jng_word> setResultSet() {
		ArrayList<Jng_word> words = new ArrayList<Jng_word>();
		try {
	    	// Inserts word_id, wrd_word, wrd_is_palin into the ResultSet.
	    	String sqlGetData = "SELECT word_id, wrd_word, wrd_is_palin FROM JNG_WORD ORDER BY word_id";
	    	rs = stmt.executeQuery(sqlGetData);
	    	while(rs.next()) {
	            Jng_word word = new Jng_word(rs.getInt(1), rs.getString(2), rs.getString(3));
	            words.add(word);
	    	}
	    } catch(SQLException se) {
	    	se.printStackTrace();
	    }
		return words;
	}
	
	// Closes Connection, Statement and ResultSet.
	public void closeConnection() {
		try {
    		if(rs != null)
    			rs.close();
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
		try {
    		if(ppsm != null)
    			ppsm.close();
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    	try {
    		if(stmt != null)
    			stmt.close();
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    	try {
    		if(conn != null)
    			conn.close();
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
	}
}
