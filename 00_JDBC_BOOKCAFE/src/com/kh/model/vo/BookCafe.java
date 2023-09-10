package com.kh.model.vo;

public class BookCafe {

	private int book_no;
	private String book_name;
	private String book_author;
	private String theme;
	private String publisher;
	private int stock;

	public BookCafe() {
	}

	public BookCafe(int book_no, String book_name, String book_author, String theme, String publisher, int stock) {
		super();
		this.book_no = book_no;
		this.book_name = book_name;
		this.book_author = book_author;
		this.theme = theme;
		this.publisher = publisher;
		this.stock = stock;
	}

	public int getBook_no() {
		return book_no;
	}

	public void setBook_no(int book_no) {
		this.book_no = book_no;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getBook_author() {
		return book_author;
	}

	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "BookCafe [book_no=" + book_no + ", book_name=" + book_name + ", book_author=" + book_author + ", theme="
				+ theme + ", publisher=" + publisher + ", stock=" + stock + "]";
	}

}
