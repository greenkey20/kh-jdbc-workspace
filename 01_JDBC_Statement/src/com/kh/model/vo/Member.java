// 2021.11.23(화) 9h40
package src.com.kh.model.vo;

import java.sql.Date;

/* VO(Value Object) = DB 테이블의 1행에 대한 데이터를 기록할 수 있는 저장용 객체
 * 유사 용어(이렇게도 불림) = DTO(Data Transfer Object), DO(Domain Object), Entity, bean..
 * 
 * VO 조건
 * 1. 반드시 캡슐화 적용
 * 2. 기본 생성자 및 매개변수 생성자 작성
 * 3. 모든 필드에 대한 setter 및 getter 메소드 작성
 */

/* (2021.11.22(월)에 만든) MEMBER 테이블과 같은 컬럼들과 유사하게 필드 구성
 * SEQUENCE와 DEFAULT 등 조건으로 값이 들어가는 경우는 해당 필드들을 뺀 생성자를 추가해줌
 * 
 * 어제 만든 'c:\dev\수업용 샘플 스크립트.sql' 파일을 메모장으로 열어 내용 확인해보기
 */

public class Member {
	
	// [필드부]
	// 필드는 DB 컬럼 정보와 유사하게 작업
	private int userNo; // USERNO NUMBER
	private String userId; // USERID VARCHAR2(15 BYTES)
	private String userPwd; // USERPWD VARCHAR2(20 BYTES)
	private String userName; // USERNAME VARCHAR2(20 BYTES) NOT NULL
    private String gender; // GENDER CHAR(1 BYTE) CHECK(GENDER IN ('M', 'F')); 기본자료형 char 쓰면 추후 자료형 변환 등 번거로움 -> 가능하다면 기본자료형 대신 String 및 객체 사용이 더 편리한 바, 권장
    private int age; // AGE NUMBER
    private String email; // EMAIL VARCHAR2(30 bytes)
    private String phone; // PHONE CHAR(11 bytes)
    private String address; // ADDRESS VARCHAR2(100 bytes)
    private String hobby; // HOBBY VARCHAR2(50 bytes)
    private Date enrollDate; // ENROLLDATE DATE DEFAULT SYSDATE NOT NULL <- import java.sql.Date
    
    // [생성자부]
    // 기본 생성자
	public Member() {
		super();
	}

	// 모든 필드를 매개변수로 받는 생성자
	public Member(int userNo, String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby, Date enrollDate) {
		super();
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
		this.enrollDate = enrollDate;
	}

	// SEQUENCE와 DEFAULT 등 조건으로 값이 들어가는 경우 필드들 제외한, 회원 추가용 생성자
	public Member(String userId, String userPwd, String userName, String gender, int age, String email, String phone,
			String address, String hobby) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
	}
	
	// [메소드부]
	// getter, setter
	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", gender=" + gender + ", age=" + age + ", email=" + email + ", phone=" + phone + ", address="
				+ address + ", hobby=" + hobby + ", enrollDate=" + enrollDate + "]";
	}

}
