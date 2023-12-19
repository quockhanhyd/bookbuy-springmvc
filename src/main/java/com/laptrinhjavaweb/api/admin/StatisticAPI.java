package com.laptrinhjavaweb.api.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.laptrinhjavaweb.dto.BookDTO;
import com.laptrinhjavaweb.dto.request.StatisticDTO;
import com.laptrinhjavaweb.dto.response.ResponseMessage;
import com.laptrinhjavaweb.dto.response.StatisticBookDTO;
import com.laptrinhjavaweb.dto.response.StatisticResponseDTO;
import com.laptrinhjavaweb.dto.response.StatisticSalesDTO;
import com.laptrinhjavaweb.entity.BillEntity;
import com.laptrinhjavaweb.entity.BillInfoEntity;
import com.laptrinhjavaweb.entity.BookEntity;
import com.laptrinhjavaweb.mapper.BookMapper;
import com.laptrinhjavaweb.repository.BillInfoRepository;
import com.laptrinhjavaweb.repository.BillRepository;
import com.laptrinhjavaweb.repository.BookRepository;
import com.laptrinhjavaweb.service.IBookService;
import com.laptrinhjavaweb.utils.SecurityUtils;

@RestController(value = "statisticAPIOfAdmin")
@RequestMapping(value = {"/admin/api"})
public class StatisticAPI {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BillInfoRepository billInfoRepository;
	
	@Autowired
	private BillRepository billRepository;
	
	@SuppressWarnings("deprecation")
	@PostMapping(value = {"/statisticBook"})
	public ResponseEntity<?> statisticBook(@RequestBody StatisticDTO input) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(input.getToDate());
        calendar.add(Calendar.DATE, 1);
        input.setToDate(calendar.getTime());
        
		StatisticResponseDTO result = new StatisticResponseDTO();
		List<BillInfoEntity> ltBillInfo = billInfoRepository.findByCreateDateBetween(input.getFromDate(), input.getToDate());
		List<StatisticBookDTO> ltData1 = new ArrayList<>();
		
		for(BillInfoEntity item : ltBillInfo)
		{
			if (ltData1.stream().anyMatch(x -> x.getBookId() == item.getBookId())) {
				for (StatisticBookDTO s : ltData1) {
					if (s.getBookId() == item.getBookId()) {
						s.setNumberSold(s.getNumberSold() + item.getQuantity());
					}
				}
			}
			else {
				ltData1.add(new StatisticBookDTO(item.getBookId(), bookRepository.getOne(item.getBookId()).getName(), item.getQuantity(), 0));
			}
		}
		
		List<BillEntity> ltBill = billRepository.findByCreateDateBetweenOrderByCreateDate(input.getFromDate(), input.getToDate());
		List<StatisticSalesDTO> ltData2 = new ArrayList<>();
		if (input.getType() == 1) {
			for (BillEntity item : ltBill) {
				if (ltData2.stream().anyMatch(x -> item.getCreateDate().getDate() == x.getDate().getDate() && 
						item.getCreateDate().getMonth() == x.getDate().getMonth() &&
						item.getCreateDate().getYear() == x.getDate().getYear())) {
					for (StatisticSalesDTO s : ltData2) {
						if (item.getCreateDate().getDate() == s.getDate().getDate() && 
						item.getCreateDate().getMonth() == s.getDate().getMonth() &&
						item.getCreateDate().getYear() == s.getDate().getYear()) {
							s.setSale(s.getSale() + item.getTotalPrice());
						}
					}
				}
				else {
					ltData2.add(new StatisticSalesDTO(new Date(item.getCreateDate().getYear(), item.getCreateDate().getMonth(), item.getCreateDate().getDate()), item.getTotalPrice()));
				}
			}
		}
		
		
		result.setLtData1(ltData1);
		result.setLtData2(ltData2);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseMessage("OK", "Book has been created!", result)
			);
	}
	
}
