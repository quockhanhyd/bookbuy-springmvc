package com.laptrinhjavaweb.dto;

public class BookQuantityDTO {
	private Long id;
	private Long quantity;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BookQuantityDTO(Long id, Long quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
	}}
