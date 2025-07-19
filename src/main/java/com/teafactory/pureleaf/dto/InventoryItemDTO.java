package com.teafactory.pureleaf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryItemDTO {
    private Long id;
    private String item;
    private int quantity;
    private String unit;
    private LocalDateTime lastUpdated;
}
