package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MemberDao {
	
	public int joinMember(User u) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String query = "INSERT INTO USER_TBL VALUES(?, ?, ?, ?)";
		
		Class.forName(query)
		conn = DriverManager.getConnection("jdbc.oracle.")
		
	}

}
