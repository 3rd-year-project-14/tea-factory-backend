package com.teafactory.pureleaf.inventoryProcess.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
public class BagWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private WeighingSession weighingSession;

    private double coarse;
    private double water;
    private LocalDate date;
    private double grossWeight;
    private double netWeight;
    private LocalDateTime recordedAt;
    private double tareWeight;
    private double otherWeight;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "supply_request_id", nullable = false)
    private TeaSupplyRequest supplyRequest;

    private int bagTotal;
}
