package com.teafactory.pureleaf.loanProcess.entity;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    private Long reqId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "months", nullable = false)
    private Integer months;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "type", nullable = false)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20)")
    private Status status = Status.PENDING;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}