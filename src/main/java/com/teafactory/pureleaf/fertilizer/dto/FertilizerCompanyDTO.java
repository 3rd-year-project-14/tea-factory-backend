package com.teafactory.pureleaf.fertilizer.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerCompanyDTO {
    private Long id;
    private String name;
    private String address;
    private String contactPerson;
    private String contactNumber;
    private String email;
    private List<String> categories; // category names
}
