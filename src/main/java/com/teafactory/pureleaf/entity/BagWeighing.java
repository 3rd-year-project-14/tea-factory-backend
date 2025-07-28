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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_bag_id", referencedColumnName = "id")
    private TripBag tripBag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    private WeighingSession weighingSession;

    @Column(name = "gross_weight")
    private Double grossWeight;

    @Column(name = "tare_weight")
    private Double tareWeight;

    @Column(name = "net_weight")
    private Double netWeight;

    @Column(name = "wet")
    private Boolean wet;

    @Column(name = "coarse")
    private Boolean coarse;

    @Column(name = "other_weight")
    private Double otherWeight;

    @Column(name = "other_weight_reason")
    private String otherWeightReason;

    @Column(name = "type")
    private String type;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}
