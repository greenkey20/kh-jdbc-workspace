package src.com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import static src.com.kh.common.JDBCTemplate.*;

import src.com.kh.model.dao.ProductDao;
import src.com.kh.model.vo.Product;

public class ProductService {

	public ArrayList<Product> selectAll() {

		Connection conn = getConnection();
		ArrayList<Product> plist = new ProductDao().selectAll(conn);
		close(conn);
		return plist;
	}

	public int insertProduct(Product p) {

		// Connection 객체 생성
		Connection conn = getConnection();

		// DAO 호출 시 Connection 객체와 기존에 넘기고자 했던 매개변수를 같이 넘김
		int result = new ProductDao().insertProduct(conn, p);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		// Connection 객체 반납
		close(conn);

		return result;

	}

	public ArrayList<Product> selectByProductName(String keyword) {
		
		Connection conn = getConnection();

		ArrayList<Product> pList = new ProductDao().selectByProductName(conn, keyword);

		close(conn);

		return pList;

	}

	public int updateProduct(Product p) {

		Connection conn = getConnection();

		int result = new ProductDao().updateProduct(conn, p);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);

		return result;
	}

	public int deleteProduct(String product_id) {

		Connection conn = getConnection();

		int result = new ProductDao().deleteProduct(conn, product_id);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);

		return result;
	}

}
