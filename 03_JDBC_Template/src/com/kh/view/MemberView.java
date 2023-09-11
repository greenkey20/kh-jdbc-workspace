// 2021.11.25(목) 16h
package src.com.kh.view;

import src.com.kh.model.vo.Member;
import src.com.kh.controller.MemberController;

import java.util.ArrayList;
import java.util.Scanner;

public class MemberView {

	// MemberView 객체를 생성하면 Scanner 객체와 MemberController 객체가 생성됨
	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();

	public void mainMenu() {
		while (true) { // 프로그램이 종료될 때까지 무한 반복
			// 사용자에게 보여줄 화면
			System.out.println("-- 회원 관리 --");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5. 회원 수정");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.println("-------------");
			System.out.print("메뉴 입력 > ");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1:
				insertMember();
				break;
			case 2:
				selectAll();
				break;
			case 3:
				selectByUserId();
				break;
			case 4:
				selectByUserName();
				break;
			case 5:
				updateMember();
				break;
			case 6:
				deleteMember();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public void insertMember() {

		System.out.println("---- 회원 추가 ----");

		// 입력
		System.out.print("아이디 > ");
		String userId = sc.nextLine();

		System.out.print("비밀번호 > ");
		String userPwd = sc.nextLine();

		System.out.print("이름 > ");
		String userName = sc.nextLine();

		System.out.print("성별(M/F) > ");
		String gender = String.valueOf(sc.nextLine().toUpperCase().charAt(0));

		System.out.print("나이 > ");
		int age = sc.nextInt();
		sc.nextLine();

		System.out.print("이메일 > ");
		String email = sc.nextLine();

		System.out.print("휴대폰번호(숫자만) > ");
		String phone = sc.nextLine();

		System.out.print("주소 > ");
		String address = sc.nextLine();

		System.out.print("취미 > ");
		String hobby = sc.nextLine();

		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);
	}

	/**
	 * 회원 전체 조회를 할 수 있는 화면
	 */
	public void selectAll() {

		System.out.println("--- 회원 전체 조회 ---");

		mc.selectAll();

	}

	public void selectByUserId() {

		System.out.println("--- 회원 아이디 검색 ---");

		System.out.print("아이디 입력 > ");
		String userId = sc.nextLine();

		mc.selectByUserId(userId);

	}

	public void selectByUserName() {

		System.out.println("--- 회원 이름 키워드 검색 ---");

		System.out.print("키워드 입력 > ");
		String keyword = sc.nextLine();

		mc.selectByUserName(keyword);

	}

	public void updateMember() {

		System.out.println(" --- 회원 정보 변경 ---");

		// 변경할 회원의 아이디
		System.out.print("변경할 회원의 아이디 > ");
		String userId = sc.nextLine();

		// 변경할 정보들
		System.out.print("변경할 비밀번호 > ");
		String newPwd = sc.nextLine();

		System.out.print("변경할 이메일 > ");
		String newEmail = sc.nextLine();

		System.out.print("변경할 휴대폰번호(숫자만) > ");
		String newPhone = sc.nextLine();

		System.out.print("변경할 주소 > ");
		String newAddress = sc.nextLine();

		// 회원 정보 수정 요청
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress);

	}

	public void deleteMember() {

		System.out.println("---- 회원 탈퇴 ----");

		System.out.print("탈퇴할 회원 ID > ");
		String userId = sc.nextLine();

		mc.deleteMember(userId);
	}

	// -----------------------------------------------------------

	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 " + message);
	}

	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 " + message);
	}

	public void displayNodata(String message) {
		System.out.println(message);
	}

	public void displayList(ArrayList<Member> list) {
		System.out.println("\n 조회된 데이터는" + list.size() + "건 입니다. \n");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	public void displayOne(Member m) {
		System.out.println(m);
	}

}