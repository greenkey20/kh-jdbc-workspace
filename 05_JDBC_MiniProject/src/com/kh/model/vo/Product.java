package com.kh.model.vo;

public class Product {

	private String product_id;
	private String product_name;
	private int price;
	private String description;
	private int stock;

	public Product() {

	}

	public Product(String product_id, String product_name, int price, String description, int stock) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
		this.price = price;
		this.description = description;
		this.stock = stock;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", product_name=" + product_name + ", price=" + price
				+ ", description=" + description + ", stock=" + stock + "]\n";
	}

}
