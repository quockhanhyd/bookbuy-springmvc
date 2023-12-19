package com.laptrinhjavaweb.dto.request;

import java.util.Date;

public class StatisticDTO {
	private Date fromDate;
	private Date toDate;
	private int Type;
	public StatisticDTO() {
		super();
	}
	
	public StatisticDTO(Date fromDate, Date toDate, int type) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		Type = type;
	}

	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}
	
}
