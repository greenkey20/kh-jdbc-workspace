// 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 강사님과 함께 확인/실습
package src.com.kh.controller;

import java.util.ArrayList;

import src.com.kh.model.dao.MemberDao;
import src.com.kh.model.vo.Member;
import src.com.kh.view.MemberView;

public class MemberController {

	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby) {
		
		// 1. View로부터 전달받은 값들/데이터 하나하나 넘기기 번거로우니 '데이터 가공 처리' -> 이 값들만 받는 Member 클래스 생성자를 만들었는 바, 전달받은 데이터를 Member 객체에 담음
		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		// 2. Dao의 insertMember() 메소드 호출 -> (Prepared)Statement에서 dml 구문 처리 후 반환하는 처리 결과 = row의 수 = int형 자료
		int result = new MemberDao().insertMember(m);
		
		// 3. 결과 값에 따라 사용자가 보게 될 화면 지정
		if (result > 0) { // 성공했을 경우
			// 성공 메시지를 띄워주는 화면 호출
			new MemberView().displaySuccess("회원 추가 성공");
		} else { // 실패했을 경우
			// 실패 메시지를 띄워주는 화면 호출
			new MemberView().displayFailure("회원 추가 실패");
		}
		
	} // insertMember() 종료
	
	// 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 2021.11.25(목) 강사님과 함께 실습+설명
	/**
	 * 회원 전체 요청 처리해주는 메소드
	 */
	public void selectAll() {
		
		// SELECT문의 결과로 나오는 DB의 테이블 형태나 JDBC가 반환해주는 ResultSet Java에서 보기 쉽지 않음 -> 무엇이든/이것저것 담을 수 있는 '마법의 리스트' ArrayList에 담아서 받을 것임
		// 이 메소드 내에서 MemberDao 객체 더 이상 쓸 일 없는 바, 굳이 변수 만들지는 않고, 그냥 생성만 해서(new 연산자 + 기본생성자 MemberDao() -> heap 영역에 생김) 1번 사용
		// list라는 이름의 ArrayList를 스택 영역에 만듦 + new MemberDao().selectAll() 호출해서 실행시킨 뒤 ArrayList<Member> 형태를 반환받음 -> 그것을 ArrayList<Member> 타입의 변수 list에 대입시킴
		ArrayList<Member> list = new MemberDao().selectAll();
		
		// 조회 결과 있는지 없는지 판단(조건문 if 사용) 후, 사용자가 보게 될 화면(이런 기능을 하는 메소드는 모두 View에 있음) 지정
		if (list.isEmpty()) {
			new MemberView().displayNoData("전체"); // list가 비어있는 경우 보여줄 View 화면
		} else { // list가 비어있지 않으면
			// MemberView 클래스의 객체 만들고, 그 객체의 displayMultipleData() 메소드 호출 + 매개변수로 list 전달
			new MemberView().displayMultipleData(list);
		}
		
	} // selectAll() 종료

	public void selectByUserId(String userId) {
		// userId는 unique 값인 바, 조회 결과는 1행 -> Member 객체에 조회 결과 담아오고자 함
		Member m = new MemberDao().selectByUserId(userId);
		
		if (m == null) {
			new MemberView().displayNoData(userId);
		} else {
			new MemberView().displayOneData(m);
		}
		
	} // selectByUserId() 종료

	public void selectByUserName(String userNameKeyword) {
		
		// DB에서 SELECT문 실행 -> DB로부터 ResultSet 반환됨 -> 조회 결과는 여러 행일 수 있으므로, ArrayList에 담고자 함
		
		// DB와 직접 쿵짝쿵짝하는 Dao에 처리(이름에 keyword 포함하는 회원의 정보 조회) 요청 -> 검색 결과가 담긴 list를 반환 받음 -> 이 리터럴을 ArrayList<Member> 자료형의 변수에 넣어줌
		ArrayList<Member> list = new MemberDao().selectByUserName(userNameKeyword);
		
		if (list.isEmpty()) {
			new MemberView().displayNoData("회원 이름 키워드");
		} else {
			new MemberView().displayMultipleData(list);
		}
		
	} // selectByUserName() 종료

	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress,
			String newHobby) {
		
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(newPwd);
		// 나의 생각 = userName 컬럼에도 NOT NULL 제약조건 있지만, 이렇게 Dao에 전달할 용도의 Member 객체 생성 시 userName 필드 값 안 넣어줘도 되네.. 왜 그런걸까 생각해보자..
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		m.setHobby(newHobby);
		
		int result = new MemberDao().updateMember(m);
		
		if (result > 0) {
			new MemberView().displaySuccess("회원 정보 변경 성공");
		} else {
			new MemberView().displayFailure("회원 정보 변경 실패");
		}
		
	} // updateMember() 종료

	public void deleteMember(String userId) {
		
		// DB에 직접 접근할 수 있는 Dao에게 처리 요청 -> 전역이나 Controller 클래스에 Dao 없으니까 여기서 객체 하나 만들어야 함; new MemberDao()
		// Dao 객체에 넘길 자료가 몇 개 안 될 때는 아래와 같이 그냥 넘겨도 됨 vs 다수일 경우 Member 객체에 묶어서 등 나의 판단에 따라 '가공처리'하면 됨
		// new MemberDao().deleteMember(userId)가 정수 가지고 돌아옴 -> int형 변수 result에 담음
		int result = new MemberDao().deleteMember(userId);
		
		if (result > 0) { // 회원 탈퇴 성공
			new MemberView().displaySuccess("회원 탈퇴 성공"); // 사용자에게 보여줄 화면은 View에 있음; 단, View 객체는 이 클래스에 없는 바, 여기서 한 번 만들어서 쓰고, 관련 메소드(인자로 String 리터럴 넣어주어야 함) 호출
		} else { // 회원 탈퇴 실패
			new MemberView().displayFailure("회원 탈퇴 실패");
		}
		
	} // deleteMember() 종료

} // 클래스 영역 끝
