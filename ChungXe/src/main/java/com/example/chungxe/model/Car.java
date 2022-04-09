package com.example.chungxe.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Car {
    private int id;
    private String name;
    private String color;
    private String licensePlate;
    private int seatNumber;
    private float price;
    //Image
    private String image64;
    private String status;
    private CarCategory carCategory;
    private Branch branch;
}
