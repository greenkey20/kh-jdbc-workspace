// 2021.11.23(화)
package src.com.kh.run;

import src.com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) { // main() 메소드; () 안의 문자열배열 args 실행
		
		// 프로그램 실행만들 담당; 사용자가 보게 될 첫 화면만 띄워주고 끝
		new MemberView().mainMenu(); // new 연산자 + MemberView 클래스의 (자동생성되어있는) 기본 생성자 + 메소드 호출

	}

}
