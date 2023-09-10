package src.com.kh.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TestRun {

	public static void main(String[] args) {

		// 1. 내 pc DBMS상 JDBC 계정에 TEST 테이블에 행 입력하기
		// 사용자로부터 자료 입력받아 insert하고자 함
//		Scanner sc = new Scanner(System.in);
//		System.out.print("너의 번호는 뭐니?");
//		int num = sc.nextInt();
//		sc.nextLine(); // 입력 버퍼에 개행문자 남아있는 것을 처리
//		System.out.print("너의 이름은 뭐니?");
//		String name = sc.nextLine();

		// JDBC 사용하기
		// 단계1) 필요한 변수들 먼저 세팅; 전역으로 사용하기 위해 static(?)으로 만듦
		int result = 0;
		Connection conn = null;
		Statement stmt = null; // 앞에 java.sql. 쓰면 import할 필요 없음

		// 단계2) 실행할 SQL문을 '완성 형태'로 만들기; 단, 맨뒤에 세미콜론(;) 붙이면 안 됨
		// String sql = "INSERT INTO TEST VALUES(4, '강무민', '21/04/14')"; // SQL에서는 쿼리문이지만, Java에서는 그냥 문자열 -> 쿼리문인 문자열
		// '강스노크메이든'(20bytes 초과 문자열) insert하려고 하면 오류; java.sql.SQLException: ORA-12899: value too large for column "JDBC"."TEST"."TNAME" (actual: 21, maximum: 20) -> insert 실패

		// 사용자로부터 입력받은 자료를 insert구문에 사용하는 SQL문 작성
		// String sql = "INSERT INTO TEST VALUES(" + num + ", '" + name + "', SYSDATE)"; // Oracle로 넘어갔을 때 어떻게 읽힐지 생각해서 써야 함

		// 17h40 다른 DML문 실습해보기
		// String sql = "UPDATE TEST SET TNO = 5 WHERE TNAME = '강토미'";
		String sql = "DELETE FROM TEST WHERE TNO = 4"; // sql문 2개 처리하려면 객체 하나 더 만들어야 함; 그냥 이렇게 쓰면 오류 날 것..

		// ' 앞에 탈출문자 역슬래시(\) 쓰는 건 Java에서 출력 시 필요 vs 여기서는 ' 그대로(?) 보내니까 탈출문자 필요 없음

		try {
			// 1) 외부매체와 연결하기 위해 드라이버 등록 필요 -> JDBC driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("driver 등록 성공"); // 이 문구 잘 출력되면 윗줄에서 driver 잘 등록된 것임

			// 2) Connection 객체 생성 = DB에 연결(url, 계정명, 비밀번호)
			// DriverManager 클래스의 getConnection() 메소드 사용 -> 내가 사용하고 있는 Oracle에 접속; 메소드의 인자로 넣은 내용 = sql developer 접속 시 기재한 정보
			// 위에서 객체는 생성해 두었으므로, 아래 행에서 초기화해줌
			// url = Oracle버전 + @(at, 구분자) + IP주소(컴퓨터마다 고유의 주소/127.0.0.1/나 자신의 주소/localhost + 포트 번호 + SID)
			// 논리적 주소 = 변하지 않는, 고정된 주소 vs 물리적 주소(잘 바뀜) (내가 반대로 들었나? >.<)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			System.out.println("Connection 객체 생성!");

			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			System.out.println("Statement 객체 생성!"); // 내가 확인하고자 하는 내용 출력해보기 vs 오류 발생하면 catch문으로 빠짐

			// 4) sql 쿼리 날려서 실행 후 결과(= 처리된 행 수 = int형 자료) 받기
			result = stmt.executeUpdate(sql);
			// 내가 실행할 SQL문이 DML((테이블 내의) 데이터 조작 언어; insert, update, delete)일 경우, stmt.executeUpdate("DML문") -> 반환형 = int
			// 내가 실행할 SQL문이 select문인 경우, stmt.executeQuery("SELECT문") -> ResultSet이라는 객체 반환(? 내용은 맞을텐데, 필기 제대로/똑같이 못한 듯?)

			// 5) 트랜잭션 처리
			if (result > 0) { // 성공했을 경우 commit
				conn.commit();
			} else { // 아닐 경우 rollback
				conn.rollback();
			}

		} catch (ClassNotFoundException e) { // driver는 클래스를 당겨오는 것이라, class not found exception으로 잡음
			System.out.println("드라이버 어디 있어?/오타 났대");
			e.printStackTrace();
		} catch (SQLException e) { // connection과 statement 관련
			System.out.println("Connection 객체부터 확인하래");
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if (result > 0 ) {
			System.out.println("insert 성공!"); // insert 결과는 sql developer에서 확인; 특별히 제약조건 안 걸었으므로, 이 main() 실행 횟수만큼 insert되어있음
		} else {
			System.out.println("insert 실패");
		}

		/*
		// 2. 내 pc DBMS상 JDBC 계정에 TEST 테이블 모든 데이터 조회해보기
		// SELECT문 -> ResultSet(조회된 데이터 담겨있음) 받기 -> ResultSet으로부터 데이터 뽑기

		// 단계1) 필요한 변수들 세팅/선언 -> 아래에서는 할당만 해주면 됨
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null; // SELECT문 실행된 조회 결과값들이 처음 담길 객체

		// 단계2) 실행할 SQL문을 '완성 형태'로 만들기; 단, 맨뒤에 세미콜론(;) 붙이면 안 됨
		String sql = "SELECT * FROM TEST";

		try {
			// 1) JDBC driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2) Connection 객체 생성 = DB에 연결(url, 계정명, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC"); // sql exception 처리 필요 -> 자동완성으로 'catch문 추가'

			// 3) Statement 객체 생성
			stmt = conn.createStatement(); // Statement 관련 exception은 sql exception 처리가 잡아줌

			// 4) SQL문을 전달해서 실행 후 결과 전달받기
			rset = stmt.executeQuery(sql);
			System.out.println("잘 되고 있니? 확인");

			// 5) tokenizer, iterator 등처럼 while문 써서 출력
			while (rset.next()) { // ResultSet의 next() = cursor를 움직여주는 역할; ResultSet 처음 받아오면 커서는 가장 첫번째 행에 있음 -> next()에 의해 한 칸씩 내려가면서 해당/다음 행이 있으면 true vs 없으면 false
				// 각 컬럼을 하나하나 변수에 담아 출력
				int tNo = rset.getInt("TNO"); // 커서가 위치한 행의 해당 컬럼 값 불러오기
				String tName = rset.getString("tname"); // Java에서는 문자열 자료형; 컬럼명 대소문자 상관 없이 쓰면 됨
				Date tDate = rset.getDate("TDATE"); // Date는 API인 바, import해야 함; 단, sql에 있는 Date 가져오기
				System.out.println(tNo + ", " + tName + ", " + tDate);
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
		*/

	} // main() 종료

}