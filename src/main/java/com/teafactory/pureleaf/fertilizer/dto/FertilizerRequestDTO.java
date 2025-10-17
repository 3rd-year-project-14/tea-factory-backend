package com.teafactory.pureleaf.fertilizer.dto;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerRequestDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long companyId;
    private String companyName;
    private Long userId;
    private String userName;
    private Integer quantity;
    private String note;
    private FertilizerRequestStatus status;
    private String rejectReason;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
