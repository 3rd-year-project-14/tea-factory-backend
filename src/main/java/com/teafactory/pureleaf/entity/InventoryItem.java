//package com.teafactory.pureleaf.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//public class InventoryItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String item;
//    private int quantity;
//    private String unit;
//
//    private LocalDateTime lastUpdated;
//
//    @PrePersist
//    @PreUpdate
//    public void updateTimestamp() {
//        this.lastUpdated = LocalDateTime.now();
//    }
//}
package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private int quantity;
    private String unit;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
