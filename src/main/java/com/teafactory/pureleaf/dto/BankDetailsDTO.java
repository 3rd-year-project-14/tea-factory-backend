package com.teafactory.pureleaf.dto;

import lombok.Data;

@Data
public class BankDetailsDTO {
    private Long bankDetailsId;
    private String accountNumber;
    private String bankName;
    private String branch;
    private String accountHolderName;
    private Long userId;
}

