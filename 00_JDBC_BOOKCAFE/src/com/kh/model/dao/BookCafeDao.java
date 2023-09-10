package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.model.vo.BookCafe;
import static com.kh.common.JDBCTemplate.*;

public class BookCafeDao {

	private Properties prop = new Properties();

	public BookCafeDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<BookCafe> selectAll(Connection conn) {

		ArrayList<BookCafe> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectAll");

		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				list.add(new BookCafe(rset.getInt("BOOK_NO"), rset.getString("BOOK_NAME"), rset.getString("BOOK_AUTHOR"),
						rset.getString("THEME"), rset.getString("PUBLISHER"), rset.getInt("STOCK")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}

		return list;
	} // selectAll() 종료

	public int insertBook(Connection conn, BookCafe b) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertBook");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBook_no());
			pstmt.setString(2, b.getBook_name());
			pstmt.setString(3, b.getBook_author());
			pstmt.setString(4, b.getTheme());
			pstmt.setString(5, b.getPublisher());
			pstmt.setInt(6, b.getStock());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public ArrayList<BookCafe> selectByBookName(Connection conn, String keyword) {

		ArrayList<BookCafe> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = prop.getProperty("selectByBookName");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new BookCafe(rs.getInt("BOOK_NO"), rs.getString("BOOK_NAME"), rs.getString("BOOK_AUTHOR"),
						rs.getString("THEME"), rs.getString("PUBLISHER"), rs.getInt("STOCK")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return list;
	}

	public int updateBook(Connection conn, BookCafe b) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "UPDATE BOOKCAFE SET THEME = ?, PUBLISHER = ?, STOCK = ? WHERE BOOK_NO = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getTheme());
			pstmt.setString(2, b.getPublisher());
			pstmt.setInt(3, b.getStock());
			pstmt.setInt(4, b.getBook_no());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public int deleteBook(Connection conn, int book_no) {

		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM BOOKCAFE WHERE BOOK_NO = ? ";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_no);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public int check(Connection conn, int book_no, int stock) {

		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(prop.getProperty("checkStock"));
			pstmt.setInt(1, book_no);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				if (rs.getInt("STOCK") == 0) {
					result = 0;
				} else if (stock > rs.getInt("STOCK")) {
					result = 2;
				} else {
					result = 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}

		return result;
	}

	public int lendBook(Connection conn, int book_no, int stock) {

		PreparedStatement pstmt = null;
		int result = 0;

		try {
			pstmt = conn.prepareStatement(prop.getProperty("lendBook"));
			pstmt.setInt(1, stock);
			pstmt.setInt(2, book_no);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public int receiveBook(Connection conn, int book_no, int stock) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(prop.getProperty("receiveBook"));
			pstmt.setInt(1, stock);
			pstmt.setInt(2, book_no);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

}
