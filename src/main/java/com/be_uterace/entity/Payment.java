package com.be_uterace.entity;


import com.be_uterace.entity.enumeration.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne
    @JoinColumn(name = "SHIRT_SIZE_ID", referencedColumnName = "SIZE_ID")
    private ShirtSize shirtSize;

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

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;


}
