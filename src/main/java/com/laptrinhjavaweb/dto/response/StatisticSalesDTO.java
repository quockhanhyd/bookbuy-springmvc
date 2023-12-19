package com.laptrinhjavaweb.dto.response;

import java.util.Date;

public class StatisticSalesDTO {
	private Date date;
	private Long sale;
	public StatisticSalesDTO() {
		super();
	}
	public StatisticSalesDTO(Date date, Long sale) {
		super();
		this.date = date;
		this.sale = sale;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getSale() {
		return sale;
	}
	public void setSale(Long sale) {
		this.sale = sale;
	}
}
