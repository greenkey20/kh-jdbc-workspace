// 2021.11.25(목) 16h
package src.com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

// JDBCTemplate 클래스에 있는 모든 메소드(*)를 static으로 쓰겠다; static 메소드들을 import함
import static src.com.kh.common.JDBCTemplate.*;
// 접근제한자 -> 패키지 내/외부 접근 관련 e.g. JDBCTemplate 클래스 및 그 안의 메소드들이 static이기 때문에(x) 클래스 및 메소드들의 접근제한자가 public으로 되어있기 때문에(o) 다른 클래스에서 사용할 수 있음
// static -> 프로그램 '실행' 시작하면 메모리의 static 영역에 올라가서 프로그램 실행 중에 계속 있음 + 프로그램 종료되면 사라짐 -> 객체 생성(heap 메모리 영역에 올림) 없이 사용 가능
import src.com.kh.model.dao.MemberDao;
import src.com.kh.model.vo.Member;

/* Service(서비스) 클래스 : 기존의 Dao 클래스의 역할 분담 -> Controller와 Dao 사이의 역할 -> Controller에서 Service 호출(Connection 객체 생성) 후, Service를 거쳐서 Dao로 넘어감; '연결'하고 Connection 관련 역할 담당
 * Dao 호출 시 Connection 객체 + 기존에 Controller에서 Dao로 넘기고자 했던 매개변수를 같이 넘겨줌 -> DML구문의 경우, Dao 처리가 끝나면 Dao 결과에 따른 트랜잭션 처리도 Service단에서 같이 해줌
 * Service단을 추가함으로써 Dao에는 순수하게 SQL문을 처리하는 부분만 남음 
 */

public class MemberService {

	public int insertMember(Member m) {
		// 1) JDBC Driver 등록 + Connection 객체 생성
		Connection conn = /*JDBCTemplate.*/getConnection(); // static 메소드이기 때문에/메모리 영역이 다르기 때문에 JDBCTemplate.getConnection(); 이렇게 호출하면 됨/일반 객체 생성처럼 new 연산자로 heap 메모리 영역에 올릴 필요 없음
		// + JDBCTemplate 쓰기 싫으니/번거로우니 import static~ * -> JDBCTemplate 클래스의 메소드명 앞에 JDBCTemplate 쓸 필요 없음  

		// 2) Dao 호출 시 Connection 객체 + 기존에 Controller에서 Dao로 넘기고자 했던/Controller에서 Service로 넘겨받은 값을 매개변수로 같이 넘김
		int result = new MemberDao().insertMember(conn, m);
		// MemberDao() 객체가 이 클래스 어디에도 없으니 객체 생성해서 사용/접근해야 함; 단, 이 메소드에서는 이렇게 insertMember 호출할 때 1번만 쓰면 되니까 굳이 변수 만들어서 대입하지 않음

		// 6b) Dao 처리 결과에 따른 트랜잭션 처리
		if (result > 0) {
			/*JDBCTemplate.*/commit(conn);
		} else {
			/*JDBCTemplate.*/rollback(conn);
		}

		// 7) Connection 객체 반납
		/*JDBCTemplate.*/close(conn);

		return result;
	} // insertMember() 종료

	// 2021.11.25(목) 9h
	public ArrayList<Member> selectAll() {
		// 1) JDBC Driver 등록 + Connection 객체 생성
		Connection conn = getConnection();

		// 2) Controller에서 넘겨받은 것 없으므로, Connection 객체만 Dao에 넘겨줌 -> 결과값을 받을 ArrayList 타입 변수 list 만듦 -> Dao 호출해서 return(SELECT문 처리 결과인 ResultSet의 내용물을 Member 자료형으로 한 행 한 행 쌓은 list) 받기
		ArrayList<Member> list = new MemberDao().selectAll(conn);
		// (배열의) 얕은 복사 -> 대입연산자 우측의 값 list(메소드를 호출해서 받은 '값')와 좌측의 변수 list가 같은 곳을 가리키도록 함(강사님께서 메모리/변수? 등이 화살표 가리키는 그림 그리며 설명해 주셨는데, 제대로 못 봄 ㅠ.ㅠ; 스스로 그려보려 했으나 못하겠음.. 메모리 영역 등 정확히 이해 못한 듯..)

		// 3) Connection 객체는 여기서 역할을 다 했기 때문에 close
		close(conn);

		// 4) 결과값 return
		return list;
	} // selectAll() 종료
	
	// logic이 들어가는 메소드는 presentation(사용자에게/화면에 보여지는 것)이 없는 것이 좋음 vs View단 메소드들처럼 사용자들에게 뭔가 보여주는 역할을 하는 경우 메소드의 반환형은 보통 void; 값을 반환x, print 구문을 통해 보여주기만 하면 됨

	// 2021.11.25(목) 11h 스스로 구현해보기 -> 14h 강사님과 함께 만들며 확인
	public Member selectByUserId(String userId) {
		// 1) Connection 객체 생성
		Connection conn = getConnection();

		// 2) Connection 객체와 Controller로부터 전달받은 userId를 함께 Dao에 전달 -> Dao로부터 Member 객체를 돌려받고자 함
		Member m = new MemberDao().selectByUserId(conn, userId);

		close(conn);

		return m;
	}

	public ArrayList<Member> selectByUserName(String keyword) {
		// 1)
		Connection conn = getConnection();

		// 2)
		ArrayList<Member> list = new MemberDao().selectByUserName(conn, keyword);

		// 3) Dao로부터 list를 받아온 바, Connection의 역할을 다 함 -> 보통 객체 만든 곳에서 자원 반납함
		close(conn);

		// 4) 결과값 반환
		return list;
	} // selectByUserName() 종료

	public int updateMember(Member m) {
		// 1) Connection 객체 생성
		Connection conn = getConnection();

		// 2) 결과를 담을 변수 생성 -> 관련 Dao 메소드 호출해서 결과값 받기; 단, Connection 객체와 기존에 넘겨야 할 매개변수 함께 넘김
		int result = new MemberDao().updateMember(conn, m);

		// 3) 결과값에 따라 commit 또는 rollback 실행
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}

		close(conn);

		return result;
	} // updateMember() 종료

	public int deleteMember(String userId) {
		// 1) JDBC Driver 등록 + Connection 객체 생성
		Connection conn = getConnection();

		// 2) 위에서 생성한 Connection 객체와 Controller로부터 전달받은 userId를 Dao에 넘겨주며 delete문 실행 요청 -> int형의 자료를 반환 받음 -> 이 값을 사용하기 위해 값을 int형 변수에 담아줌  
		int result = new MemberDao().deleteMember(conn, userId);

		// 6b) 트랜잭션 처리
		if (result > 0) { // 탈퇴/삭제 성공
			commit(conn);
		} else { // 탈퇴/삭제 실패
			rollback(conn);
		}

		close(conn);

		return result;
	} // deleteMember() 종료

} // 클래스 영역 끝