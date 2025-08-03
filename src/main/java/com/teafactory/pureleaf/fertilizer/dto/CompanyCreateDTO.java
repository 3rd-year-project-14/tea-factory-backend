package com.teafactory.pureleaf.fertilizer.dto;

import lombok.Data;
import java.util.Set;
import java.util.List;

@Data
public class CompanyCreateDTO {
    private String companyName;
    private String address;
    private String contactPerson;
    private String phone;
    private String email;
    private Set<Long> fertilizerTypeIds;
    private List<FertilizerTypeCreateDTO> newFertilizerTypes;
}
