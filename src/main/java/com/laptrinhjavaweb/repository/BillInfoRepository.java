package com.laptrinhjavaweb.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.BillEntity;
import com.laptrinhjavaweb.entity.BillInfoEntity;

@Repository
public interface BillInfoRepository extends JpaRepository<BillInfoEntity, Long> {  
	List<BillInfoEntity> findByBill(BillEntity bill);
	List<BillInfoEntity> findByCreateDateBetween(Date fromDate, Date toDate);
}
