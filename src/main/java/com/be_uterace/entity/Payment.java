//package com.be_uterace.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.sql.Timestamp;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "PAYMENT")
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "PAYMENT_ID")
//    private Long paymentId;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
//    private Event event;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "PAYMENT_ID", referencedColumnName = "PAYMENT_ID")
//    private PaymentMethod paymentMethod;
//
//    @CreationTimestamp
//    @Column(name = "CREATED_AT")
//    private Timestamp createdAt;
//
//    @Column(name = "TRANSACTION_ID")
//    private Long transactionId;
//
//    @Column(name = "STATUS")
//    private boolean status;
//
//
//}
