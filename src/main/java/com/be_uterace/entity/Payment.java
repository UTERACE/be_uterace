package com.be_uterace.entity;


import com.be_uterace.entity.enumeration.EPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    private Event event;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENT_PROVIDER_ID", referencedColumnName = "ID")
    private PaymentProvider paymentProvider;

    @Column(name = "created_On")
    @CreationTimestamp
    private ZonedDateTime createdOn;

    @Column(name = "last_ModifiedOn")
    @UpdateTimestamp
    private ZonedDateTime lastModifiedOn;

    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;


}
