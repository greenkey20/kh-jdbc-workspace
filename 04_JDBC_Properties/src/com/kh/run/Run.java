// 2021.11.29(월) 9h
package src.com.kh.run;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import src.com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) {
		
		/* Java 마지막 시간 = Properties = Map 계열 컬렉션; key + value 세트로 담음; 주로 외부 설정 파일을 읽어오기, 또는 파일 형태로 출력하고자 할 때 사용
		 * store(), storeToXML() -> Properties, xml 파일로 내보내기
		 */
		
		/*
		Properties prop = new Properties(); // Properties 객체 하나 생성 -> java.util에 있는 것 import
		
		// setProperty() -> Properties 파일 내용 입력
		prop.setProperty("A", "B");	// key 'A' + value 'B'
		prop.setProperty("driver", "oracle.jdbc.driver.OracleDriver"); // 현재 사용하고 있는 Oracle driver 정보를 "driver"라는 key 값에 대응하는 value 값으로 입력
		prop.setProperty("url", "jdbc:oracle:thin:@localhost:1521:xe"); // jdbc하면서 사용하고 있는 url = getConnection()의 첫번째 인자 = version + url? + port번호..
		prop.setProperty("username", "JDBC");
		prop.setProperty("password", "JDBC");

		try {
			// FileOutputStream 객체 생성 시 생성자 매개변수로 경로 제시 vs 별도의 경로 제시가 없으면 프로젝트 폴더 내에 만들어짐
			// resources(폴더) = 주로 프로젝트 내의 외부파일들을 저장하는 역할
			prop.store(new FileOutputStream("resources/driver.properties"), "driver.properties"); // Properties 객체 생성한 뒤, setProperty()로 값을 넣고 나서, store() -> Properties 파일(외부 매체)에/로 set한 값들을 내보내기; 2개의 매개변수를 받음 = Writer(stream) + String(주석, comments)
			// Properties 파일 생성 시 위 keys/values 순서 엉망으로 들어감 = Map 계열 특징 -> 파일 열고, 내용 수정하고(내용 순서 수정, \ 삭제 등) 저장하면 반영됨 -> 프로그램 재실행하면 위 set한 것처럼 파일 내용 다시 써짐 -> 파일 한 번 생성(및 수정)한 뒤에는 파일 생성 코드 주석 처리함
			
			prop.storeToXML(new FileOutputStream("resources/query.xml"), "query.xml"); // storeToXML() -> XML 파일로 내보내기
			// XML 파일에서는, Properties 파일과 달리, 공백, 줄바꿔쓰기/개행 등 내가 쓰고 싶은대로 쓸 수 있음
			// XML 파일로 쿼리문 저장하면, 스트림 만들고 바깥으로부터 파일 불러와서 쓰는 속도(내가 느끼지는 못하겠지만, 훨씬 느림) < 메소드 내에 쿼리문 작성해서 쓰는 속도
		} catch (IOException e) { // "surround with try/multi-catch" 예외 처리 자동완성
			e.printStackTrace();
		} 
		*/
		
		new MemberView().mainMenu(); // MemberView 객체 만들어서 더 쓸 일 없으니까, 그냥 기본생성자로 이번 객체 만들고 그 객체의 mainMenu() 메소드에 접근함

	} // main() 종료

} // 클래스 영역 끝
