package com.example.chungxe.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CarStat {
    private int carID;
    private String name;
    private String licensePlate;
    private float revenue;
}

