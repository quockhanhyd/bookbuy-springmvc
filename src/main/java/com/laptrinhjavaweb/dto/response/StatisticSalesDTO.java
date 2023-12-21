package com.laptrinhjavaweb.dto.response;

import java.util.Date;
import java.util.List;

public class StatisticSalesDTO {
	private Date date;
	private Long sale;
	private List<Long> ListBillID;
	private String BookName;
	public StatisticSalesDTO() {
		super();
	}
	
	public StatisticSalesDTO(Date date, Long sale, List<Long> listBillID, String bookName) {
		super();
		this.date = date;
		this.sale = sale;
		ListBillID = listBillID;
		BookName = bookName;
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

	public List<Long> getListBillID() {
		return ListBillID;
	}

	public void setListBillID(List<Long> listBillID) {
		ListBillID = listBillID;
	}

	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		BookName = bookName;
	}
}
