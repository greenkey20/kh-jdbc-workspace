package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.ProductController;
import com.kh.model.vo.Product;

public class ProductView {

	private Scanner sc = new Scanner(System.in);
	private ProductController pc = new ProductController();

	public void meinMenu() {

		while (true) {
			System.out.println("==== 제품 관리 프로그램 ====");
			System.out.println("1. 상품 전체 조회하기");
			System.out.println("2. 상품 추가하기");
			System.out.println("3. 상품명(키워드로) 검색하기");
			System.out.println("4. 상품 정보 수정하기 (상품 id로 조회하고 수정)");
			System.out.println("5. 상품 삭제하기 (상품 id로 조회해서 삭제)");
			System.out.println("0. 프로그램 종료");
			System.out.println("-------------");
			System.out.print("메뉴 입력 > ");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1:
				selectAll();
				break;
			case 2:
				insertProduct();
				break;
			case 3:
				selectByProductName();
				break;
			case 4:
				updateProduct();
				break;
			case 5:
				deleteProduct();
				break;
			case 0:
				System.out.println("끝! 프로그램을 종료합니다");
				return;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요");
			}
		}
	}

	public void selectAll() {
		System.out.println("---- 상품 전체 조회 ----");
		pc.selectAll();
	}

	public void insertProduct() {
		System.out.println("---- 상품 추가 ----");
		System.out.print("상품 ID > ");
		String productId = sc.nextLine();
		System.out.print("상품명 > ");
		String productName = sc.nextLine();
		System.out.print("상품 가격 > ");
		int price = sc.nextInt();
		sc.nextLine();
		System.out.print("상품 상세 정보 > ");
		String description = sc.nextLine();
		System.out.print("재고 > ");
		int stock = sc.nextInt();
		sc.nextLine();
		pc.insertProduct(productId, productName, price, description, stock);
	}

	public void selectByProductName() {
		System.out.println("---- 상품명(키워드) 검색 ----");
		System.out.print("상품명 키워드 > ");
		String keyword = sc.nextLine();
		pc.selectByProductName(keyword);

	}

	public void updateProduct() {
		System.out.println("---- 상품 정보 수정 하기 ----");
		System.out.print("수정할 상품 ID > ");
		String product_id = sc.nextLine();
		System.out.print("새로운 상품 가격 > ");
		int newPrice = sc.nextInt();
		sc.nextLine();
		System.out.print("새로운 상품 상세 정보 > ");
		String newDescription = sc.nextLine();
		System.out.print("새로운 재고 > ");
		int newStock = sc.nextInt();
		sc.nextLine();

		pc.updateProduct(product_id, newPrice, newDescription, newStock);
	}

	public void deleteProduct() {
		System.out.println("---- 상품 삭제 -----");
		System.out.print("삭제할 상품 ID > ");
		String product_id = sc.nextLine();
		pc.deleteProduct(product_id);

	}

	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 : " + message);
	}

	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 : " + message);
	}

	public void displayList(ArrayList<Product> list) {
		System.out.println("서비스 요청 성공! " + list.size() + "건이 조회되었습니다.");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i));
		}
	}

	public void displayNodata(String message) {
		System.out.println(message);
	}

}
