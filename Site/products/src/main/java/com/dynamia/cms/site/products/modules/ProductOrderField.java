package com.dynamia.cms.site.products.modules;

public enum ProductOrderField {

	NAME("name"), PRICE("price"), VIEWS("views"), BRAND("brand.name");

	private String field;

	private ProductOrderField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
