package com.example.chungxe.controller;

import com.example.chungxe.dao.CarDAO;
import com.example.chungxe.model.Car;
import com.example.chungxe.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    CarDAO carDAO;

    @Autowired
    ServletContext application;

    @GetMapping("/statistic")
    public List<Statistic> getStatisticByCar(@RequestParam String startDate, String endDate){
        return carDAO.getStatisticByCar(startDate, endDate);
    }

    @GetMapping("")
    public List<Car> getListCar() {
        return carDAO.getListCar();
    }

    @GetMapping("/carid")
    public Car getCarByID(@RequestParam int carid) {
        return carDAO.getCarByID(carid);
    }

    @GetMapping("/search")
    public List<Car> searchCar(@RequestParam(required = false) String kw, @RequestParam(required = false, defaultValue = "0") int nbrSeat,
                               @RequestParam(required = false, defaultValue = "0") int branchId,
                               @RequestParam(required = false, defaultValue = "0") int categoryId) {
        return carDAO.searchCar(kw, nbrSeat, branchId, categoryId);
    }

    @GetMapping("/getListSeat")
    public List<Integer> getListNbrSeat() {
        return carDAO.getListNbrSeat();
    }

    //New Code
    @PostMapping("/addCar")
    public Car addCar(@RequestParam("name") String name,
                      @RequestParam("imageFile") MultipartFile imageFile,
                      @RequestParam("color") String color,
                      @RequestParam("licensePlate") String licensePlate,
                      @RequestParam("seatNumber") int seatNumber,
                      @RequestParam("price") int price,
                      @RequestParam("status") String status,
                      @RequestParam("carCategoryID") int carCategoryID,
                      @RequestParam("branchID") int branchID) throws IOException {
        System.out.println(imageFile.getOriginalFilename());
        System.out.println(imageFile.getContentType());
        return carDAO.addCar(name, imageFile, color, licensePlate, seatNumber, price, status,
                carCategoryID, branchID);
    }

}
