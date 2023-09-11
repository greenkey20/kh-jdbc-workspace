package src.com.kh.model.dao;

import src.com.kh.model.vo.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import static src.com.kh.common.JDBCTemplate.close;

public class ProductDao {

	private Properties prop = new Properties();

	public ProductDao() {
		
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Product> selectAll(Connection conn) {

		ArrayList<Product> pList = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = prop.getProperty("selectAll");
		// product_id, String product_name, int price, String description, int stock

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pList.add(new Product(rs.getString("PRODUCT_ID"), rs.getString("PRODUCT_NAME"), rs.getInt("PRICE"),
						rs.getString("DESCRIPTION"), rs.getInt("STOCK")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return pList;
	} // selectAll() 종료

	public int insertProduct(Connection conn, Product p) {
		
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertProduct");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getProduct_id());
			pstmt.setString(2, p.getProduct_name());
			pstmt.setInt(3, p.getPrice());
			pstmt.setString(4, p.getDescription());
			pstmt.setInt(5, p.getStock());

			// 4, 5) DB에 완성 된 SQL문을 실행 후 결과(*처리 된 행의 갯수)받기
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	} // insertProduct() 종료

	public ArrayList<Product> selectByProductName(Connection conn, String keyword) {
		
		ArrayList<Product> pList = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = prop.getProperty("selectByProductName");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pList.add(new Product(rs.getString("PRODUCT_ID"), rs.getString("PRODUCT_NAME"), rs.getInt("PRICE"),
						rs.getString("DESCRIPTION"), rs.getInt("STOCK")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return pList;
	} // selectByProductName() 종료

	public int updateProduct(Connection conn, Product p) {
		
		int result = 0;
		PreparedStatement pstmt = null;

		// String sql = "UPDATE PRODUCT SET PRICE = ?, DESCRIPTION = ?, STOCK = ? WHERE
		// USERID = ?";
		String sql = prop.getProperty("updateProduct");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, p.getPrice());
			pstmt.setString(2, p.getDescription());
			pstmt.setInt(3, p.getStock());
			pstmt.setString(4, p.getProduct_id());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public int deleteProduct(Connection conn, String product_id) {
		
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("deleteProduct");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product_id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

}
