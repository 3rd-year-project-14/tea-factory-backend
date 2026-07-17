// OOP Concept: Package (Encapsulation, Organization)
package com.teafactory.pureleaf.fertilizer.dto;

// OOP Concept: Importing Classes (Abstraction, Reusability)
import lombok.*;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleFertilizerCompanyDTO {
    // OOP Concept: Field Declaration (Encapsulation)
    private Long id;
    private String name;
}
