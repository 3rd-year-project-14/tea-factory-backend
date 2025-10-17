package com.teafactory.pureleaf.advance.entity;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "advance_request")
@Data
public class AdvanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advanceId;
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String rejectReason;

    public enum Status {
        Pending,
        Approved,
        Paid,
        Rejected
    }
    public enum PaymentMethod {
        Cash,
        BankTransfer,
        Cheque
    }

}
