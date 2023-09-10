package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.BookCafeService;
import com.kh.model.vo.BookCafe;
import com.kh.view.BookCafeView;

public class BookCafeController {

	public void selectAll() {

		ArrayList<BookCafe> list = new BookCafeService().selectAll();

		if (list.isEmpty()) {
			new BookCafeView().displayNoData("조회된 데이터가 없습니다.");
		} else {
			new BookCafeView().displayList(list);
		}

	}

	public void insertBook(int book_no, String book_name, String book_author, String book_theme, String publisher,
			int stock) {

		BookCafe bc = new BookCafe(book_no, book_name, book_author, book_theme, publisher, stock);

		int result = new BookCafeService().insertProduct(bc);

		if (result > 0) {
			new BookCafeView().displaySuccess("도서 추가 성공");
		} else {
			new BookCafeView().displayFail("도서 추가 실패");
		}

	}

	public void selectByBookName(String keyword) {

		ArrayList<BookCafe> list = new BookCafeService().selectByBookName(keyword);

		if (list.isEmpty()) {
			new BookCafeView().displayNoData("조회 결과가 없습니다.");
		} else {
			new BookCafeView().displayList(list);
		}
	}

	public void updateBook(int book_no, String theme, String publisher, int stock) {

		BookCafe b = new BookCafe();
		b.setBook_no(book_no);
		b.setTheme(theme);
		b.setPublisher(publisher);
		b.setStock(stock);

		int result = new BookCafeService().updateBook(b);

		if (result > 0) {
			new BookCafeView().displaySuccess("정보 수정 성공!");
		} else {
			new BookCafeView().displayFail("정보 수정 실패!");
		}
	}

	public void deleteBook(int book_no) {

		int result = new BookCafeService().deleteBook(book_no);

		if (result > 0) {
			new BookCafeView().displaySuccess("삭제 성공!");
		} else {
			new BookCafeView().displayFail("삭제 실패!");
		}

	}

	public void lendBook(int book_no, int stock) {

		int result = new BookCafeService().lendBook(book_no, stock);

		if (result == 1) {
			System.out.println("정상적으로 대여 완료 했습니다.");
		} else if (result == 2) {
			System.out.println("대여할 수량이 보관 수량보다 많습니다.");
		} else if (result == 0) {
			System.out.println("대여에 실패했습니다.");
		}

	}

	public void receiveBook(int book_no, int stock) {

		int result = new BookCafeService().receiveBook(book_no, stock);

		if (result == 1) {
			System.out.println("정상적으로 반납되었습니다.");
		} else {
			System.out.println("반납이 정상적으로 이루어지지 않았습니다. 다시 시도하세요.");
		}

	}

}
