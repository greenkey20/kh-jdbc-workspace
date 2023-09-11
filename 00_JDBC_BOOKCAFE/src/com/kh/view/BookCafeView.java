package src.com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import src.com.kh.controller.BookCafeController;
import src.com.kh.model.vo.BookCafe;

public class BookCafeView {

    private Scanner sc = new Scanner(System.in);
    private BookCafeController bc = new BookCafeController();

    public void mainMenu() {
        while (true) {
            System.out.println("== 도서 관리 서비스 ==");
            System.out.println("1. 도서 전체 조회하기");
            System.out.println("2. 도서 추가하기");
            System.out.println("3. 도서명(도서명 키워드) 검색하기");
            System.out.println("4. 도서 정보 수정하기 (book_no로 조회하고 수정)");
            System.out.println("5. 도서 삭제하기");
            System.out.println("6. 도서 대여하기");
            System.out.println("7. 도서 반납하기");
            System.out.println("0. 프로그램 종료하기");
            System.out.println("-------------");
            System.out.print("메뉴를 골라주세요 => ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    selectAll();
                    break;
                case 2:
                    insertBook();
                    break;
                case 3:
                    selectByBookName();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    lendBook();
                    break;
                case 7:
                    receiveBook();
                    break;
                case 0:
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("메뉴를 다시 골라주세요");
            }

        }
    } // mainMenu() 종료

    public void selectAll() {

        System.out.println("*** 도서 전체 조회 ***");

        System.out.println("===========================================");

        bc.selectAll();

    }

    public void insertBook() {

        System.out.println("*** 도서 추가 ***");

        System.out.print("도서 번호 > ");
        int book_no = sc.nextInt();
        sc.nextLine();

        System.out.print("도서 제목 > ");
        String book_name = sc.nextLine();

        System.out.print("도서 저자 > ");
        String book_author = sc.nextLine();

        System.out.print("도서 테마 > ");
        String book_theme = sc.nextLine();

        System.out.print("도서 출판사 > ");
        String publisher = sc.nextLine();

        System.out.print("재고 > ");
        int stock = sc.nextInt();
        sc.nextLine();

        System.out.println("===========================================");

        bc.insertBook(book_no, book_name, book_author, book_theme, publisher, stock);
    }

    public void selectByBookName() {

        System.out.println("*** 도서 제목 키워드 검색 ***");

        System.out.print("도서 제목 키워드 입력 > ");
        String keyword = sc.nextLine();

        System.out.println("===========================================");

        bc.selectByBookName(keyword);
    }

    public void updateBook() {

        System.out.println("*** 도서 정보 수정 ***");

        System.out.print("수정할 도서 번호 > ");
        int book_no = sc.nextInt();
        sc.nextLine();

        System.out.print("도서 주제 수정 > ");
        String theme = sc.nextLine();

        System.out.print("도서 출판사 수정 > ");
        String publisher = sc.nextLine();

        System.out.print("책 권수 수정 > ");
        int stock = sc.nextInt();

        System.out.println("===========================================");

        bc.updateBook(book_no, theme, publisher, stock);

    }

    public void deleteBook() {

        System.out.println("*** 도서 삭제 ***");

        System.out.print("삭제할 도서 번호 > ");
        int book_no = sc.nextInt();
        sc.nextLine();

        System.out.println("============================================");

        bc.deleteBook(book_no);
    }

    private void lendBook() {
        System.out.println("*** 도서 대여 ***");
        System.out.print("도서 번호 입력 > ");
        int book_no = sc.nextInt();

        System.out.print("대여할 권 수 > ");
        int stock = sc.nextInt();
        sc.nextLine();

        System.out.println("===========================================");

        bc.lendBook(book_no, stock);
    }

    private void receiveBook() {

        System.out.println("*** 도서 반납 ***");

        System.out.print("도서 번호 > ");
        int book_no = sc.nextInt();

        System.out.println("반납할 권 수 > ");
        int stock = sc.nextInt();
        sc.nextLine();

        System.out.println("===========================================");

        bc.receiveBook(book_no, stock);

    }

    // --------------------------------------------------------------------
    public void displayNoData(String message) {

        System.out.println(message);

    }

    public void displayList(ArrayList<BookCafe> list) {

        System.out.println("\n 조회된 데이터는" + list.size() + "건 입니다. \n");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));

        }
    }

    public void displaySuccess(String message) {

        System.out.println("\n서비스 요청 성공! " + message);

    }

    public void displayFail(String message) {

        System.out.println("\n서비스 요청 실패! " + message);

    }

}
