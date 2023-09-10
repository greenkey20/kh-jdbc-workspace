package src.com.kh.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestRun {
    public static void main(String[] args) {
		/*
		// 사용자에게 값을 입력받아 DBMS로 전달
		Scanner sc = new Scanner(System.in);
		System.out.print("너 번호가 뭐니?");
		int num = sc.nextInt();
		sc.nextLine();
		System.out.print("너 이름이 뭐니?");
		String name = sc.nextLine();

		// 필요한 변수들 먼저 세팅
		// 1단계 끝
		int result = 0;
		Connection conn = null;
		Statement stmt = null;

		// 2단계
		// 실행 SQL("완성형태"로 만들기)
		// 맨 뒤에 세미콜론을 절대 붙이면 안된다!

		//String sql = "INSERT INTO TEST VALUES(3, '우하하', SYSDATE)";
		String sql = "INSERT INTO TEST VALUES("+
							num + ",'" + name + "', SYSDATE)";

		try {
			// 1) JDBC driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("driver 등록 성공");

			// 2) Connection 객체 생성
			// DB에 연결(url, 계정명, 비밀번호)
			conn =
				DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			System.out.println("Connection 객체 생성!");

			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			System.out.println("Statement 객체 생성!");

			// 4) SQL 쿼리 날려서 실행 후 결과받기 (처리된 행 수)
			result = stmt.executeUpdate(sql);

			// 내가 실행할 SQL문이 DML문(insert, update, delete)일 경우
			// => stmt.executeUpdate("DML문") : int
			// 내가 실행할 SQL문이 SELECT문인 경우
			// => stmt.executeQuery("SELECT문") : resultSet

			// 5) 트랜잭션 처리
			if(result > 0) { // 성공했을 경우 commit
				conn.commit();
			}else { // 아닐 경우 rollback
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 오타났대");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("커넥션객체부터 확인하래");
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(result > 0) {
			System.out.println("insert성공!");
		}else {
			System.out.println("insert실패!");
		}
		*/

        // 2. 내 pc DBMS상 JDBC계정에 TEST테이블 모든 데이터 조회해보기
        // SELECT문
        // => ResultSet(조회된 데이터들 담겨있음) 받기
        // => ResultSet으로부터 데이터 뽑기

        // 필요한 변수들 셋팅
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        // select문 실행된 조회 결과값들이 처음 담길 객체

        String sql = "SELECT * FROM TEST";

        try {
            // 1) JDBC driver 등록
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 2) Connection 객체 생성
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
            // 3) Statement 객체 생성
            stmt = conn.createStatement();
            // 4) SQL문 전달해서 실행 후 결과받기(ResultSet 객체)
            rset = stmt.executeQuery(sql);

            // 5)
            while (rset.next()) {
                // 커서를 움직여주는 역할
                // 해당행이 있으면 true / 없으면 false

                int tNo = rset.getInt("TNO");
                String tName = rset.getString("tname");
                Date tDate = rset.getDate("TDATE");
                System.out.println(tNo + ", "
                        + tName + ", " + tDate);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rset.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}