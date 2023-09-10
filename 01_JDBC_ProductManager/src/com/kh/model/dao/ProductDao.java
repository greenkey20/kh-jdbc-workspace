package src.com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import src.com.kh.model.vo.Product;

public class ProductDao {

	public ArrayList<Product> selectAll() {
		ArrayList<Product> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM PRODUCT";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);

			while (rset.next()) {
				list.add(new Product(rset.getString("PRODUCT_ID"), rset.getString("PRODUCT_NAME"), rset.getInt("PRICE"),
						rset.getString("DESCRIPTION"), rset.getInt("STOCK")));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null && !rset.isClosed()) {
					rset.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	} // selectAll() 종료

	public int insertProduct(Product p) { // INSERT문 => 처리된 행의 갯수 => 트랜잭션 처리

		// 0) 필요한 변수들 먼저 셋팅

		int result = 0; // 처리된 결과 (처리된 행의 갯수) 를 담아줄 변수
		Connection conn = null; // 접속된 DB의 연결정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받기 위한 변수

		// + 필요한변수 : 실행할 SQL(미완성된 형태로)
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'xxx', 'xxx', 'xxx', 'xxx', x, 'xxx@xxx',
		// , 'xxxx', 'xxxx', 'xxx', SYSDATE)
		// + "'' + "',"

		String sql = "INSERT INTO Product VALUES( '" + p.getProduct_id() + "', '" + p.getProduct_name() + "', "
				+ p.getPrice() + ", '" + p.getDescription() + "', " + p.getStock() + ")";

		try {
			// 1) JDBC드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오타가 있을경우, ojdbc6.jar를 누락시켰을 경우
			// -> ClassNotFoundException이 발생할 수 있으므로 예외처리 꼭 해줘야함!

			// 2) Connection 객체 생성(== DB와 연결시키겠다 --> url, 계정명, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");

			// 3_1) PreparedStatement 객체 생성 (SQL문을 미리 넘겨준다.)
			stmt = conn.createStatement();

			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			// pstmt.setXXX(?의 위치, 실제값)

			// PreparedStatement의 단점
			// => 완성된 SQL문을 볼 수 없다.

			// 4, 5) DB에 완성된 SQL문을 실행 후 결과(처리된 행의 갯수) 받기
			result = stmt.executeUpdate(sql);

			// 6_2) 트랜잭션 처리
			if (result > 0) { // 1개 이상의 행이 INSERT 되었다면 == 성공했을 경우 => 커밋
				conn.commit();
			} else { // 실패했을경우 => 롤백
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			System.out.println();
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println();
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8) 결과반환
		return result; // 처리된 행의 갯수
	}

	public ArrayList<Product> selectByProductName(String keyword) {

		ArrayList<Product> list = new ArrayList<>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_NAME LIKE '%" + keyword + "%'";

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");

			stmt = conn.createStatement();

			rset = stmt.executeQuery(sql);

			while (rset.next()) {
				list.add(new Product(rset.getString("PRODUCT_ID"), rset.getString("PRODUCT_NAME"), rset.getInt("PRICE"),
						rset.getString("DESCRIPTION"), rset.getInt("STOCK")));
			}

		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null && !rset.isClosed()) {
					rset.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public int updateProduct(Product p) {

		int result = 0;

		Connection conn = null;
		Statement stmt = null;

		// UPDATE PRODUCT SET PRICE = newPrice, DESCRIPTION = 'newDesciption', STOCK =
		// newStock WHERE PRODUCT_ID = 'product_id'
		String sql = "UPDATE PRODUCT " + "SET PRICE = " + p.getPrice() + ", " + "DESCRIPTION = '" + p.getDescription()
				+ "', " + "STOCK = " + p.getStock() + " " + "WHERE PRODUCT_ID = '" + p.getProduct_id() + "'";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			stmt = conn.createStatement();

			result = stmt.executeUpdate(sql);

			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	} // updateProduct() 종료

	public int deleteProduct(String productId) {
		int result = 0;
		Connection conn = null;
		Statement stmt = null;
		String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID = '" + productId + "' ";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
