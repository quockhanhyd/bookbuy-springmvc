package com.laptrinhjavaweb.api.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
			// chỉ thống kê những đơn hàng ở trạng thái Đã giao và Hoàn thành
			if (item.getBill().getStatus() == 3 || item.getBill().getStatus() == 4) {
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
		}
		
		List<BillEntity> ltBill = billRepository.findByCreateDateBetweenOrderByCreateDate(input.getFromDate(), input.getToDate());
		List<StatisticSalesDTO> ltData2 = new ArrayList<>();
		if (input.getType() == 1) {
			for (BillEntity item : ltBill) {
				// chỉ thống kê những đơn hàng ở trạng thái Đã giao và Hoàn thành
				if (item.getStatus() == 3 || item.getStatus() == 4) {
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
						ltData2.add(new StatisticSalesDTO(new Date(item.getCreateDate().getYear(), item.getCreateDate().getMonth(), item.getCreateDate().getDate()), item.getTotalPrice(), null, null));
					}
				}
			}
		}
		
		
		result.setLtData1(ltData1);
		result.setLtData2(ltData2);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseMessage("OK", "Book has been created!", result)
			);
	}
	
	@PostMapping("/exportExcel")
    public void exportToExcel(HttpServletResponse response, @RequestBody StatisticDTO input) throws IOException {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(input.getToDate());
        calendar.add(Calendar.DATE, 1);
        input.setToDate(calendar.getTime());
        
        response.setContentType("application/octet-stream");
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=thong_ke_doanh_thu_" + dateFormat.format(input.getFromDate()) + " - " + dateFormat.format(input.getToDate()) + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        export(response, input);    
    } 
 
	
	private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private void writeHeaderLine(StatisticDTO input) {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFamily(FontFamily.ROMAN);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        
        createCell(row, 3, "CỬA HÀNG SÁCH TRÀNG AN", style);

        row = sheet.createRow(1);
        createCell(row, 3, "BÁO CÁO DOANH THU " + dateFormat.format(input.getFromDate()) + " đến " + dateFormat.format(input.getToDate()), style);
         

        row = sheet.createRow(3);
        createCell(row, 0, "STT", style);      
        createCell(row, 1, "Ngày", style);       
        createCell(row, 2, "Số đơn hàng", style);    
        createCell(row, 3, "Sản phẩm đã bán", style);
        createCell(row, 4, "Tổng thu ngày", style);
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    @SuppressWarnings("deprecation")
	private void writeDataLines(StatisticDTO input) {
        int rowCount = 4;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFamily(FontFamily.ROMAN);
        font.setFontHeight(14);
        style.setFont(font);
        
        List<BillEntity> ltBill = billRepository.findByCreateDateBetweenOrderByCreateDate(input.getFromDate(), input.getToDate());
		List<StatisticSalesDTO> ltData2 = new ArrayList<>();
		if (input.getType() == 1) {
			for (BillEntity item : ltBill) {
				// chỉ thống kê những đơn hàng ở trạng thái Đã giao và Hoàn thành
				if (item.getStatus() == 3 || item.getStatus() == 4) {
					if (ltData2.stream().anyMatch(x -> item.getCreateDate().getDate() == x.getDate().getDate() && 
							item.getCreateDate().getMonth() == x.getDate().getMonth() &&
							item.getCreateDate().getYear() == x.getDate().getYear())) {
						for (StatisticSalesDTO s : ltData2) {
							if (item.getCreateDate().getDate() == s.getDate().getDate() && 
							item.getCreateDate().getMonth() == s.getDate().getMonth() &&
							item.getCreateDate().getYear() == s.getDate().getYear()) {
								s.setSale(s.getSale() + item.getTotalPrice());
								s.getListBillID().add(item.getId());
								
								StringBuilder bookNameBuilder = new StringBuilder();
								item.getBillsInfo().forEach(x -> {
								    BookEntity book = bookRepository.getOne(x.getBookId());
								    bookNameBuilder.append(book.getName())
								                   .append(": ")
								                   .append(PurchaseAPI.formatCurrency(x.getPrice()))
								                   .append(" x ")
								                   .append(x.getQuantity())
								                   .append(" \n");
								});

								String bookName = bookNameBuilder.length() >= 2 ? 
					                    bookNameBuilder.substring(0, bookNameBuilder.length() - 2) : 
					                        bookNameBuilder.toString();
								s.setBookName(s.getBookName() + bookName);
							}
						}
					}
					else {
						StringBuilder bookNameBuilder = new StringBuilder();
						item.getBillsInfo().forEach(x -> {
						    BookEntity book = bookRepository.getOne(x.getBookId());
						    bookNameBuilder.append(book.getName())
						                   .append(": ")
						                   .append(PurchaseAPI.formatCurrency(x.getPrice()))
						                   .append(" x ")
						                   .append(x.getQuantity())
						                   .append("\n");
						});

						String bookName = bookNameBuilder.length() >= 2 ? 
			                    bookNameBuilder.substring(0, bookNameBuilder.length() - 2) : 
			                        bookNameBuilder.toString();
						ltData2.add(new StatisticSalesDTO(new Date(item.getCreateDate().getYear(), item.getCreateDate().getMonth(), item.getCreateDate().getDate()), item.getTotalPrice(), Arrays.asList(item.getId()), bookName));
					}
				}
			}
		}
             
		int index = 1;
        for (StatisticSalesDTO item : ltData2) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, index++, style);
            createCell(row, columnCount++, dateFormat.format(item.getDate()), style);
            createCell(row, columnCount++, item.getListBillID().size(), style);
            createCell(row, columnCount++, item.getBookName(), style);
            createCell(row, columnCount++, PurchaseAPI.formatCurrency(item.getSale()), style);
             
        }
    }
     
    public void export(HttpServletResponse response, StatisticDTO input) throws IOException {
        writeHeaderLine(input);
        writeDataLines(input);
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
	
}
