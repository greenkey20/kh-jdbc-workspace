package src.com.kh.controller;

import src.com.kh.model.dao.ProductDao;
import src.com.kh.model.vo.Product;
import src.com.kh.view.ProductView;

import java.util.ArrayList;

public class ProductController {

	public void selectAll() {

		ArrayList<Product> list = new ProductDao().selectAll();

		if (list.isEmpty()) {
			new ProductView().displayFail("조회된 결과가 없습니다.");
		} else {
			new ProductView().displayList(list);
		}

	}

	public void selectByProductName(String keyword) {

		ArrayList<Product> list = new ProductDao().selectByProductName(keyword);

		if (list.isEmpty()) {
			new ProductView().displayNodata("조회된 결과가 없습니다.");
		} else {
			new ProductView().displayList(list);
		}

	}

	public void insertProduct(String product_id, String product_name, int price, String description, int stock) {
		Product p = new Product(product_id, product_name, price, description, stock);

		// 2. Dao의 insertMember 메소드 호출
		int result = new ProductDao().insertProduct(p);

		// 3. 결과값에 따라서 사용자가 보게 될 화면 지정
		if (result > 0) { // 성공했을 경우
			// 성공메시지를 띄워주는 화면 호출
			new ProductView().displaySuccess("상품 추가 성공");
		} else { // 실패했을 경우
			// 실패메시지를 띄워주는 화면 호출
			new ProductView().displayFail("상품 추가 실패");
		}
	}

	public void updateProduct(String product_id, int newPrice, String newDescription, int newStock) {

		Product p = new Product();

		p.setProduct_id(product_id);
		p.setPrice(newPrice);
		p.setDescription(newDescription);
		p.setStock(newStock);

		int result = new ProductDao().updateProduct(p);

		if (result > 0) {
			new ProductView().displaySuccess("상품 정보 수정 성공");
		} else {
			new ProductView().displayFail("상품 정보 수정 실패");
		}

	} // updateProduct() 종료

	public void deleteProduct(String product_id) {

		int result = 0;
		result = new ProductDao().deleteProduct(product_id);

		if (result > 0) {
			System.out.println("상품이 정상적으로 삭제 되었습니다.");
		} else {
			System.out.println("상품ID가 정상적이지 않습니다. 다시 삭제 요청하세요.");
		}
	}

}
