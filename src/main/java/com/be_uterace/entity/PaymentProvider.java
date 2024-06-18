package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT_PROVIDER")
public class PaymentProvider {
    @Id
    private Integer id;
    private boolean isEnabled;
    private String name;
    private String configureUrl;
    private String landingViewComponentName;
    private String additionalSettings;

}
