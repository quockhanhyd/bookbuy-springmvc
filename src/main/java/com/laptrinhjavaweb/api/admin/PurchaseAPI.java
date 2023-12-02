package com.laptrinhjavaweb.api.admin;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.dto.OrderDTO;
import com.laptrinhjavaweb.dto.response.ResponseMessage;
import com.laptrinhjavaweb.entity.BillEntity;
import com.laptrinhjavaweb.entity.BillInfoEntity;
import com.laptrinhjavaweb.entity.BookEntity;
import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.service.IBookService;
import com.laptrinhjavaweb.service.IPurchaseService;
import com.laptrinhjavaweb.utils.SecurityUtils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@RestController(value = "purchaseAPIOfAdmin")
@RequestMapping(value = {"/admin/api"})
public class PurchaseAPI {
	
	@Autowired
	private IPurchaseService purchaseService;
	
	@Autowired
	private IBookService bookService;
	
	@GetMapping(value = {"/list-purchases"})
	public ResponseEntity<?> getListPurchase() {
		if(SecurityUtils.isAuthentication()) {
			if(SecurityUtils.isADMIN()) {
				List<OrderDTO> result = purchaseService.getAllOrders();
				return result.isEmpty() ?
						ResponseEntity.ok(
								new ResponseMessage("OK", "Data orders is empty!", null)
							) :
						ResponseEntity.ok(
								new ResponseMessage("OK", "Data orders has been taken!", result)
							);
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseMessage("FAILED", "You are not logged in!", "not author")
			);
	}
	
	@PutMapping(value = {"/purchase"})
	public ResponseEntity<?> changeStatus(@RequestParam(required = false, defaultValue = "0", value = "id") Long id) {
		if(SecurityUtils.isAuthentication()) {
			if(SecurityUtils.isADMIN()) {
				return purchaseService.changeStatus(id) ? 
						ResponseEntity.status(HttpStatus.OK).body(
								new ResponseMessage("OK", "Order has been changed status!", "success")
							):
						ResponseEntity.status(HttpStatus.OK).body(
								new ResponseMessage("FAILED", "Order can't be changed status!", "can't")
							);
			}
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseMessage("FAILED", "You are not admin!", "not authen")
				);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseMessage("FAILED", "You are not logged in!", "not author")
			);
	}
	
	@DeleteMapping(value = {"/purchase"})
	public ResponseEntity<?> cancelPurchase(@RequestParam(required = false, defaultValue = "0", value = "id") Long id) {
		if(SecurityUtils.isAuthentication()) {
			if(SecurityUtils.isADMIN()) {
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseMessage("OK", "", purchaseService.cancelPurchase(id))
					);
			}
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseMessage("FAILED", "You are not admin!", "not authen")
				);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseMessage("FAILED", "You are not logged in!", "not author")
			);
	}
	
	@GetMapping("/generate-invoice")
	public void generateInvoice(HttpServletResponse response, @RequestParam Long id) throws DocumentException, IOException {
		response.setContentType("application/pdf");
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice_" + id + ".pdf";
        response.setHeader(headerKey, headerValue);
        
        BillEntity bill = purchaseService.getOne(id);
        CustomerEntity customer = purchaseService.getCustomerById(bill.getCustomerId());
         
        export(response, bill, customer);
	}
	
	private void writeTableHeader(PdfPTable table) {
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 5.0f, 2.0f, 1.5f, 2.0f});
        table.setSpacingBefore(10);
		
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
         
        cell.setPhrase(new Phrase(removeDiacritics("STT"), font13bw));
         
        table.addCell(cell);
         
        cell.setPhrase(new Phrase(removeDiacritics("Tên sách"), font13bw));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase(removeDiacritics("Giá bán"), font13bw));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase(removeDiacritics("Số lượng"), font13bw));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase(removeDiacritics("Tổng"), font13bw));
        table.addCell(cell);       
    }
     
    private void writeTableData(PdfPTable table, BillEntity bill) {
    	int stt = 1;
    	Long total = 0L;
        PdfPCell cell = new PdfPCell();
        
        for (BillInfoEntity billInfo : bill.getBillsInfo()) {
        	BookEntity book = bookService.findOne(billInfo.getBookId());
        	cell.setPhrase(new Phrase((stt++) + "", font13));
            table.addCell(cell);

        	cell.setPhrase(new Phrase(removeDiacritics(book.getName()), font13));
            table.addCell(cell);

        	cell.setPhrase(new Phrase(removeDiacritics(formatCurrency(billInfo.getPrice())), font13));
            table.addCell(cell);

        	cell.setPhrase(new Phrase(removeDiacritics(billInfo.getQuantity() + ""), font13));
            table.addCell(cell);

        	cell.setPhrase(new Phrase(removeDiacritics(formatCurrency(billInfo.getPrice() * billInfo.getQuantity())), font13));
            table.addCell(cell);
            
            total += billInfo.getPrice() * billInfo.getQuantity();
        }
        
        table.addCell("");
        cell.setPhrase(new Phrase(removeDiacritics("TỔNG"), font13));
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        cell.setPhrase(new Phrase(removeDiacritics(formatCurrency(total)), font13));
        table.addCell(cell);
    }
    
    // font
    Font font18b = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Color.BLUE);
    Font font18bi = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 18, Color.BLUE);
    Font font13b = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, Color.BLACK);
    Font font13bw = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, Color.WHITE);
    Font font13i = FontFactory.getFont(FontFactory.TIMES_ITALIC, 13, Color.BLACK);
    Font font13 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Color.BLACK);
    Font font10i = FontFactory.getFont(FontFactory.TIMES_ITALIC, 10, Color.BLACK);
     
    public void export(HttpServletResponse response, BillEntity bill, CustomerEntity customer) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Paragraph p1 = new Paragraph(removeDiacritics("CÔNG TY CỔ PHẦN SÁCH TRÀNG AN"), font13b);
        p1.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p2 = new Paragraph(removeDiacritics("Địa chỉ (Address): 54 Triều Khúc, Thanh Xuân, Hà Nội"), font13b);
        p2.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p3 = new Paragraph((removeDiacritics("Điện thoại (Phone): 01234556789")), font13b);
        p3.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p4 = new Paragraph(removeDiacritics("Mã số thuế (Tax code): 2 3 4 5 6 7 8 9"), font13b);
        p4.setAlignment(Paragraph.ALIGN_LEFT);
        
        Paragraph p5 = new Paragraph(removeDiacritics("HÓA ĐƠN BÁN HÀNG"), font18b);
        p5.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph p6 = new Paragraph(removeDiacritics("(SALE INVOICE)"), font18bi);
        p6.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph p7 = new Paragraph(removeDiacritics("Ngày/Date " + bill.getCreateDate().getDate() + " tháng/month " + bill.getCreateDate().getMonth() + " năm/year " + bill.getCreateDate().getYear()), font13i);
        p7.setAlignment(Paragraph.ALIGN_CENTER);
        
        Paragraph p8 = new Paragraph(removeDiacritics("Họ và tên người mua hàng (Customer name): " + customer.getFullName()), font13);
        p8.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p9 = new Paragraph(removeDiacritics("Số điện thoại (Customer Phone): " + customer.getPhone()), font13);
        p9.setAlignment(Paragraph.ALIGN_LEFT);
        String address = customer.getInfoExtra() + ", " + customer.getWard() + ", " + customer.getDistrict() + ", " + customer.getProvince();
        Paragraph p10 = new Paragraph(removeDiacritics("Địa chỉ (Customer address): " + address), font13);
        p10.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p11 = new Paragraph(removeDiacritics("Hình thức thanh toán (Payment type): Thanh toán khi nhận hàng"), font13);
        p11.setAlignment(Paragraph.ALIGN_LEFT);
         
        document.add(p1);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        document.add(p5);
        document.add(p6);
        document.add(p7);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        document.add(p8);
        document.add(p9);
        document.add(p10);
        document.add(p11);
        document.add(new Paragraph(" "));
         
        PdfPTable table = new PdfPTable(5);
        writeTableHeader(table);
        writeTableData(table, bill);
         
        document.add(table);
        
        Paragraph p12 = new Paragraph(removeDiacritics("Người mua hàng (Buyer)                                    Người bán hàng (Seller)"), font13b);
        p12.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph p13 = new Paragraph(removeDiacritics("Ký, ghi rõ họ tên (Signature & full name)                         Ký, đóng dấu, ghi rõ họ tên (Signature, stamp & full name)"), font10i);
        p13.setAlignment(Paragraph.ALIGN_CENTER);

        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(p12);
        document.add(p13);
         
        document.close();
         
    }
    
    public static String formatCurrency(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
        return decimalFormat.format(number).replace(',', '.');
    }
    
    public static String removeDiacritics(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }

}
