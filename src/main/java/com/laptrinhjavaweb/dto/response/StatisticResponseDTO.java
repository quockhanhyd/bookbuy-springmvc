package com.laptrinhjavaweb.dto.response;

import java.util.List;

public class StatisticResponseDTO {
	private List<StatisticBookDTO> ltData1;
	private List<StatisticSalesDTO> ltData2;
	public List<StatisticBookDTO> getLtData1() {
		return ltData1;
	}
	public void setLtData1(List<StatisticBookDTO> ltData1) {
		this.ltData1 = ltData1;
	}
	public List<StatisticSalesDTO> getLtData2() {
		return ltData2;
	}
	public void setLtData2(List<StatisticSalesDTO> ltData2) {
		this.ltData2 = ltData2;
	}
	public StatisticResponseDTO(List<StatisticBookDTO> ltData1, List<StatisticSalesDTO> ltData2) {
		super();
		this.ltData1 = ltData1;
		this.ltData2 = ltData2;
	}
	public StatisticResponseDTO() {
		super();
	}
}
