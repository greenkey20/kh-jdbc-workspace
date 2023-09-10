// 2021.11.23(화)
package src.com.kh.view;

import src.com.kh.controller.MemberController;
import src.com.kh.model.vo.Member;

import java.util.ArrayList;
import java.util.Scanner;

// View: 사용자가 보게 될 시각적인 요소/화면; View단에서만 입력, 출력 -> 사용자로부터 자료 입력받아 Controller에 넘겨주고, Controller로부터 처리 결과 받아서 사용자에게 보여줌
public class MemberView {
	// View 안의 모든 메소드가/전역으로 다 쓸 수 있도록 Scanner 객체 생성
	private Scanner sc = new Scanner(System.in);
	
	// 전역으로 바로 MemberController 요청할 수 있도록 MemberController 객체 생성
	private MemberController mc = new MemberController();
	
	/**
	 * 사용자가 보게 될 첫 화면(메인화면)
	 */
	public void mainMenu() {
		// 메소드 안에 커서 두고 alt + shift + j -> 메소드 위에 주석 달림(내가 직접 라이브러리 만들 때 등 + 프로젝트 시 메소드가 많아지므로 주석 잘 다는 연습해두기) -> 이 메소드를 호출한 곳에서 메소드명 위에 마우스 올리면 이 주석이 표시됨
		
		// 종료를 선택하기 전까지 계속 돌 반복문(while문)
		while (true) {
			// 사용자에게 보여줄 화면을 syso로 출력
			System.out.println("***** 회원 관리 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 ID로 검색");
			System.out.println("4. 회원 이름 키워드로 검색"); // SQL문 깍두기 사용(LIKE %%)
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.print("이용할 메뉴 선택 > ");
			int menu = sc.nextInt();
			sc.nextLine(); // 1칸 Enter 치기
			
			switch (menu) {
			case 1 : insertMember();
					break;
			case 2 : selectAll();
					break;
			case 3 : selectByUserId();
					break;
			case 4 : selectByUserNameKeyword(); // coding conventions(개발자들끼리의 약속) 지키기 -> 협업 + 작업 일관성 등 e.g. 변수명은 소문자로 시작 + 클래스/인터페이스명은 대문자로 시작 -> 낙타봉(camel case) 표기법
					break;
			case 5 : updateMember();
					break;
			case 6 : deleteMember();
					break;
			case 0 : System.out.println("프로그램을 종료합니다.");
					return;
			default : System.out.println("번호를 잘못 입력했습니다."); // break 굳이 안 써도 됨
			} // switch문 영역 끝
			
		} // while문 영역 끝
		
	} // mainMenu() 종료

	/**
	 * 회원 추가용 화면 => 추가하고자 하는 회원의 정보를 입력받아서 추가 요청할 수 있는 화면
	 */
	public void insertMember() {
		System.out.println("----- 회원 추가 -----");
		System.out.print("ID > ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 > ");
		String userPwd = sc.nextLine();
		
		System.out.print("이름 > ");
		String userName = sc.nextLine();
		
		System.out.print("성별(M/F) > ");
		String gender = sc.nextLine().toUpperCase();
		
		System.out.print("나이 > ");
		int age = sc.nextInt();
		sc.nextLine();
		
		System.out.print("이메일 > ");
		String email = sc.nextLine();
		
		System.out.print("전화번호(숫자만) > ");
		String phone = sc.nextLine();
		
		System.out.print("주소 > ");
		String address = sc.nextLine();
		
		System.out.print("취미(,로 공백없이 나열) > "); // 데이터를 Split, Tokenizer 등으로 잘라서 쓸 수 있도록 ",로 공백없이 나열" 조건 붙여줌
		String hobby = sc.nextLine();
		
		// 입력받은 내용으로 Controller의 어떤 메소드를 호출해서 처리(회원 추가) 요청
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby); // 자동완성으로 메소드 만듦
		
	} // insertMember() 종료
	
	/**
	 * 회원 전체(ResultSet) 조회(요청 보내고, 그 결과를 보여주기)를 할 수 있는 화면
	 */
	public void selectAll() {
		System.out.println("----- 회원 전체 조회 -----");
		
		// 회원 전체 조회 요청을 Controller에 보냄
		mc.selectAll();
	} // selectAll() 종료
	
	/**
	 * 검색할 회원의 ID를 사용자로부터 입력받은 뒤 조회 요청 + 그 결과 보여주는 화면
	 */
	private void selectByUserId() {
		System.out.println("----- 회원 ID로 검색 -----");
		System.out.print("ID 입력 > ");
		String userId = sc.nextLine();
		
		// Controller에 회원 ID 검색 요청 시, 입력받은 ID를 같이 넘김
		mc.selectByUserId(userId);
	}
	
	// 2021.11.23(화) 숙제
	/**
	 * 검색할 회원의 이름의 키워드를 사용자로부터 입력받은 뒤 조회 요청 + 그 결과 보여주는 화면
	 */
	private void selectByUserNameKeyword() {
		// 사용자의 요청 = keyword를 입력할테니 이 keyword가 이름에 포함된 사람(의 정보) 찾아줘
		
		System.out.println("----- 회원 이름 키워드로 검색 -----");
		System.out.print("회원 이름 키워드 입력 > ");
		String userNameKeyword = sc.nextLine();
		
		// Controller에 회원 이름 키워드 검색 요청 시, 입력받은 이름 키워드를 같이 넘김
		// Controller 객체 mc를 부르고, 이 요청 처리해줄 관련 메소드 호출(); 메소드명 우측 괄호() 안 = 마술상자에 넣을 값/입력/매개변수 
		mc.selectByUserNameKeyword(userNameKeyword);
	} // selectByUserNameKeyword() 종료
	
	/**
	 * 정보 변경하고자 하는 회원의 ID 및 값 변경하고자 하는 컬럼명(들)을 입력받은 뒤 정보 변경 요청 + 그 결과 보여주는 화면 
	 * (강사님 version = 변경할 회원의 ID, 변경할 정보들(비밀번호, 이메일, 휴대폰번호, 주소)을 사용자에게 입력받은 후 변경 요청하는 메소드 
	 */
	public void updateMember() {
		
		// 내가 추가할 수 있는 점 = 같은 값이 있으면 넘어가고(?) 등 정교하게..
		
		// 내가 숙제로 쓴 것
		/*
		ArrayList<String> newColumnsList = new ArrayList<>(); // 변경하고자 하는 컬럼명(들)을 담을 list
		ArrayList<String> newValuesList = new ArrayList<>(); // 새로이 넣고자 하는 값(들)을 컬럼명 순서와 맞춰 담을 list
		
		System.out.println("----- 회원 정보 변경 -----");
		System.out.print("ID 입력 > "); // userID는 unique + not null인 컬럼이므로, userID을 이용해 내용 변경/수정할 대상 찾음/지정
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 입력 > ");
		String userPwd = sc.nextLine();
		
		boolean isLogIn = mc.logIn(userId, userPwd); // 로그인 개념처럼, 해당 회원에 대한 ID와 비밀번호를 알고 있을 때만 정보 변경 가능하도록 해봄
		
		if (!isLogIn) {
			System.out.println("회원 정보를 변경할 수 없습니다.");
		} else {
			System.out.print("변경하고자 하는 컬럼명(들)을 ,로 공백없이 나열 > ");
			String columns = sc.nextLine(); // 사용자로부터 입력받은, 변경하고자 하는 컬럼명(들)이 ,로 나열된, 문자열
			
			String[] list = columns.split(","); // 변경하고자 하는 컬럼명들을 문자형 배열 형태로 쪼갬
			
			for (int i = 0; i < list.length; i++) { // list 요소/사용자가 변경 희망하는 컬럼명들을 하나씩 확인
				if (list[i].equals("USERNO") || list[i].equals("USERID") || list[i].equals("ENROLLDATE")) { // userNo(sequence에 의해 생성되는 값), userId(unique 제약조건), enrolldate(default 제약조건)는 수정 불가능
					System.out.println(list[i] + " 컬럼의 값은 변경할 수 없습니다.");
				} else { // 기타 컬럼명들은 값 수정 가능
					System.out.print(list[i] + " 컬럼에 새로 넣을 값 입력 > "); // 사용자로부터 변경 후 값 입력받음
					String newValue = sc.nextLine();
					
					newColumnsList.add(list[i]); // 최종적으로 정보 변경할 컬럼명 리스트에 하나씩 쌓음/추가
					newValuesList.add(newValue); // 최종적으로 정보 변경할 내용/값 리스트에 하나씩 쌓음/추가
				}
				
			} // for문 영역 끝
			
			// 변경하고자 하는 userId, 컬럼명(들)과 그 컬럼(들)에 새로 넣을 값이 담긴 ArrayList 2개를 Controller에 전달해서 처리(해당 회원 정보 변경) 요청
			mc.updateMember(userId, newColumnsList, newValuesList);
			
		} // else문 영역 끝
		*/
		
		// 2021.11.24(수) 강사님 방식
		System.out.println("----- 회원 정보 변경 -----");
		
		// 변경할 회원의 ID
		System.out.print("변경할 회원의 ID > ");
		String userId = sc.nextLine();
		
		// 변경할 정보들
		System.out.print("새 비밀번호 > ");
		String newPwd = sc.nextLine();
		
		System.out.print("새 Email > ");
		String newEmail = sc.nextLine();
		
		System.out.print("새 휴대폰번호(숫자만) > ");
		String newPhone = sc.nextLine();
		
		System.out.print("새 주소 > ");
		String newAddress = sc.nextLine();
		
		// Controller에 회원 정보 수정 요청
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress); // updateMember() 오버로딩
		
	} // updateMember() 종료
	
	/**
	 * 탈퇴할 회원의 ID를 사용자로부터 입력받을 때 보여줄 화면; 탈퇴하고자 하는 회원의 ID 및 값 변경하고자 하는 컬럼명(들)을 입력받은 뒤 정보 변경 요청 + 그 결과 보여주는 화면
	 */
	public void deleteMember() {
		
		System.out.println("----- 회원 탈퇴 -----");
		System.out.print("탈퇴할 ID 입력 > "); // userID는 unique + not null인 컬럼이므로, userID을 이용해 탈퇴/삭제할 대상 찾음/지정
		String userId = sc.nextLine();
		
		// 내가 숙제하면서 쓴 방식
		/*
		System.out.print("비밀번호 입력 > ");
		String userPwd = sc.nextLine();
		
		// 로그인 개념처럼, 해당 회원에 대한 ID와 비밀번호를 알고 있을 때만 탈퇴 가능하도록 해봄
		if (mc.logIn(userId, userPwd)) {
			mc.deleteMember(userId);
		} else {
			System.out.println("로그인 정보가 일치하지 않아, 탈퇴할 수 없습니다.");
		}
		*/
		
		// 2021.11.24(수) 강사님 방식
		mc.deleteMember(userId);
		
	} // deleteMember() 종료
	
	// 2021.11.23(화) 수업 내용
	// 서비스 요청 처리 후 사용자가 보게 될 응답화면들 만들기
	
	/**
	 * 서비스 요청 성공 시 보게 될 화면
	 * @param message : 성공메시지
	 */
	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 : " + message);
	}
	
	/**
	 * 서비스 요청 실패 시 보게 될 화면
	 * @param message : 실패메시지
	 */
	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 : " + message);
	}
	
	/**
	 * 조회 서비스 요청 시, 조회 결과가 없을 때 사용자가 보게 될 화면
	 * @param message : 사용자에게 보여질 메시지
	 */
	public void displayNoData(String message) {
		System.out.println(message);
	}

	/**
	 * 조회 서비스 요청 시 여러 행이 조회된 결과를 받아서 보게 될 화면
	 * @param list : 여러 행이 조회된 결과
	 */
	public void displayList(ArrayList<Member> list) {
		System.out.println("\n조회된 데이터는 " + list.size() + "건입니다.\n");
		
		// list는 index 개념이 있으므로 for문 돌려 한 행 한 행 출력 가능
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i)); // list의 요소를 뽑아내는 get() 메소드
		}
		
	} // displayList() 종료
	
	/**
	 * 조회 서비스 요청 시 1개 행이 조회된 결과를 받아서 보게 될 화면
	 * @param m : 1행이 조회된 결과
	 */
	public void displayOne(Member m) { // 이 메소드 호출 시 Member 객체 하나 필요
		System.out.println("\n조회된 데이터는 다음과 같습니다.");
		System.out.println(m);
	}

} // 클래스 영역 끝
