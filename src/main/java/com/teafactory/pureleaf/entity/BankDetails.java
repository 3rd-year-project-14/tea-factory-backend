package com.teafactory.pureleaf.entity;

import com.teafactory.pureleaf.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bank_details")
public class BankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankDetailsId;

    private String accountNumber;
    private String bankName;
    private String branch;
    private String accountHolderName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
