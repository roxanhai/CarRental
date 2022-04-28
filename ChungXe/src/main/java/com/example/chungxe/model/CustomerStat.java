package com.example.chungxe.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CustomerStat {
    private int customerID;
    private String fullname;
    private String telephone;
    private String identityCard;
    private float revenue;
}
