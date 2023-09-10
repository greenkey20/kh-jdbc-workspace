// 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 강사님과 함께 확인/실습
package src.com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import src.com.kh.controller.MemberController;
import src.com.kh.model.vo.Member;

public class MemberView {

    // MemberView 클래스의 객체 생성 시 아래 필드 2개 만들어짐(?)
    private Scanner sc = new Scanner(System.in);
    private MemberController mc = new MemberController(); // MemberController 만들어서(new) 메모리에 올려두고 mc라는 변수에 담아둠

    public void mainMenu() {

        while (true) { // 조건식이 true인 동안 계속 반복
            System.out.println("***** 회원 관리 프로그램 *****");
            System.out.println("1. 회원 추가");
            System.out.println("2. 회원 전체 조회");
            System.out.println("3. 회원 ID로 검색");
            System.out.println("4. 회원 이름 키워드로 검색");
            System.out.println("5. 회원 정보 변경");
            System.out.println("6. 회원 탈퇴");
            System.out.println("0. 프로그램 종료");
            System.out.print("메뉴 입력 > ");
            int menu = sc.nextInt(); // 동등연산자 =의 우항의 값을 좌항의 특정 자료형의 변수에 대입해라
            sc.nextLine(); // 윗줄 nextInt()에 의한 개행 처리

            switch (menu) {
                case 1:
                    insertMember(); // 비교적 쉬움
                    break;
                case 2:
                    selectAll(); // 비교적 쉬움
                    break;
                case 3:
                    selectByUserId();
                    break;
                case 4:
                    selectByUserName(); // 비교적 어려움
                    break;
                case 5:
                    updateMember(); // 비교적 어려움
                    break;
                case 6:
                    deleteMember(); // 비교적 쉬움; 쉬운 것부터 만드는 것도 전략..
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
            } // switch문 영역 끝

        } // while문 영역 끝

    } // mainMenu() 종료

    /**
     * 회원 추가용 화면
     */
    private void insertMember() {
        System.out.println("---- 회원 추가 ----");
        System.out.print("ID > ");
        String userId = sc.nextLine();

        System.out.print("비밀번호 > ");
        String userPwd = sc.nextLine();

        System.out.print("이름 > ");
        String userName = sc.nextLine();

        System.out.print("성별(M/F) > ");
        String gender = String.valueOf(sc.nextLine().toUpperCase().charAt(0)); // String.valueOf() 메소드 기억 잘 안 남 -> 수업 자료 다시 읽어보자

        System.out.print("나이 > ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("이메일 > ");
        String email = sc.nextLine();

        System.out.print("휴대폰번호(숫자만) > ");
        String phone = sc.nextLine();

        System.out.print("주소 > ");
        String address = sc.nextLine();

        System.out.print("취미(,로 구분하되 띄어쓰기 없이 입력) > ");
        String hobby = sc.nextLine();

        mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);

    } // insertMember() 종료

    // 2021.11.24(수) 오후 수업 시 직접 만들어보기 -> 2021.11.25(목) 강사님과 함께 실습+설명

    /**
     * 회원 전체 조회(요청)를 할 수 있는 화면
     */
    private void selectAll() { // 9h~11h50
        // 이 메소드는 이 클래스 내부에서만 사용함 -> mainMenu()에서 자동완성으로 메소드 만들었더니 Eclipse가 private으로 만들어두었음 vs 수업시간에 강사님께서는 public으로 만드심
        // 나의 질문 = 강사님, view단 mainMenu() 메소드에서 만든 selectAll() 등 자동완성 했더니 이클립스가 private으로 만들어놨는데요 + 강사님께서도 내부에서만 사용한다고 설명해주셨는데, 그래도 public으로 만드는 것이 좋은 것인가요?
        System.out.println("---- 회원 전체 조회 ----");
        mc.selectAll();
    } // selectAll() 종료

    private void selectByUserId() {
        System.out.println("---- 회원 ID로 검색 ----");
        System.out.print("검색하고자 하는 ID 입력 > ");
        String userId = sc.nextLine();

        mc.selectByUserId(userId);
    } // selectByUserId() 종료

    private void selectByUserName() { // 나는 mainMenu()에서 자동완성해서 private으로 만듦 vs 수업시간에 강사님께서는 public으로 만드심
        // 사용자에게 보여주기
        System.out.println("---- 회원 이름 키워드로 검색 ----");
        System.out.print("검색하고자 하는 회원 이름 키워드 입력 > ");

        // 사용자로부터 입력받기
        // 사용자로부터 1줄(다음 개행 전까지) 입력받은 것
        String userNameKeyword = sc.nextLine();

        // 처리(이름에 keyword 포함하는 회원의 정보 조회) 요청은 Controller에게 보냄 = Controller의 관련 메소드 호출; 단, keyword도 함께 넘겨줘야 함
        mc.selectByUserName(userNameKeyword);
    } // selectByUserName() 종료

    private void updateMember() {
        System.out.println("---- 회원 정보 변경 ----");
        System.out.print("정보 변경하고자 하는 회원 ID > ");
        String userId = sc.nextLine();

        System.out.print("새로운 비밀번호 > ");
        String newPwd = sc.nextLine();

        System.out.print("새로운 이메일 > ");
        String newEmail = sc.nextLine();

        System.out.print("새로운 휴대폰번호(숫자만) > ");
        String newPhone = sc.nextLine();

        System.out.print("새로운 주소 > ");
        String newAddress = sc.nextLine();

        System.out.print("새로운 취미(,로 구분하되 띄어쓰기 없이 입력) > ");
        String newHobby = sc.nextLine();

        mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress, newHobby);
		
		/*
		while (true) {
			System.out.println("== 정보 변경 가능한 컬럼 ==");
			System.out.println("1. 비밀번호");
			System.out.println("2. 이메일");
			System.out.println("3. 전화번호");
			System.out.println("4. 주소");
			System.out.println("5. 취미");
			System.out.print("정보 변경하고자 하는 컬럼명 선택(여러 개 선택 시 ,로 구분하되 띄어쓰기 없게) > ");
			String menu = sc.nextLine();
			
			String[] columnsToUpdate = menu.split(",");
			String columnToUpdate = null;
			
			for (int i = 0; i < columnsToUpdate.length; i++) {
				
				switch (columnsToUpdate[i]) {
				case "1" : columnToUpdate = "USERPWD";
						break;
				case "2" : columnToUpdate = "EMAIL";
						break;
				case "3" : columnToUpdate = "PHONE";
						break;
				case "4" : columnToUpdate = "ADDRESS";
						break;
				case "5" : columnToUpdate = "HOBBY";
						break;
				}
			}
			
			switch (menu) {
			
			}
			
			System.out.print(columnToUpdate + "에 새로 입력할 정보 > ");
			String newValue = sc.nextLine();
			
			mc.updateMember(userId, columnToUpdate, newValue);
			
		} // while문 영역 끝
		*/

    } // updateMember() 종료

    private void deleteMember() {
        System.out.println("---- 회원 탈퇴 ----");

        System.out.print("탈퇴하려는 회원 ID > ");
        String userId = sc.nextLine();

        mc.deleteMember(userId);
    } // deleteMember() 종료

    public void displaySuccess(String message) {
        System.out.println("작업 처리 성공 : " + message);
    }

    public void displayFailure(String message) {
        System.out.println("작업 처리 실패 : " + message);
    }

    public void displayMultipleData(ArrayList<Member> list) { // 강사님의 메소드명 = displayList()
        System.out.println(list.size() + "건의 자료가 조회되었습니다."); // 강사님의 출력문 = "\n조회된 데이터는 " + list.size() + "건입니다.\n"
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)); // list.get(i) = list의 i번째 요소 뽑아냄
        }
    }

    public void displayOneData(Member m) {
        System.out.println(m);
    }

    public void displayNoData(String message) {
        System.out.println(message + " 조회 결과가 없습니다.");
    }

} // 클래스 영역 끝
