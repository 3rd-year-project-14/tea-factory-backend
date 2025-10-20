package com.teafactory.pureleaf.fertilizer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fertilizer_request_item")
public class FertilizerRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fertilizer_request_id", nullable = false)
    private FertilizerRequest fertilizerRequest;

    @Column(nullable = false)
    private Long fertilizerStockId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FertilizerRequestItemStatus status = FertilizerRequestItemStatus.PENDING;

    @Column(length = 500)
    private String rejectReason;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FertilizerRequest getFertilizerRequest() { return fertilizerRequest; }
    public void setFertilizerRequest(FertilizerRequest fertilizerRequest) { this.fertilizerRequest = fertilizerRequest; }
    public Long getFertilizerStockId() { return fertilizerStockId; }
    public void setFertilizerStockId(Long fertilizerStockId) { this.fertilizerStockId = fertilizerStockId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public FertilizerRequestItemStatus getStatus() { return status; }
    public void setStatus(FertilizerRequestItemStatus status) { this.status = status; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}
