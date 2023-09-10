// 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 강사님과 함께 확인/실습
package src.com.kh.model.dao;

import java.sql.Connection; // import = 이것들의 위치를 알려주는 것 -> 나의 질문 = Connection, DriverManager 등은 무엇인가? 패키지? 자료형? -> 커서 올리면 설명 볼 수 있음(interface..)
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import src.com.kh.model.vo.Member;

public class MemberDao {
	
	/* JDBC용 객체
	 * 1. Connection: DB의 연결정보(IP주소, port번호, 계정명, 비밀번호)를 담고 있는 객체
	 * 2. (Prepared)Statement: 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체; Statement 사용 시에는 완성된 SQL문 전달해야 함
	 * 3. ResultSet: 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * Statement(부모)와 PreparedStatement(자식) 관계 -> PreparedStatement 클래스 = Statement클래스를 상속받는 자식클래스; 속도 더 빠름
	 * PreparedStatement: SQL문을 바로 실행하지 않고, 잠시 보관하는 개념 -> 미완성된 SQL문을 먼저 전달하고, 실행하기 전에 완성 형태로 만든 후 실행만 하면 됨
	 * 미완성된 SQL문 만들기: 사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치 holder)로 확보 -> 각 위치홀더에 맞는 값들을 세팅
	 * 
	 * 차이점
	 * 1) Statement는 완성된 SQL문 vs PreparedStatement는 미완성된 SQL문
	 * 2) 객체 생성 시
	 * stmt = conn.createStatement();
	 * pstmt = conn.prepareStatement(sql); // 객체 생성 시 미완성/구멍뚫은 상태의 SQL문(틀) 먼저 전달
	 * 3) Statement로 SQL문 실행 시 결과 = stmt.executeXXXX(sql);
	 * 	  PreparedStatement로 SQL문 실행 시 ?로 빈 공간을 실제 값으로 채워준 뒤 실행
	 *  pstmt.setString(?위치, 실제값);
	 *  pstmt.setInt(?위치, 실제값); -> 결과 = pstmt.executeXXXX(); 
	 * 
	 * JDBC 처리 순서; JDBC 사용하고 싶으면 API 이렇게 쓰라고 미리/이미 정해져 있는 것 -> 이해할 필요 없고, 외워서 사용하면 됨 vs 개발자 재량껏 할 수 있는 건 단계 6a)과 8)
	 * 1) JDBC Driver 등록: 해당 DBMS가 제공하는 클래스 등록
	 * 2) Connection 객체 생성: 접속하고자 하는 DB의 (접속)정보(url, 계정, 비밀번호)를 입력해서 DB에 접속하면서 생성
	 * 3a) PreparedStatement 객체 생성: Connection 객체를 이용해서 미완성된 SQL문을 담아서 생성; 완성된 SQL문 담을 수도 있긴 함 e.g. SELECT * FROM MEMBER 같이 따옴표 쓸 필요 없는 쿼리문 등
	 * 3b) 현재 미완성된 SQL문을 완성 형태로 채움: 미완성된 경우에만 해당 vs 완성된 경우에는 생략 가능
	 * 4) SQL문 실행: executeXXX(); sql 매개변수 없음 -> SELECT문의 경우 executeQuery() 메소드 호출 vs 나머지 DML문(+테이블 만들기(create) 등 DDL문)의 경우 executeUpdate() 메소드 호출
	 * 5) 결과 받기: SELECT문의 경우 ResultSet 객체(조회된 데이터가 담겨있음)로 받음 -> 6a) ResultSet에 담겨있는 데이터를 하나씩 뽑아서 VO 객체에 담기; 많으면 ArrayList로 관리
	 * 			  나머지 DML문의 경우 int형(처리된 행의 개수)으로 받음 -> 6b) 트랜잭션 처리; 성공이면 COMMIT vs 실패면 ROLLBACK
	 * 7) 다 쓴 JDBC용 객체들은 반드시 자원 반납 <- 생성된 순서의 역순으로 close()
	 * 8) Controller로 결과 반환: SELECT문의 경우 6a)에서 만들어진 결과 vs 나머지 DML문의 경우 처리된 행의 개수 
	 */

	public int insertMember(Member m) { // INSERT문 -> 결과 = 처리된 행(row)의 개수(count) -> 트랜잭션 처리
		
		// 0) 필요한 변수들 먼저 세팅
		// insert문 처리 결과(처리된 행의 개수)를 담아줄(+Controller로 반환할) 변수
		int result = 0;
		
		// JDBC 관련 객체
		Connection conn = null; // 접속된 DB의 연결 정보를 담는 변수
		PreparedStatement pstmt = null; // SQL문 실행 후 결과를 받기 위한 변수
		
		// 미완성된 형태의, 실행할, SQL
		
		// INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, 'userId', 'userPwd', 'userName', 'gender', age, 'email@xx', 'phone', 'address', 'hobby', SYSDATE)
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT)";
		
		try {
			// 1) JDBC Driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오타가 있을 경우, 또는 ojdbc6.jar를 누락시켰을 경우 등 Class Not Found exception이 발생할 수 있으므로, 예외 처리 꼭 해줌
			
			// 2) url, 계정명, 비밀번호 -> Connection 객체 생성 = DB와 연결시킴
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// sql exception 발생 가능한 바, 1)에서 만든 try~ catch~ 구문 아래에 catch문 추가
			
			// 3a) PreparedStatement 객체 생성 + SQL문을 미리 넘겨줌
			pstmt = conn.prepareStatement(sql);
			// 3b) 미완성된 SQL문일 경우, 완성시켜주기 <- pstmt.setXXX(?의 위치, 실제값)
			pstmt.setString(1, m.getUserId()); // String은 '' 알아서 붙여서 입력해줌
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge()); // 숫자는 숫자 입력해줌
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			// PreparedStatement의 단점 = 완성된 SQL문을 볼 수 없음
			
			// 4,5) 완성된 SQL문을 DB에 실행 후 결과(처리된 행의 개수) 받기
			result = pstmt.executeUpdate(); // Statement와 부모-자식 관계인 바, 실행할 때 호출하는 메소드 동일
			
			// 6b) 트랜잭션 처리
			if (result > 0) { // 1개 이상의 행이 INSERT 되었다면 = 성공했을 경우 -> COMMIT 
				conn.commit();
			} else { // 실패했을 경우 -> ROLLBACK
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("오타 문제야 >.<");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("위치홀더(?) 제대로 안 채워넣었어 ㅠ.ㅠ");
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체 자원 반납 -> 생성된 순서의 역순으로 close() cf. (간단히 설명하자면) 역순으로 안 닫으면 안 닫힘 
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 이 메소드를 호출한 곳으로 결과 반환
		return result; // 처리된 행의 개수 -> 처리된 것이 없으면 메소드 초반에 초기화해준 값 0
	} // insertMember() 종료

	// 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 2021.11.25(목) 강사님과 함께 실습+설명
	public ArrayList<Member> selectAll() { // 이 메소드를 부르면/호출하면 돌아갈 때 ArrayList<Member>와 같이 생긴 것을 반환함
		// SELECT문 실행 -> ResultSet 형태 반환 -> Member 형태로 한 줄 한 줄 읽어내서, ArrayList에 담아 반환하고자 함
		
		// 이것저것 담을 수 있는 마법의 리스트 ArrayList -> Member만 들어갈 수 있는 (Array)List를 만듦
		ArrayList<Member> list = new ArrayList<>(); // 현재 텅 빈 리스트
		
		// Dao에서는 DB와 왔다갔다/쿵짝쿵짝 하며 작업함 -> 이를 위해 필요한 객체들 있음
		// 변수 선언 시 아무 것/내용물도 넣어두지 않음(null)
		Connection conn = null; // DB 사용하려면 우선 '접속'해야 함 -> 접속된 DB의 연결 정보를 담는 변수 선언
		PreparedStatement pstmt = null; // SQL문 실행 후 결과를 받기 위한 변수 선언
		ResultSet rset = null; // SELECT문이 실행된 조회 결과값들이 처음에 실질적으로 담길 객체
		
		String sql = "SELECT * FROM MEMBER ORDER BY USERNO";
		// * = MEMBER 테이블에서 전체 컬럼(o) 행(x)을 조회 -> sql developer에서 SELECT문 질의 결과 = result set
		
		try {
			// JDBC 사용을 위해 정해진 도구/규약/API -> 사용하는 방법만 알면 됨
			// 1) JDBC Driver(사용하고자 하는 모든 도구가 들어있음) 등록
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 수업시간에는 고정적으로 이 도구를 사용함; 에러 msg = 클래스를 못 찾는 예외가 발생할 수 있음
			
			// 2) Connection 객체 생성 -> Oracle/sql developer에 연결됨
			// 위에서 만든 변수 사용 -> . = 참조 연산자; .의 왼쪽 위치에 있는 오른쪽의 무언가를 사용/접근 -> url, 계정명, 비밀번호
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC"); // 수업시간에는 고정적으로 사용하는 정보; 에러 msg = sql 예외가 발생할 수 있음
			
			// 3) PreparedStatement 객체 생성 -> SQL문 보내서 실행
			// 메소드 초반 pstmt 변수 선언 시 내용물 비워두었는데, 여기서
			pstmt = conn.prepareStatement(sql);
			
			// 4) pstmt.executeQuery() = SQL 쿼리문을 날림
			// 5) -> DB로부터 result set이 돌아옴 -> Java에서는 ResultSet rset에 담음
			rset = pstmt.executeQuery();
			
			// 6a) 행이 몇 개일지 모름 -> 반복문 while 사용 (vs 반복 횟수 정확히 알 때는 for문 사용) 
			while (rset.next()) { // () 안의 조건식이 true일 경우 반복문이 돎 vs false일 경우 반복문이 돌지 않음
				// 처음에 result set 불러오면 커서는 컬럼명 위에 있음 -> rset.next() = 커서를 1줄 아래로 옮겨/내려줌 -> 해당 행이 존재할 경우/그 행에 데이터가 있으면 true vs 없으면 false 반환
				
				// 현재 rset의 커서가 가리키고 있는 해당 행의 데이터를 (컬럼별로) 하나씩 뽑아서 Member 객체(MEMBER 테이블의 모양/컬럼에 따라 만들었음)(의 필드)에 담고자 함 -> Member 객체 먼저 생성
				Member m = new Member();
				
				// rset으로부터 어떤 컬럼에 해당하는 값을 뽑을 것인지 제시
				// rset.getInt("컬럼명") -> 해당 컬럼에 있는 int형 값을 뽑아냄
				// rset.getString("컬럼명") -> 해당 컬럼에 있는 String형 값을 뽑아냄
				// rset.getDate("컬럼명") -> 해당 컬럼에 있는 Date형 값을 뽑아냄
				
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
				m.setEnrolldate(rset.getDate("ENROLLDATE"));
				// result set의 1개 행에 대한 모든 컬럼의 데이터값들을 각각의 필드에 담아 하나의 Member 객체 m에 옮겨담기 완료
				
				/*
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
									*/
				list.add(m); // 위와 같이 채워진 Member 객체 하나를 list에 추가 -> 반복문인 바, while문 시작 부분의 조건식 부분으로 돌아감 
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체들을 생성된 순서의 역순으로 반납
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 결과 반환(필기 다 못함)
		return list;
		// return = 이 메소드를 호출한 곳으로 돌아감 + 반환할/돌려줄 값이 있는 경우 기재
	}  // selectAll() 종료

	public Member selectByUserId(String userId) {
		
		Member m = null; // 2021.11.25(목) 수업시간에 강사님께서는 new Member()로 초기화하심 -> 존재하지 않는 userId 검색했을 때 검색 결과 'Member [userNo=0, userId=null, userPwd=null, userName=null, gender=null, age=0, email=null, phone=null, address=null, hobby=null, enrolldate=null]' 나옴
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) { // 1건만 반환받으니까 조건문 if로 걸러주는 것(?) 맞나?
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
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return m;
	} // selectByUserId() 종료

	public ArrayList<Member> selectByUserName(String userNameKeyword) {
		
		// 0) 필요한 변수들 세팅
		ArrayList<Member> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 나의 숙제 수행 시, 강사님께서 어려울 거라고 + 다음날 수업시간에 같이 해보자고 하셔서, LIKE 사용 시도 안 함..
		// String sql = "SELECT * FROM MEMBER WHERE USERNAME = ?"; // 사용자의 이름에 맞는 행 검색
		// 문제 의도에 더 맞는 SQL문: String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%'";
		
		// 안 되는 방법: String sql = "SELECT * FROM MEMBER WHERE USER LIKE '%?%'"; -> '%'keyword'%'로 완성되어 정상 수행 안 됨
		// userNameKeyword = "끼";
		// String sql = "SELECT * FROM MEMBER WHERE USER LIKE '%?%'";
		// pstmt.setString(1, keyword); // "SELECT * FROM MEMBER WHERE USER LIKE '%'끼'%'" 이렇게 SQL문 완성되어 날아감
		
		// 방법1)
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'"; // SQL || = 연결연산자 -> '%' || 'keyword' || '%' = '%keyword%'
		// 방법2)
		// String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3a)
			pstmt = conn.prepareStatement(sql);
			// 3b)
			// 방법1) "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'" -> 이 방법이 유지/보수에는 더 좋을 듯(강사님 의견)
			pstmt.setString(1, userNameKeyword);
			// 방법2) "SELECT * FROM MEMBER WHERE USERNAME LIKE ?" -> 깍두기 %keyword%를 Java에서 붙여서 넘겨줌 -> 가독성은 이 방법이 더 나읏 듯(강사님 의견)
			// pstmt.setString(1, "%" + userNameKeyword + "%");

			// 4,5)
			rset = pstmt.executeQuery();
			
			// 6a)
			while (rset.next()) {
				// Member 객체 만드는 법 = new 연산자 + Member() 생성자 호출 -> 매개변수 받는 생성자 호출하면 인자로 넣어준 값들로 필드 값들 초기화 가능; 내가 Member 클래스에서 만든 생성자의 매개변수 개수와 같아야 함
				// list에 넣으면서 Member 객체 생성함 <- 이렇게 할 수 있으려고 생성자 만든 것임
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
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	} // selectByUserName() 종료

	public int updateMember(Member m) {
		
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// String userId, String newPwd, String newEmail, String newPhone, String newAddress, String newHobby
		String sql = "UPDATE MEMBER SET USERPWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ?, HOBBY = ? WHERE USERID = ?";
		// UPDATE MEMBER SET USERPWD = 'newPwd', EMAIL = 'newEmail'. PHONE = 'newPhone', ADDRESS = 'newAddress', HOBBY = 'newHobby' WHERE USERID = m.getUserId()
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getHobby());
			pstmt.setString(6, m.getUserId());
			
			result = pstmt.executeUpdate();
			
			// 2021.11.24(수) 숙제 수행 시, 트랜잭션 처리 누락한 것 같은데, 자료 수정 사항이 DB에 반영이 된 것 같다..? e.g. 해피 이메일 주소 변경 등
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
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	} // updateMember() 종료
	
	public int deleteMember(String userId) { // 아직 구상이 안 되어서 무엇을 반환해야 할지 모를 때에는 일단 void로 만듦; 한 번에 완벽하게 만들기보다는 만들면서 수정..
		// 덜 헷갈리도록 동일한 기능을 하는 메소드는 메소드명, 변수명 등 통일해줌 e.g. View-Controller-Dao 클래스 간 deleteMember(), 사용자가 입력한 userId 전달
		
		// JDBC 단계0) 필요한 변수들 세팅
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		// 실행할 SQL문: "DELETE FROM MEMBER WHERE USERID = 'userId'
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			// 1)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// 3a)
			pstmt = conn.prepareStatement(sql);
			// 3b)
			pstmt.setString(1, userId);
			
			// 4,5)
			result = pstmt.executeUpdate(); // 전송한 SQL문에 의해 아무 것도 안 바뀌었다면 0 반환 vs 바뀐 것이 있다면 처리된 행 개수(1 이상의 정수) 반환
			
			// 6) 트랜잭션 처리: commit 또는 rollback 중 하나만 수행해야 함 -> result 값을 가지고 조건문 만들어서 처리
			if (result > 0) { // 회원 탈퇴 처리가 1개 이상 되었다면
				conn.commit(); // 변경 내용 확정
			} else {
				conn.rollback(); // 되돌림
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7) 자원 반납
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 회원 탈퇴 처리 성공 ou 실패에 따라 Controller에서도 View에서 보여줄 내용 결정해야 함 -> Controller로 result 반환
		return result;
	} // deleteMember() 종료
	
} // 클래스 영역 끝
