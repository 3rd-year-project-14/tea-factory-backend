package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bag_weighing")
public class BagWeighing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weighing_id")
    private Long weighingId;

    @Column(name = "bag_id")
    private String bagId;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "trip_id")
    private WeighingSession session;

    @Column(name = "bag_weighing")
    private Double bagWeighing;

    @Column(name = "other_weight_reason")
    private String otherWeightReason;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @Column(name = "gross_weight")
    private Double grossWeight;

    @Column(name = "other_weight")
    private Double otherWeight;

    @Column(name = "wet")
    private Boolean wet;

    @Column(name = "tare_weight")
    private Double tareWeight;

    @Column(name = "net_weight")
    private Double netWeight;

    @Column(name = "coarse")
    private Double coarse;
}

