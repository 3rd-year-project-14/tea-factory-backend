package com.teafactory.pureleaf.payment.entity;
import  com.teafactory.pureleaf.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tea_rate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeaRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teaRateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Refers to User.id
    private User user;

    private String month; // Format: YYYY-MM

    private BigDecimal nsa;

    private BigDecimal gsa;

    @Column(name = "monthly_rate_percentage")
    private BigDecimal monthlyRate;

    private BigDecimal totalWeight;

    private BigDecimal finalRatePerKg;

    private BigDecimal totalPayout;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime calculatedDate;

    public enum Status {
        PENDING,
        APPROVED
    }
}
