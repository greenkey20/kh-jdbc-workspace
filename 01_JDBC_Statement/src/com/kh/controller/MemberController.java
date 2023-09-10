// 2021.11.23(화)
package src.com.kh.controller;

import java.util.ArrayList;

import src.com.kh.model.dao.MemberDao;
import src.com.kh.model.vo.Member;
import src.com.kh.view.MemberView;

/* Controller = View를 통해서 요청한 기능을 처리하는 역할/담당
 * 전달된 데이터를 해당 메소드에서 가공/처리한 후, DAO 메소드 호출 시 전달 -> DAO로부터 반환받은 결과에 따라 (View 메소드 호출해서)사용자가 보게 될 View(응답 화면)를 결정
 * (위 문장 DAO 부분 정확히 이해가 안 됨)
 * 
 * 코드 반복은 이번 주 후반쯤부터 한 곳에 모을 예정임
 */

public class MemberController {

	/**
	 * 사용자의 회원 추가 요청을 처리해주는 메소드
	 * @param userId
	 * @param userPwd
	 * @param userName
	 * @param gender
	 * @param age
	 * @param email
	 * @param phone
	 * @param address
	 * @param hobby
	 * => 사용자가 요청 시 입력했던 값들
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby) {
		
		// View로부터 전달된 데이터들을 Member 객체에 담기
		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		// DAO 클래스에서 회원 추가 처리 결과를 받을 정수형 변수
		int result = new MemberDao().insertMember(m);
		
		if (result > 0) { // 성공했을 경우
			new MemberView().displaySuccess("회원 추가/가입 성공"); // MemberView 자료형 객체를 만들어둔 것이 없으므로, new 키워드로 생성하고 display~ 메소드 접근; new를 쓰지 않고 MemberView 자료형/클래스를 사용하고 싶다면, MemberView 자료형/클래스를 static으로 만들어주면 됨
		} else { // 실패했을 경우
			new MemberView().displayFail("회원 추가/가입 실패");
		}
		
	} // insertMember() 종료

	/**
	 * 사용자의 회원 전체 조회 요청을 처리해주는 메소드
	 */
	public void selectAll() {
		// SELECT * FROM MEMBER;
		
		// 결과값을 담을 변수: SELECT 반환형 = ResultSet -> 한 행 한 행은 ArrayList에 담아 VO 객체로 다룸; 단, ArrayList에는 Member타입 자료형만 들어갈 수 있도록 ArrayList<Member>(제네릭이 Member인 ArrayList)
		// 선언 방법1)
		// ArrayList<Member> list = new ArrayList<>();
		// list = new MemberDao().selectAll();
		// 선언 방법2) 위 2줄을 1줄로 작성 가능
		ArrayList<Member> list = new MemberDao().selectAll();
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게 될 View 화면 지정
		if (list.isEmpty()) { // 텅 빈 list = 조회 결과가 없음
			new MemberView().displayNoData("전체 조회 결과가 없습니다.");
		} else { // list에 요소가 있음 = 조회 결과가 있음
			new MemberView().displayList(list);
		}
		
	} // selectAll() 종료

	/**
	 * 사용자의 회원 ID 검색 요청을 처리해주는 메소드
	 * @param userId : 사용자가 입력한, 검색할, 회원 ID
	 */
	public void selectByUserId(String userId) {
		// SELECT문 실행 -> userId 컬럼에는 unique 제약조건이 걸려있는 바, 특정 userId를 검색하면 조회 결과(ResultSet)가 1행임 -> Member 객체로 View에 반환함
		Member m = new MemberDao().selectByUserId(userId);
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게 될 View 화면 지정
		if (m == null) { // 조회 결과가 없는 경우
			new MemberView().displayNoData(userId + "에 대한 검색 결과가 없습니다.");
		} else { // 조회 결과가 있는 경우
			new MemberView().displayOne(m);
		}
		
	} // selectByUserId() 종료

	// 2021.11.23(화) 숙제
	/**
	 * 사용자의 회원 이름 키워드 검색 요청을 처리해주는 메소드
	 * @param userNameKeyword : 사용자가 입력한, 검색할, 회원 이름 키워드
	 */
	public void selectByUserNameKeyword(String userNameKeyword) {
		// 키워드를 회원명에 가진 사람이 다수일 수 있음 -> SELECT 반환형 = ResultSet -> 검색 결과의 한 행 한 행은 ArrayList에 담아 VO 객체로 다룸; 단, ArrayList에는 Member타입 자료형만 들어갈 수 있도록 ArrayList<Member>(제네릭이 Member인 ArrayList)
		ArrayList<Member> list = new MemberDao().selectByUserNameKeyword(userNameKeyword);
		// 자동완성 -> 실수할 일 없음 e.g. View에서 받은 keyword를 Controller -> Dao 넘기기 등
		
		// 조회 결과가 있을지 없을지 모름 -> 각 경우에 맞게 사용자에게 보여줄(View) 내용을 조건문을 통해 결정 
		if (list.isEmpty()) { // Dao 받아온 list가 비어있는 경우 = 검색 결과가 없는 경우
			new MemberView().displayNoData("\"" + userNameKeyword + "\"을/를 포함하는 회원 이름은 없습니다."); // 강사님 문구 = keyword + "에 대한 검색 결과가 없습니다."
		} else {
			new MemberView().displayList(list);
		}
		
	} // selectByUserNameKeyword() 종료

	/**
	 * 사용자가 입력한 ID와 비밀번호가 DB에 저장된 것과 일치하는지 확인해서 로그인 요청을 처리해주는 메소드
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	public boolean logIn(String userId, String userPwd) {
		
		boolean isLogIn = false; // 로그인 성공/실패 여부를 판별/전달할 논리형 변수
		
		String realUserPwd = new MemberDao().logIn(userId);
		
		if (realUserPwd == null || !realUserPwd.equals(userPwd)) { // realUserPwd가 null값으로 전달된 경우를 제일 먼저 비교연산해야 null값 오류가 안 나는 것을 확인함
			isLogIn = false;
			new MemberView().displayFail("로그인 실패");
		} else {
			isLogIn = true;
			new MemberView().displaySuccess("로그인 성공");
		}
		
		return isLogIn;
	} // logIn() 종료
	
	/**
	 * 사용자의 회원 정보 변경 요청을 처리해주는 메소드
	 * @param newColumnsList : 사용자로부터 입력받은 + 변경 가능한 컬럼(들)의 목록
	 * @param newValuesList : 사용자로부터 입력받은, 새로운, 컬럼값(들)의 목록
	 */
	public void updateMember(String userId, ArrayList<String> newColumnsList, ArrayList<String> newValuesList) {
		
		// DAO 클래스에서 회원 추가 처리 결과를 받을 정수형 변수
		int result = new MemberDao().updateMember(userId, newColumnsList, newValuesList);
		
		if (result > 0) {
			new MemberView().displaySuccess("회원 정보 변경 성공");
		} else {
			new MemberView().displayFail("회원 정보 변경 실패");
		}
		
	} // updateMember() 종료
	
	// 2021.11.24(수) 강사님 방식: 기본 logic -> 기능 추가, 더 멋지게 보이게 하는 것 등은 개인 역량에 따라 추가..
	// 내가 어제 만든 updateMember() 메소드 오버로딩
	/**
	 * 사용자의 회원 정보 수정 요청 시 처리해주는 메소드
	 * @param userId : 변경하고자 하는 회원의 ID; 식별값
	 * @param newPwd
	 * @param newEmail
	 * @param newPhone
	 * @param newAddress : new~ 변경할 정보들
	 */
	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress) {
		
		// View에서 전달받은 데이터를 Dao에 전달하기 위해 '가공 처리'하는 방법은 여러 가지 = 배열, list, 문자열 여러 개 그대로 등
		// 단, 매개변수가 다수이므로, 강사님께서는 Member 객체 만들어 넘기심; VO 객체에 입력받은 값들 담기
		Member m = new Member();
		
		m.setUserId(userId);
		// 추가 가능한 option = 아래 컬럼 중 일부만 선택해서 변경하도록 등 
		m.setUserPwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		
		int result = new MemberDao().updateMember(m); // 위와 같이 값 초기화한 Member 객체 m을 매개변수로 Dao 클래스에 넘겨줌; 실행하고자 하는 SQL문 = update -> 반환형 = 처리된 행 수(int자료형)
		
		// Dao에서 반환받은 결과에 따라 사용자에게 어떤 화면 보여줄지(View) 지정
		if (result > 0) { // 성공
			new MemberView().displaySuccess("회원 정보 변경 성공");
		} else { // 실패
			new MemberView().displayFail("회원 정보 변경 실패");
		}
		
	} // updateMember() 종료

	/**
	 * 사용자가 회원 탈퇴 요청 시 처리해주는 메소드
	 * @param userId : 사용자로부터 입력받은, 탈퇴하고자 하는, 회원 ID
	 */
	public void deleteMember(String userId) {
		
		// DML 구문 실행 결과 = int
		int result = new MemberDao().deleteMember(userId);
		
		// 결과에 따른 화면 지정
		if (result > 0) { // 성공
			new MemberView().displaySuccess("회원 탈퇴 성공");
		} else { // 실패
			new MemberView().displayFail("회원 탈퇴 실패");
		}
		
	} // deleteMember() 종료
	
	/* 2021.11.23(화) 숙제 회고
	 * 고민했던 점: 회원 정보 수정 기능 구현 시, 어떤 컬럼들이 수정 (불)가능한가, 몇 개의 컬럼들을 수정하게끔 할 것인가 등을 생각해서 정했다 -> 다수의 컬럼을 수정하기로 했을 때 sql문을 Statement을 통해 어떻게 반복해서 전달할지 생각이 필요했다.
	 * 아쉬운 점: 내가 현재 쓴 코드 상으로는 회원 정보 수정 시 성별을 M, F로만 정확히 써야 할 수 밖에 없다; m, f 등 소문자로 쓰면 (프로그램은 계속 동작하지만) 자료 수정 반영은 안 된다. 
	 * 개인적으로 구현해보고 싶어서 로그인 비슷한 것을 만들어보려고 했고, 의도한대로 기능은 하지만, '회원 관리 프로그램' 취지/의도에는 적합하지 않은 것 같다.
	 */

} // 클래스 영역 끝
