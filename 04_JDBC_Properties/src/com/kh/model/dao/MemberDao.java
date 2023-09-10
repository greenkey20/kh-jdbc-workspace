// 2021.11.25(목) 16h
package com.kh.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.model.vo.Member;

public class MemberDao {
	/* JDBC용 객체
	 * - Connection : DB의 연결정보를 담고 있는 객체(ip주소, port번호, 계정명, 비밀번호)
	 * - (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
	 * - ResultSet : 만일 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * ** PreparedStatement 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념
	 * 			미완성된 SQL문을 먼저 전달하고 실행하기전에 완성 형태로 만든 후 실행만 하면 됨  
	 *    		미완성된 SQL문 만들기 (사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치홀더)로 확보
	 * 			각 위치홀더에 맞는 값들을 세팅
	 * 
	 * ** Statement(부모)와 PreparedStatement(자식) 관계
	 * * 차이점
	 * 1) Statement는 완성된 SQL문, PreparedStatement는 미완성된 SQL문
	 * 
	 * 2) Statement 객체 생성시			stmt = conn.createStatement();
	 *    PreparedStatement 객체 생성 시 pstmt = conn.prepareStatement(sql);
	 *    
	 * 3) Statement로 SQL문 실행 시 			결과 = stmt.executeXXXX(sql);
	 * PreparedStatement로 SQL문 실행 시 ?로 빈 공간을 실제 값으로 채워 준뒤 실행한다.
	 * 												pstmt.setString(?위치, 실제값);
	 * 												pstmt.setInt(?위치, 실제값);
	 * 										결과 = pstmt.executexxxx();
	 * 
	 * ** JDBC 처리순서
	 * 1) JDBC Driver등록 : 해당 DBMS가 제공하는 클래스 등록
	 * 2) Connection 객체 생성 : 접속하고자하는 DB의 정보를 입력해서 DB에 접속하면서 생성(url, 계정, 비번)
	 * 3_1) PreparedStatement객체 생성 : Connection객체를 이용해서 생성(미완성된 SQL문을 담아서)
	 * 3_2) 현재 미완성된 SQL문을 완성형태로 채우기
	 * 						=> 미완성된 경우에만 해당 / 완성된 경우에는 생략 가능
	 * 4) SQL문 실행 : executeXXX() => sql매개변수 없음!!
	 * 				> SELECT문의 경우 : executeQuery() 메소드 호출
	 * 				> 나머지 DML문의 경우 : executeUpdate() 메소드 호출
	 * 5) 결과 받기 :
	 * 				> SELECT문의 경우 : ResultSet 객체(조회된 데이터들이 담겨있음)로 받기 => 6_1)
	 * 				> 나머지 DML문의 경우 : int형(처리된 행의 갯수)으로 받기 => 6_2)
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO객체에 담기(많으면 ArrayList로 관리)
	 * 6_2) 트랜잭션처리(성공이면 COMMIT, 실패면 ROLLBACK)
	 * 7) 다 쓴 JDBC용 객체들은 반드시 자원반납(close()) => 생성된 순서의 역순으로!!
	 * 8) 결과 반환 (Controller로)
	 * 			> SELECT문의 경우 6_1) 에서 만들어진 결과
	 * 			> 나머지 DML문의 경우 처리된 행의 갯수
	 * 
	 * 2021.11.25(목) 16h
	 * project02까지 Dao 클래스에 기능이 너무 몰려있었음(Dao 클래스에 과부하 걸림) + Dao 클래스에 코드 반복 많음 + 사실상 예외처리는 무척 복잡/정교함
	 * 개발자는 반복 지양해야 함
	 */
	
	private Properties prop = new Properties(); // properties 객체 생성 + 이 클래스만 사용할 수 있도록 캡슐화(접근제한자 private)
	
	// 생성자 내부에 파일 호출하는 코드 작성 -> Dao 객체 생성 시 xml 파일 다시 읽어옴
	public MemberDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int insertMember(Connection conn, Member m) {
		// INSERT문 실행 -> 처리된 행의 개수 반환 -> 트랜잭션 처리

		// 0) 필요한 변수 먼저 세팅 
		int result = 0; // 처리된 결과/행의 개수를 담아줄 변수
		// Connection 객체는 Service에서 받음
		PreparedStatement pstmt = null; // SQL문 실행 후 결과를 받기 위한 변수
		
		// 미완성된 형태의, 실행할, SQL문
		// String sql = ..(기존 SQL문)
		String sql = prop.getProperty("insertMember");
		
		// 1,2)는 해서 넘겨받음
		
		try {
			// 3a) PreparedStatement 객체 생성 + SQL문을 미리 넘겨줌
			pstmt = conn.prepareStatement(sql);
			
			// 3b) 미완성된 SQL문일 경우 완성시켜주기
			// pstmt.setXXX(?의 위치, 실제값)
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			// 4,5) 완성된 SQL문을 DB에서 실행한 뒤, 결과/처리된 행의 개수 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 더 이상 쓸 일/필요 없는 것들의 자원 반납
			close(pstmt);
		}
		
		// 6) 트랜잭션 처리는 Connection 객체를 가지고 하는 바, Connection 객체를 만든 Service에서 할 것임
		// -> service로 돌아가서 트랜잭션 처리할 때 result 필요하므로 가지고 돌아감
		return result;
	} // insertMember() 종료
	
	// 2021.11.25(목) 9h
	public ArrayList<Member> selectAll(Connection conn){
		
		// 0) 필요한 변수들 세팅
		// SELECT문 실행 -> ResultSet 형태로 DB로부터 결과가 옴 -> 데이터의 양이 많을 수 있는데, 이런 것을 잘 담을 수 있는 ArrayList 객체에 담아서 반환하고자 함
		ArrayList<Member> list = new ArrayList<>(); // 조회 결과를 담을 ArrayList 하나 생성 -> 현재는 텅빈 리스트; 값은 아직 없음
		
		// JDBC 관련 객체
		PreparedStatement pstmt = null; // SQL문 실행 + 결과 받을 변수
		ResultSet rset = null; // SELECT문이 실행된 조회 결과 값들이 처음 담길 객체
		
		// 실행할 SQL문
		// String sql = "SELECT * FROM MEMBER ORDER BY USERNO ASC"; // *(모든 컬럼) = (특히 컬럼 수가 많은 경우) 연산/처리 속도 느려짐
		/* 일할 때는 아래와 같이 특정 컬럼 지정해서 조회하는 경우 많음 -> 어떤 컬럼을 조회하는지 쉽게 볼 수 있음 + 연산/처리 속도 비교적 빠름
		String sql = "SELECT ?"
						+ ", ?"
						+ ", ?"
						+ ", ?"
						+ ", ?"
						+ ", ?"
						+ ", ?"
						+ ", ? "
					+ "FROM "
					+ "MEMBER"; (띄어쓰기 컨벤션 제대로 필기 못함)
					*/
		String sql = prop.getProperty("selectAll");
		
		try {
			// 3a) Connection 객체 가지고 PreparedStatement 객체 생성 + 이 때 SQL을 인자로 넣음 
			pstmt = conn.prepareStatement(sql);
			// 3b) 미완성된 SQL문이라면 완성시켜주기 vs 위와 같이 미완성된 부분 없다면 이 단계 생략 가능
			
			// 4) SQL문(SELECT문) 실행 -> 5) 결과(ResultSet) 받기
			rset = pstmt.executeQuery();
			
			// 6a) 현재 조회 결과가 담긴 ResultSet에서 1행씩 뽑아서 VO 객체에 담기
			// rset.next(): 커서를 내림 -> 행이 있으면 true vs 없으면 false
			while (rset.next()) {
				// rset의 커서 위치에 있는 데이터를 뽑아서 Member 객체에 담음
				Member m = new Member(); // 대입연산자의 우측 = 빈 객체 = 일종의 값 <- new(-> heap 메모리 영역에 올라감) Member()(필드 값 없는/null인 Member 객체)
				
				// rset으로부터 무엇을 뽑을 것인지 알려줘야 함 -> 권장 사항 = "컬럼명" 대문자로 쓰기
				// rset.getInt("컬럼명" ou 컬럼순서): int형 컬럼값 뽑을 때
				// rset.getString("컬럼명" ou 컬럼순서): String형 컬럼값 뽑을 때
				// rset.getDate("컬럼명" ou 컬럼순서): Date형 컬럼값 뽑을 때
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
				// userNo, enrolldate 없는 Member 생성자 만들었으므로, USERNO, ENROLLDATE 정보 안 빼와도 오류 발생은 안 함; 단, Member 객체에 USERNO, ENROLLDATE 등 null로 되어있을 것임
				// 강사님 설명 = 10개만 뽑아오면 그런 Member 생성자 안 만들었기 때문에 오류 발생? -> 나의 질문 = 위에서처럼 생성자 말고 setter 사용해서 뽑아오면, 안 뽑아온 컬럼은 null로 될 뿐, 오류 발생 안 하지 않을까?
				// 10h30 인터넷 접속 갑자기 끊겨서 10분 정도 설명 못 들음..

				list.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 생성된 순서의 역순으로 다 쓴 JDBC용 객체 반납
			close(rset);
			close(pstmt);
		}
		
		// 8) 결과 반환
		return list;
	} // selectAll() 종료
	
	// 2021.11.25(목) 11h 스스로 구현해보기 -> 14h 강사님과 함께 만들며 확인
	public Member selectByUserId(Connection conn, String userId) {
		// 0) 필요한 변수들 세팅
		// 조회된 회원에 대한 정보를 담을 Member 타입 변수 선언 + null로 초기화 -> 아무 것도 조회 안 되면 null 상태(?)의 Member 객체를 반환할 것임 
		Member m = null;
		
		// JDBC 관련 객체들 선언 -> 이렇게 변수 선언 시 객체 생성 안 해주는 이유는?(JDBC 수업 초반에 강사님께서 얼핏(?) 설명해주신 것 같은데, 정확히 기억 안 남)
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 미완성된 형태의, 실행할, SQL문
		// String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		String sql = prop.getProperty("selectByUserId");
		
		try {
			// 3a) SQL문을 인자로 넣어주며 PreparedStatement 객체 생성 
			pstmt = conn.prepareStatement(sql);
			// 3b) 미완성된 SQL문 완성시키기
			pstmt.setString(1, userId);
			// 4,5) SQL(SELECT)문 실행 + 결과
			rset = pstmt.executeQuery();
			
			// 6a) 현재 조회 결과가 담긴 ResultSet에서 1개 행이 존재한다면 뽑아서 VO 객체에 담음
			if (rset.next()) { // 1개의 행이 존재한다면
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 생성된 순서의 역순으로 JDBC용 객체들 (자원) 반납
			close(rset);
			close(pstmt);
		}
		
		// 8) 결과값 반환 = Member 객체 들고 이 메소드를 호출한 곳으로 돌아감
		return m;
	}
	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword) {
		// 0) 필요한 변수들 세팅
		// SELECT문 조회 결과 담을 list
		ArrayList<Member> list = new ArrayList<>();
		
		// JDBC 관련 객체들
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문
		// String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		String sql = prop.getProperty("selectByUserName");
		
		try {
			pstmt = conn.prepareStatement(sql); // Unhandled exception type SQLException; conn.prepareStatement(sql) 호출하면 PreparedStatement 값이 반환됨 -> 그 반환된 '값'을 변수 pstmt에 저장함
			pstmt.setString(1, "%" + keyword + "%" ); // Unhandled exception type SQLException
			
			// SQL문 실행 + 실행 결과 값 받아오기
			// pstmt.executeQuery() = 실행 + 결과 나옴 -> 이 결과값을 어딘가에 써야 하는 바, rset 변수에 담음 
			rset = pstmt.executeQuery(); // Unhandled exception type SQLException
			
			while (rset.next()) { // Unhandled exception type SQLException
				// 반복문 안에서 생성한, Member 형태를 가진 객체 m의 각 필드에 rset으로부터 컬럼별로 값을 받아와 담고, ArrayList인 list에 add() 메소드로 하나씩 쌓음
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	} // selectByUserNAme() 종료
	
	public int updateMember(Connection conn, Member m) {
		
		// String userId, String newPwd, String newEmail, String newPhone, String newAddress
		
		// 0) 필요한 변수들 세팅
		int result = 0;
		PreparedStatement pstmt = null;

		// 실행할 SQL문: UPDATE MEMBER SET USERPWD = 'xx', EMAIL = 'xx', PHONE = 'xx', ADDRESS = 'xx' WHERE USERID = 'xx';
		// String sql = "UPDATE MEMBER SET USERPWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USERID = ?"; // 강사님께서 알려주신 띄어쓰기 방식으로 고쳐쓰기 >.<
		String sql = prop.getProperty("updateMember");
		
		try {
			// 3a) PreparedStatement 객체 생성 + SQL문을 미리 넘겨줌
			pstmt = conn.prepareStatement(sql); // Unhandled exception type SQLException
			// JDBC 시험 = 서술형 8문제(DBMS/Oracle 필기/교재 내용) + 문제해결 시나리오 4문제(JDBC 순서(에 따라 무엇을 해줘야 하는지 등) 모두 이해하고 (Prepared)Statement codes 각각 5번씩 + Template 3번씩 직접 써봐야 함  
			// 문제해결 시나리오 문제 exemple = 윗줄을 conn.prepareStatement(sql);로 쓰면 무엇이 + 왜 작동이 안 되는가?
			// 문제점 = PreparedStatement 객체가 null이기 때문에 __에서/__ 때에(잘 모르겠음) null point exception 발생 
			// 사유 = 윗줄 코드에서 PreparedStatement 객체를 변수에 담지 않았기 때문에(맞게 듣고 이해한 것인가? 17h20 우리반영상 다시 보고 싶음)
			// 해결방법 = 윗줄 코드를 pstmt = conn.prepareStatement(sql);와 같이 수정
			
			// 3b) 미완성 상태의 SQL문 완성시키기
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			// 4) SQL문 실행 + 결과 나옴 -> 5) 관련 변수/객체에 결과 받기
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 
			close(pstmt);
		}
		
		// 8) 결과 반환
		return result;
	} // updateMember() 종료
	
	public int deleteMember(Connection conn, String userId) {
		// 0) 필요한 변수 먼저 세팅
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		// 실행할 SQL문: DELETE FROM MEMBER WHERE USERID = 'userId' 
		// String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		String sql = prop.getProperty("deleteMember");
		
		try {
			// 1) JDBC Driver 등록 + 2) Connection 객체 생성은 JDBCTemplate에서 한 것을 Service에서 받아 여기(Dao)에 conn으로 넘겨줌 
			// 3a) Connection 객체 이용해서 PreparedStatement 객체 생성 + 미완성된 SQL문을 담아서 날림
			pstmt = conn.prepareStatement(sql); // conn.prepareStatement(sql) 값을 사용하려면 어딘가(변수)에 담아줘야 함
			// 3b) 미완성 상태의 SQL문을 완성 형태로 채우기
			pstmt.setString(1, userId);
			// 4) pstmt.executeUpdate() = SQL문 실행 + 결과 나옴 -> 5) 결과값을 사용할 것이므로, 관련 변수에 담아줌
			result = pstmt.executeUpdate();
			// 6b) 트랜잭션 처리는 Service에서 할 것임
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 생성된 순서의 역순으로, 다 쓴 JDBC용 객체들의 자원 반납
			close(pstmt); // JDBCTempalte 클래스에서 내가 만든 close() 메소드에서 예외 처리를 해 주었기 때문에, 여기서 안 해도 됨
			// + 나머지 객체들 자원 반납은 Service에서 할 것임
		}
		
		// 8) 결과 반환
		return result;
	} // deleteMember() 종료
	
} // 클래스 영역 끝
