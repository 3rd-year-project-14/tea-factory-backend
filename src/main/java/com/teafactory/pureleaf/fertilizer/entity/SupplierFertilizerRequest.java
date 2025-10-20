package com.teafactory.pureleaf.fertilizer.entity;

import com.teafactory.pureleaf.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "supplier_fertilizer_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierFertilizerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private User supplier;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(length = 500)
    private String note;

    @Column(length = 50)
    private String status;

    @OneToMany(mappedBy = "supplierFertilizerRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierFertilizerRequestItem> items;
}

