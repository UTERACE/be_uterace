package com.be_uterace.repository;

import com.be_uterace.entity.Area;
import com.be_uterace.entity.Organization;
import com.be_uterace.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {

//    @Query("SELECT p FROM Payment p WHERE p.vnpTxnRef = :vnpTxnRef AND p.paymentProvider.id =: payment_provider_id")
//    Payment findByvnpTxnRefAndpayment_provider_id(@Param("vnpTxnRef") UUID vnpTxnRef, @Param("payment_provider_id") Integer payment_provider_id);

    @Query(value = "SELECT * FROM Payment  WHERE Payment.vnp_txn_ref = :vnpTxnRef",nativeQuery = true)
    Payment findByvnpTxnRef(@Param("vnpTxnRef") String vnpTxnRef);

    @Query("SELECT p FROM Payment p WHERE p.requestId = :request_id")
    Payment findByRequestIdMomo(@Param("request_id") String request_id);

    Payment findByVnpTxnRef(String vnpTxnRef);
}
