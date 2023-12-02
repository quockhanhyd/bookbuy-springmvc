package com.laptrinhjavaweb.service;

import java.util.List;

import com.laptrinhjavaweb.dto.OrderDTO;
import com.laptrinhjavaweb.dto.PurchaseDTO;
import com.laptrinhjavaweb.entity.BillEntity;
import com.laptrinhjavaweb.entity.CustomerEntity;

public interface IPurchaseService {
	Long order(OrderDTO order);
	PurchaseDTO getListPurchase(Long id);
	String cancelPurchase(Long id);
	List<OrderDTO> getAllOrders();
	boolean changeStatus(Long id);
	BillEntity getOne(Long id);
	CustomerEntity getCustomerById(Long id);
}
