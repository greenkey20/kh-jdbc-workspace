package src.com.kh.controller;

import java.util.ArrayList;

import src.com.kh.model.dao.ProductDao;
import src.com.kh.model.service.ProductService;
import src.com.kh.model.vo.Product;
import src.com.kh.view.ProductView;
import src.com.kh.model.service.ProductService;

public class ProductController {

	public void selectAll() {
		
		ArrayList<Product> pList = new ProductService().selectAll();
		
		if (pList.isEmpty()) {
			new ProductView().displayNodata("상품이 등록되어 있지 않습니다. 등록해 주세요.");
		} else {
			new ProductView().displayList(pList);
		}
	} // selectAll() 종료

	public void insertProduct(String product_id, String product_name, int price, String description, int stock) {
		
		Product p = new Product();
		p.setProduct_id(product_id);
		p.setProduct_name(product_name);
		p.setPrice(price);
		p.setDescription(description);
		p.setStock(stock);
		
		int result = new ProductService().insertProduct(p);

		if (result > 0) {
			new ProductView().displaySuccess("상품 등록 성공");
		} else {
			new ProductView().displayFail("상품 등록 실패");
		}
	} // insertProduct() 종료

	public void selectByProductName(String keyword) {
		
		ArrayList<Product> pList = new ProductService().selectByProductName(keyword);

		if (pList.isEmpty()) {
			new ProductView().displayNodata("검색된 상품이 없습니다.");
		} else {
			new ProductView().displayList(pList);
		}

	} // selectByProductName() 종료

	public void updateProduct(String product_id, int newPrice, String newDescription, int newStock) {

		Product p = new Product();
		p.setProduct_id(product_id);
		p.setPrice(newPrice);
		p.setDescription(newDescription);
		p.setStock(newStock);

		int result = new ProductService().updateProduct(p);

		if (result > 0) {
			new ProductView().displaySuccess("상품 정보가 정상적으로 수정되었습니다.");
		} else {
			new ProductView().displayFail("상품 수정이 실패 했습니다.");
		}

	} // updateProduct() 종료

	public void deleteProduct(String product_id) {

		int result = new ProductService().deleteProduct(product_id);

		if (result > 0) {
			new ProductView().displaySuccess("상품이 정상적으로 삭제 되었습니다.");
		} else {
			new ProductView().displayFail("상품ID가 정상적이지 않습니다. 다시 삭제 요청하세요.");
		}
	} // deleteProduct() 종료

}
