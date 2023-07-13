package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.DealerOrders;
import com.assignment.restapiassignment.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerOrdersRepository extends JpaRepository<DealerOrders, Long> {
    List<DealerOrders> findByDealerId(Long dealerId);
    DealerOrders findByInvoiceNo(String invoiceNo);

}

