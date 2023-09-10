package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.model.dao.BookCafeDao;
import com.kh.model.vo.BookCafe;
import static com.kh.common.JDBCTemplate.*;

public class BookCafeService {

	public ArrayList<BookCafe> selectAll() {

		Connection conn = getconnection();

		ArrayList<BookCafe> list = new BookCafeDao().selectAll(conn);

		close(conn);

		return list;
	}

	public int insertProduct(BookCafe bc) {

		Connection conn = getconnection();

		int result = new BookCafeDao().insertBook(conn, bc);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);

		return result;
	}

	public ArrayList<BookCafe> selectByBookName(String keyword) {

		Connection conn = getconnection();
		
		ArrayList<BookCafe> BList = new BookCafeDao().selectByBookName(conn, keyword);

		close(conn);

		return BList;

	}

	public int updateBook(BookCafe b) {
		
		Connection conn = getconnection();

		int result = new BookCafeDao().updateBook(conn, b);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);

		return result;
	}

	public int deleteBook(int book_no) {
		
		Connection conn = getconnection();
		
		int result = new BookCafeDao().deleteBook(conn, book_no);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);
		
		return result;
	}

	public int lendBook(int book_no, int stock) {

		Connection conn = getconnection();

		int check = new BookCafeDao().check(conn, book_no, stock);
		
		int result = 0;
		
		if (check == 1) {
			result = new BookCafeDao().lendBook(conn, book_no, stock);
		} else if (check == 2) {
			result = 2;
		} else if (check == 0) {
			result = 0;
		}
		return result;
	}

	public int receiveBook(int book_no, int stock) {
		
		Connection conn = getconnection();
		
		int result = new BookCafeDao().receiveBook(conn, book_no, stock);
		
		return result;
	}

}
