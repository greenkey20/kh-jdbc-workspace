// 2021.11.23(화)
package src.com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import src.com.kh.model.vo.Member;

/* DAO(Date Access Object): 데이터에 직접적으로 접근; 데이터베이스 관련된 작업(CRUD)을 전문적으로 담당하는 객체 -> DAO 안의 모든 메소드는 데이터베이스와 관련된 작업으로 만듦
 * CRUD: Create, Retrieve(검색), Update, Delete; SQL DML
 * SELECT
 * 
 * Controller를 통해 호출된 기능을 수행
 * DB에 직접적으로 접근한 후 해당 SQL문을 실행 및 결과 받음(JDBC)
 */

public class MemberDao {
	
	/* JDBC용 객체
	 * 1. Connection: DB의 연결 정보를 담고 있는 객체
	 * 2. (Prepared)Statement: 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체; Statement 특징 = 완성된 SQL문을 실행할 수 있는 객체
	 * PreparedStatement 클래스 = Statement클래스를 상속받는 자식클래스; 속도 더 빠름
	 * 3. ResultSet: 만일 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * JDBC 처리 순서(중요)
	 * 1) JDBC Driver 등록: 해당 DBMS(및 버전)가 제공하는 클래스 등록
	 * 2) Connection 생성: 접속하고자 하는 DB 정보를 입력해서 DB에 접속하면서 생성
	 * 3) Statement 생성: Connection 객체를 이용해서 생성
	 * 4) SQL문 전달하면서 실행: Statement 객체 이용해서 SQL문 실행; SELECT문일 경우, executeQuery() 메소드 이용 vs DML문일 경우, executeUpdate() 메소드 이용
	 * 5) 결과 받기: SELECT문일 경우, 조회된 데이터가 담겨있는 ResultSet 객체로 받음(-> 단계6a) vs DML문일 경우, 처리된 행 수(int)로 받음(-> 단계6b)
	 * 6a) ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO 객체에 담음
	 * 6b) 트랜잭션 처리; 성공이면 COMMIT, 실패면 ROLLBACK
	 * 7) 다 쓴 JDBC용 객체들은 생성된 역순으로 반드시 자원 반납(close)
	 * 8) Controller로 결과 반환: SELECT문일 경우, 6a에서 만들어진 결과 vs DML문일 경우, int(처리된 행 수)
	 * 
	 * 헷갈릴 여지는 많지만, Java 잘 따라왔다면 크게 어려울 건 없을 것임
	 */

	/**
	 * 사용자가 회원 추가 요청 시 입력했던 값들을 가지고 insert문을 실행하는 메소드
	 * @param m : 사용자가 입력했던 ID ~ 취미 값들이 담겨있는 Member 객체
	 * @return
	 */
	public int insertMember(Member m) {
		// insert문은 처리된 행 수를 반환함 -> 그 결과에 따라 트랜잭션 처리
		
		// 필요한 변수들 먼저 세팅
		int result = 0; // insert문의 처리된 결과(처리된 행 수)를 담아줄 변수
		Connection conn = null; // 접속된 DB의 연결 정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받기 위한 변수
		
		// 실행할 SQL문; Statement를 위해서 완성된 형태의 SQL문으로 만들어 두어야 함
		// INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, 'userId', 'userPwd', 'userName', 'gender', age, 'email', 'phone', 'address', 'hobby', SYSDATE)
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, "
												+ "'" + m.getUserId() + "', "
												+ "'" + m.getUserPwd() + "', "
												+ "'" + m.getUserName() + "', "
												+ "'" + m.getGender() + "', "
												+ 		m.getAge() + ", "
												+ "'" + m.getEmail() + "', "
												+ "'" + m.getPhone() + "', "
												+ "'" + m.getAddress() + "', "
												+ "'" + m.getHobby() + "', SYSDATE)";
		// 객체 m의 필드들은 캡슐화되어 있는 바, getter를 통해 접근 가능함
		// SQL문의 문자열 데이터 표시: double quotation 사이에 single quotation 하나 -> SQL문에서의 ' 입력
		// System.out.println(sql); // 위에서 완성된 쿼리문 잘 썼는지 확인용 출력
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ojdbc6.jar가 누락되었거나, 잘 추가되었지만 코드에 오타가 있을 경우, class not found exception 발생 -> 예외 처리 필요 
			
			
			// 2) Connection 객체 생성 -> url(JDBC 버전 + IP주소 + port 번호 + 식별자(SID)), 계정명 비밀번호 제공 -> DB와 연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC"); // add catch clause to surrounding try
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) 완성된 SQL문을 DB에 전달하면서 실행 후 결과(처리된 행 수) 받기
			result = stmt.executeUpdate(sql);
			
			// 6b) 트랜잭션 처리
			if (result > 0) { // 성공했을 경우
				conn.commit();
			} else { // 실패했을 경우
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // finally문은 try~ catch~ 뒤에 붙임
			try {
				// 7) 다 쓴 JDBC용 객체 자원들을 생성된 역순으로 반납(close())
				// 아래 close() 둘 다 동일하게 sql exception 발생할 수 있어서 예외 처리
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(처리된 행의 수) 반환
		return result;
	} // insertMember() 종료

	/**
	 * 사용자가 회원 전체 조회 메뉴를 선택한 결과로 select문을 실행하는 메소드
	 * @return
	 */
	public ArrayList<Member> selectAll() {
		
		// 0) 필요한 변수들 세팅: 조회된 결과(여러 회원의 정보; 여러 행)를 뽑아서 담아줄 변수 -> ArrayList 생성
		ArrayList<Member> list = new ArrayList<>(); // 텅 빈 리스트
		
		// Connection, Statement, ResultSet 미리 선언 <- finally에서 자원 반납을 하기 위해 try 전에 미리 세팅해 두어야 함(try문 안에서는 어떤 일이 일어날지 모름)
		// 위 이유 강사님의 설명 제대로 이해 못함.. try 구문 안에서는 예외 생겨 connection 닫으라고 하고, 그러면 statement 닫아야 하고 등등? 바로 finally 구문으로 갈 수 있기 때문에?
		Connection conn = null; // 접속된 DB의 연결 정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받기 위한 변수
		ResultSet rset = null; // SELECT문이 실행된 조회 결과값들이 처음에 실질적으로 담길 객체
		
		// 완성된 형태의, 실행할, SQL문
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			// IP 주소 적어야 하는데, 나는 로컬에서 사용할 것이므로 localhost 기재
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Connection한 객체를 이용해서 Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) 완성된 SQL문(SELECT)을 DB에 전달해서 실행 후 결과(ResultSet) 받기
			rset = stmt.executeQuery(sql);
			
			// 6a) 현재 조회 결과가 담긴 ResultSet에서 1행씩 뽑아서 VO 객체에 담음
			// rset.next(): 커서를 한 줄 아래로 옮겨주고, 해당 행이 존재할 경우 true vs 아니면/존재하지 않으면 false 반환
			while (rset.next()) {
				// 현재 rset의 커서가 가리키고 있는 해당 행의 데이터를 하나씩/한 컬럼씩 뽑아서 담아줄 Member 객체를 만듦
				Member m = new Member(); // 메소드 안의 지역변수 -> 반복문 1번 돌 때마다 새로운 객체가 생성됨 -> 반복이 1번 끝나기 전에 list에 해당 Member 객체를 담아주면 됨
				
				// rset으로부터 어떤 컬럼에 해당하는 값을 뽑을 것인지 제시 -> 컬럼명(대소문자 구분x) ou 컬럼 순번 -> 권장 사항 = 대문자로 컬럼명 쓰기
				// int형 값 뽑아낼 때, rset.getInt(컬럼명 ou 컬럼순번)
				// String형 값 뽑아낼 때, rset.getString(컬럼명 ou 컬럼순번)
				// Date형 값 뽑아낼 때, rset.getDate(컬럼명 ou 컬럼순번)
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 1행에 대한 모든 컬럼의 데이터/값들을 Member자료형의 각각의 필드에 담아 하나의 Member 객체에 옮겨담음 
				
				// list에 해당 Member 객체를 담아줌
				list.add(m);
				
			} // while문 영역 끝
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체 반납(생성된 순서의 역순으로)
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(모든 회원 조회 데이터) 반환
		return list;
		
	} // selectAll() 종료

	/**
	 * 사용자가 회원 검색 요청 시 입력했던 userId를 가지고 select문을 실행하는 메소드
	 * @param userId : 사용자로부터 입력받은, 검색하고자 하는, 회원 ID
	 * @return
	 */
	public Member selectByUserId(String userId) {
		
		// 0) 필요한 변수들 세팅: 조회된 결과(한 회원의 정보; 1개 행)를 담아줄 변수 선언 vs 생성은 아래 단계6a)에서 함
		Member m = null;
		
		// JDBC 관련 객체들 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// Statement -> 완성된 형태의, 실행할, SQL문
		// SELECT * FROM MEMBER WHERE USERID = 'userId'; // 단, Statement에 쓸(?) 때는 ; 생략
		String sql = "SELECT * FROM MEMBER WHERE USERID = '" + userId + "'";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) SQL문(SELECT문)을 전달해서 실행 후, 결과 받기
			rset = stmt.executeQuery(sql);
			
			// 6a) 현재 조회 결과가 담긴 ResultSet에서 VO 객체에 담기; unique 제약조건이 걸려있는 userId 검색이므로 1행만 조회가 됨
			if (rset.next()) { // 커서를 1행 (아래로) 움직여보고, 조회 결과/해당 행이 있다면 true vs 없다면 false 반환
				// 조회된 1행에 대한 모든 열/컬럼의 데이터/값을 뽑아서 하나의 Member 객체에 담음 <- Member 객체 생성과 동시에 필드값 초기화
				m = new Member(rset.getInt("USERNO")
							 , rset.getString("USERID")
							 , rset.getString("USERPWD")
							 , rset.getString("USERNAME")
							 , rset.getString("GENDER")
							 , rset.getInt("AGE")
							 , rset.getString("EMAIL")
							 , rset.getString("PHONE")
							 , rset.getString("ADDRESS")
							 , rset.getString("HOBBY")
							 , rset.getDate("ENROLLDATE"));
			} // if문 영역 끝
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체 반납(생성된 순서의 역순으로)
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(회원ID로 검색한 결과) 반환
		return m; // 조회/검색 결과가 존재하면 m은 채워진 상태로 반환됨 vs 없으면 null값(m을 초기화해주었던 값)으로 반환됨
	} // selectByUserId() 종료

	// 2021.11.23(화) 숙제
	/**
	 * 사용자가 회원 이름 키워드 검색 요청 시 입력했던 키워드를 가지고 select문을 실행하는 메소드
	 * @param userNameKeyword : 사용자로부터 입력받은, 검색하고자 하는, 회원 이름 키워드
	 * @return
	 */
	public ArrayList<Member> selectByUserNameKeyword(String userNameKeyword) {
		
		// 0) 필요한 변수들 세팅
		// 조회된 결과(회원 이름 키워드를 포함하는 회원(들)의 정보)를 뽑아서 담아줄 변수 -> ArrayList 생성
		ArrayList<Member> list = new ArrayList<>(); // 텅 빈 리스트
		
		// Connection, Statement, ResultSet 미리 선언
		Connection conn = null; // 접속된 DB의 연결 정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받는 변수
		ResultSet rset = null; // SELECT문 실행된 조회 결과값들이 처음에 실질적으로 담길 객체
		
		// 완성된 형태의, 실행할, SQL문
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%userNameKeyword%'; // 나는 처음에 ('%userNameKeyword%')라고 썼고 동작은 했으나, () 필요 없음
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%" + userNameKeyword + "%'";
		// System.out.println(sql);
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) Statement로 완성된 SQL문(SELECT)을 DB에 전달해서 실행 후 결과(ResultSet) 받기
			rset = stmt.executeQuery(sql);
			
			// 6a) 현재 조회 결과가 담긴 ResultSet의 데이터를 1행씩 뽑아서 VO 객체에 담음
			while (rset.next()) { // 커서의 다음 행이 있는지 확인
				// 내가 숙제하며 쓴 방식
				Member m = new Member(rset.getInt("USERNO")
									, rset.getString("USERID")
									, rset.getString("USERPWD")
									, rset.getString("USERNAME")
									, rset.getString("GENDER")
									, rset.getInt("AGE")
									, rset.getString("EMAIL")
									, rset.getString("PHONE")
									, rset.getString("ADDRESS")
									, rset.getString("HOBBY")
									, rset.getDate("ENROLLDATE"));
				list.add(m);
				
				// 더 간단히 쓸 수 있는, 강사님 방식 = Member 객체 만들어서 바로 list에 넣음
				/*
				list.add(new Member(rset.getInt("USERNO")
								  , rset.getString("USERID")
								  , rset.getString("USERPWD")
								  , rset.getString("USERNAME")
								  , rset.getString("GENDER")
								  , rset.getInt("AGE")
								  , rset.getString("EMAIL")
								  , rset.getString("PHONE")
								  , rset.getString("ADDRESS")
								  , rset.getString("HOBBY")
								  , rset.getDate("ENROLLDATE")));
								  */
			} // while문 영역 끝
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC 객체들은 생성된 역순으로 자원 반납
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(회원 이름 키워드로 검색한 결과) 반환
		return list;
	} // selectByUserNameKeyword() 종료

	/**
	 * 사용자가 회원 정보 변경 요청 시 입력했던 userId 값을 가지고 그에 매칭되는 userPwd를 찾고자 하는 select문을 실행하는 메소드
	 * @param userId : 사용자로부터 입력받은, 비밀번호를 찾고자 하는, 회원 ID
	 * @return
	 */
	public String logIn(String userId) {
		
		// 0) 필요한 변수 세팅
		// 조회 결과(인자로 받은 ID의 비밀번호) 담아줄 변수 -> String
		String realUserPwd = null;
		
		// JDBC 객체 변수 생성
		Connection conn = null; // 접속된 DB의 연결 정보를 담는 변수
		Statement stmt = null; // SQL문 실행 후 결과를 받는 변수
		ResultSet rset = null; // SELECT문 실행된 조회 결과값들이 처음에 실질적으로 담길 객체
		
		// 완성된 형태의, 실행할, SQL문
		// SELECT USERPWD FROM MEMBER WHERE USERID = 'userId'
		String sql = "SELECT USERPWD FROM MEMBER WHERE USERID = '" + userId + "'";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) 완성된 SQL문(SELECT)을 DB에 전달해서 실행 후 결과(ResultSet) 받기
			rset = stmt.executeQuery(sql);
			
			// 6a) 현재 조회 결과가 담긴 ResultSet의 데이터를 1행씩 뽑아서 VO 객체에 담음
			if (rset.next()) {
				realUserPwd = rset.getString("USERPWD");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC 객체들은 생성된 역순으로 자원 반납
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(인자로 받은 회원 ID의 비밀번호) 반환
		return realUserPwd;
	} // logIn() 종료

	/**
	 * 사용자가 회원 정보 변경 요청 시 입력했던 회원 ID, (변경하고자 하는) 컬럼명 및 (새로이 담고자 하는) 컬럼값들을 가지고 update문을 실행하는 메소드
	 * @param userId : 사용자로부터 입력받은, 회원 정보를 변경하고자 하는, 회원 ID
	 * @param newColumnsList : 변경하고자 하는 컬럼명(들)의 리스트
	 * @param newValuesList : 변경하고자/새로이 담고자 하는 컬럼값(들)의 리스트
	 * @return
	 */
	public int updateMember(String userId, ArrayList<String> newColumnsList, ArrayList<String> newValuesList) {
		
		// 0) 필요한 변수들 먼저 세팅
		int result = 0; // update문의 처리 결과(처리된 행 수)를 담아줄 변수
		Connection conn = null;
		Statement stmt = null;
		
		// 완성된 형태의, 실행할, SQL문
		// UPDATE MEMBER SET newColumnsList.get(i) = 'newValuesList.get(i)' where USERID = 'userId'
		String sql = null;
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) 완성된 SQL문을 DB에 전달해서 실행 후 결과(처리된 행 수) 받기
			for (int i = 0; i < newColumnsList.size(); i++) {
				sql = "UPDATE MEMBER SET " + newColumnsList.get(i) + " = '" + newValuesList.get(i) + "' where USERID = '" + userId + "'";
				// System.out.println(sql); // sql문 확인용 출력
				result = stmt.executeUpdate(sql);
				// System.out.println(result); // result 값 확인용 출력 -> for문 i번째 돌 때마다 1로 초기화
				// 2023.9.11(월) 0h35 나의 생각 = 이렇게 초기화하면 중간에 update 실패해도 마지막 update가 성공이면 무조건 commit하게 되니까, 올바른 방법이 아닌 것 같은데..
				// vs 매번 result 값을 다른 변수에 모아서 곱해서 그 값을 가지고 트랜잭션 처리 결정하는 것이 좋을 것 같다?
			}
			
			// 6b) 트랜잭션 처리
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
				// 7) 다 쓴 JDBC용 객체 자원들을 생성된 역순으로 반납
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(처리된 행의 수) 반환
		return result;
	} // updateMember() 종료
	
	// 2021.11.24(수) 강사님 방식
	// updateMember() 오버로딩
	public int updateMember(Member m) { // 처리할 작업들: update문 실행 -> 처리된 행의 개수(int) 반환 -> 트랜잭션 처리
		
		// 0) 필요한 변수들 세팅
		int result = 0; // Controller에 반환할 변수 만듦
		
		Connection conn = null;
		Statement stmt = null;
		
		// 실행할 SQL문
		// UPDATE MEMBER
		// SET USERPWD = 'newPwd',
		//	   EMAIL = 'newEmail',
		// 	   PHONE = 'newPhone',
		// 	   ADDRESS = 'newAddress'
		// WHERE USERID = 'userId'
		
		String sql = "UPDATE MEMBER "
				   + "SET USERPWD = '" + m.getUserPwd() + "', "
				   + 	 "EMAIL = '" + m.getEmail() + "', "
				   +     "PHONE = '" + m.getPhone() + "', "
				   +     "ADDRESS = '" + m.getAddress() + "' "
				   + "WHERE USERID = '" + m.getUserId() + "'";
		// System.out.println(sql); // 완성된 SQL문 잘 썼는지 확인용 출력
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5)
			result = stmt.executeUpdate(sql);
			
			// 6b) DML문 사용했으므로, 트랜잭션 처리
			if (result > 0) { // DML 처리가 된 row가 1개 이상 있는 경우
				conn.commit();
			} else { // DML 처리된 row가 없는 경우
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 자원 반납
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과 반환
		return result;
	} // updateMember() 종료

	/**
	 * 사용자가 회원 탈퇴 요청 시 입력했던 회원 ID 값을 가지고 delete문을 실행하는 메소드
	 * @param userId : 사용자로부터 입력받은, 탈퇴하고자 하는, 회원 ID 
	 * @return
	 */
	public int deleteMember(String userId) { // 처리할 작업들: delete문 실행 -> 처리된 행의 개수(int) 반환 -> 트랜잭션 처리

		// 0) 필요한 변수 세팅
		int result = 0; // delete문의 처리 결과(처리된 행 수)를 담을 변수
		
		Connection conn = null;
		Statement stmt = null;
		
		// 실행할 SQL문
		// DELETE FROM MEMBER WHERE USERID = 'userId'
		String sql = "DELETE FROM MEMBER WHERE USERID = '" + userId + "'";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성; 1)과 같은 try문 안에 있어야 하므로, 예외 처리 시 새로운 try~ catch~ 구문 사용(x) 끝에 catch 구문 추가(o) 
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) 완성된 SQL문을 DB에 전달해서 실행 후 결과 받기
			result = stmt.executeUpdate(sql);
			
			// 6b) 트랜잭션 처리
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
				// 7) 다 쓴 JDBC 객체 자원들을 생성된 역순으로 반납
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) Controller로 결과(처리된 행의 수) 반환
		return result;
	} // deleteMember() 종료

} // 클래스 영역 끝
