package src.com.kh.run;

public class MVC {
	
	/* 1. MVC 패턴
	 * 패키지로 아래와 같이 구분
	 * Model: 데이터와 관련된 역할(데이터를 담는다거나[value object] DB에 접근해서 데이터 입출력[data access object])
	 * View: 사용자가 보게 될 시각적인 요소/화면(입력, 출력) 
	 * Controller: 사용자의 요청을 받아서 처리 후, 응답 화면을 지정하는 역할
	 * 
	 * View단에서만 출력문(System.out.println)을 사용
	 * Model의 DAO(data access object) 단에서만 DB에 직접적으로 접근한 후 해당 SQL문 실행 및 결과를 받음
	 * 
	 * 2021.11.23(화)
	 * 2. ojdbc6.jar 파일 추가
	 * 프로젝트 선택 후 마우스 오른쪽 클릭 -> Properties -> Java Build Path -> Libraries -> Add External JARS -> c:\dev\ojdbc6.jar 선택 -> Apply -> Apply and Close -> Referenced Libraries(탐색 창)에 ojdbc6 추가된 것 확인 가능
	 * ojbc6.jar 파일 추가하지 않으면 Class Not Found Exception 발생
	 * 
	 * 3. Statement = 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아주는 객체
	 * Connection 클래스의 createStatement() 메소드를 호출해서 생성 -> 실행 시 SQL문을 매개변수로 전달해서 질의 수행
	 * 
	 * 2021.11.24(수) 11h45 현재 학생들 주요 문제 = SQL 쿼리문 작성을 못함
	 * 회원 정보 추가 요청: INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, 컬럼2에 넣을 값, 컬럼3에 넣을 값, .. DEFAULT);
	 * 회원 전체 조회 요청: SELECT * FROM MEMBER;
	 * 회원 ID로 검색 요청: SELECT * FROM MEMBER WHERE USERID = 'xxxx';
	 * 회원 이름(keyword)으로 검색 요청: SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%';
	 * 회원 정보 변경(비밀번호, 이메일, 전화번호, 주소) 요청: UPDATE MEMBER SET USERPWD = 'xx', EMAIL = 'xx', PHONE = 'xx', ADDRESS = 'xx' WHERE USERID = 'xx';
	 * 회원 탈퇴 요청: DELETE FROM MEMBER WHERE USERID = 'xxxx';
	 * 
	 * source codes 작성 = 책 집필 -> 타인이 볼 거라고 생각하고 작성
	 * 잘 쓴 책 = 잘 읽힘; 가독성 높음
	 * 잘 쓴 코드 = 있어야 할 것이 있어야 할 곳에 있음 -> 가독성 향상 e.g. 코드 블럭 일관성 있게 맞추기, 띄어쓰기
	 */

}
