// 2021.11.29(월) 9h
package src.com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	// JDBC 과정/수업 중 반복적으로 쓰이는 구문들을 각각의 메소드로 정의해둘 곳 -> 재사용할 목적으로 공통 템플릿(template) 작업 진행
	// 이 클래스에서의 모든 메소드들은 다 static 메소드로 만듦 -> 싱글톤패턴 = 메모리 영역에 단 1번만 올라간 것을 재사용한 개념
	
	// cf. Math클래스는 모든 필드와 메소드(의 접근제한자)가 static으로 되어있음 -> static 메모리 = 프로그램 시작~끝 사용 가능; 메모리에 항상 올라와있음 -> 언제든지, 객체 생성(heap 메모리 영역에 올림) 없이, 사용 가능/객체 생성할 필요 없음 = 굳이 생성자 필요 없음
	// static 메모리영역에 있는 것들은 객체 생성 안 해도 사용 가능
	
	// Dao 클래스 메소드들의 공통적인 부분 뽑아내기
	// 1. DB와 접속된 Connection 객체를 생성해서 반환시켜주는 메소드
	public static Connection getConnection() {
		
		/* 기존 방식 = JDBC Driver 구문, 내가 접속할 DB의 url 정보, 계정명, 비밀번호를 Java source codes 내에 명시적으로 작성함 = 정적/hard coding 방식 -> 나쁜 건 아님; 일 바쁘면/마감시한 촉박하면 이렇게 함; 사고날 일 없고, 편함
		 * 문제점 = DBMS가 변경되었을 경우 ou 접속할 url, 계정명, 비밀번호가 변경되었을 경우, Java source codes 수정해야 함 -> 수정한 코드 내용을 반영시키고자 한다면 프로그램을 재구동해야/껐다 켜야 함 -> 사용자 입장에서 프로그램 사용 중 비정상적으로 종료되었다가 다시 구동될 수 있음 -> 유지/보수 불편 
		 * 해결 방식 = DB 관련된 정보들을 별도로 관리하는 외부 파일(.properties 확장자)로 만들어서 관리; 외부 파일로 key에 대한 value를 읽어들여서 반영시킴 = 동적 coding 방식 -> 유지/보수 용이
		 * 
		 * 2021.11.29(월) 10h
		 * 설정과 관련된 정보가 외부 파일에 존재 -> DB 관리자 등 Java 코드 모르는/일반 컴퓨터 사용자도 접속 정보를 쉽게 변경 가능
		 * "resources/driver.properties"라는 파일(정보)을 읽어들이는 시점 = Connection 객체 생성 시/getConnection() 할 때 = 정보가 필요할 때 -> 프로그램 껐다 켜지 않고도 새로운 정보를 프로그램에 (실시간으로) 반영 가능
		 * 
		 * cf. 주차 타워 관리 Java 프로그램 + DB 연결 시, list vs 배열
		 * list = 추가/삭제/수정 시 사용 권장/편리함 + 주차장의 고정된 크기는 조건문 등으로 처리
		 * 배열 = 데이터 박아놓고 단순 조회 e.g. 주차장의 크기는 변동x/고정되어있음 -> 배열 사용의 불편함을 감수하고 사용하느냐는 나의 판단 + 배열이 정해진 메모리를 사용하므로 메모리 사용 효율 높음
		 */
		
		// 동적 코딩 방식을 적용하기 위해 + Properties 파일 읽기 위해, Properties 객체를 생성
		Properties prop = new Properties();
		
		// Connection 객체를 담을 그릇 생성
		Connection conn = null;
		
		// 연결시키기 = JDBC 단계1,2)
		try {
			// load() 메소드 -> 읽기 = prop 객체로부터 load() 메소드를 이용해서 각 키에 해당되는 value 가져오기
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// Class.forName("oracle.jdbc.driver.OracleDriver"); // 못 박아놓고 사용 = 정적 코딩 방식
			Class.forName(prop.getProperty("driver"));
			
			// conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC"); // 못 박아놓고 사용 = 정적 코딩 방식
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return conn; // 만든 Connection 객체를 이 메소드 호출한 곳으로 반환
		
		// 이 메소드 안에서는 conn.close() 할 수 없음; try~ catch~문 뒤에서 하면 conn 반환 불가능 vs return conn; 뒤에서 하면 이미 돌아갔기 때문에 close 불가능
	} // getConnection() 종료
	
	// 2. 전달받은 JDBC용 객체를 각 객체별로 반납시켜주는 메소드
	// 2a) Connection 객체를 전달받아서 반납시켜주는 메소드
	public static void close(Connection conn) {
		try {
			// 주의사항: 무조건 Connection 객체를 닫는 것x; 만약에 conn이 null(e.g. DB 비밀번호 변경 등)이면 NullPointException 발생 가능
			// 지금까지는 null point exception이 왜 안 나왔는가?(=개발자로서 좋은 질문) -> null point exception은 runtime/unchecked exception = 개발자가 생각했던 것과 달리 프로그램 실행 시(o) 컴파일 시 나오는, 문법을 잘못쓴 게 아님(x) 발생되는 굳이 try~ catch~ 쓰지 않고/예외 처리할 필요 없고, 조건문으로 해결 가능
			// 개발자가 conn에 잘 넣었겠지 하고 컴파일러가 체크 안 함 -> 개발자가 체크해야 함; 예외가 발생할 법한 것들은 미리 처리해두어야 함
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // close() 종료
	
	// 2b) Statement 객체를 전달받아서 반납시켜주는 메소드
	public static void close(Statement stmt) { // 메소드명과 반환형이 같더라도 매개변수가 다르기 때문에 오버로딩; 동일한 메소드 signature(개념 기억이 안 나네..)를 갖는..
		// PreparedStatement는 Statement 클래스의 자식클래스로써, 부모의 자료형 사용 가능 -> 1가지 타입으로 여러 가지 자료형 사용 가능 = 다형성 -> 부모의 close() 메소드 사용 가능; PreparedStatement 객체 또한 이 메소드의 매개변수로 전달 가능
		try {
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 2c) ResultSet 객체를 전달받아서 반납시켜주는 메소드(오버로딩 적용)
	public static void close(ResultSet rset) {
		try {
			if (rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 이전 Dao 클래스에서는 commit/rollback을 try문 안에서 했었는데, 
	// 3. 전달받은 Connection 객체를 가지고 트랜잭션 처리를 해주는 메소드
	// 3a) 전달받은 Connection 객체를 가지고 COMMIT 시켜주는 메소드
	public static void commit(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 3b) 전달받은 Connection 객체를 가지고 ROLLBACK 시켜주는 메소드
	public static void rollback(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // rollback() 종료

} // 클래스 영역 끝
