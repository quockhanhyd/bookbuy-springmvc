package com.laptrinhjavaweb.dto.response;

public class StatisticBookDTO {
	private Long bookId;
	private String name;
	private Long numberSold;
	private double percent;
	
	public StatisticBookDTO(Long bookId, String name, Long numberSold, double percent) {
		super();
		this.bookId = bookId;
		this.name = name;
		this.numberSold = numberSold;
		this.percent = percent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNumberSold() {
		return numberSold;
	}
	public void setNumberSold(Long numberSold) {
		this.numberSold = numberSold;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
}
