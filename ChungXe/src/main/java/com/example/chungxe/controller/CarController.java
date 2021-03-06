package com.example.chungxe.controller;

import com.example.chungxe.dao.CarDAO;
import com.example.chungxe.model.Car;
import com.example.chungxe.model.CarStat;
import com.example.chungxe.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/licensePlate")
    public Car getCarByLicensePlate(@RequestParam String licensePlate) {
        return carDAO.getCarByLicensePlate(licensePlate);
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

    @GetMapping("/statisticByCar")
    public List<CarStat> getRevenueStatByCar(){
        return carDAO.getRevenueStatByCar();
    }

    @GetMapping("/getAvalibleCar")
    public List<Car> getListAvalibleCar(){
        return carDAO.getListAvalibleCar();
    }

    //New Code
    @PostMapping("/addCar")
    public ResponseEntity<?> addCar(@RequestParam("name") String name,
                      @RequestParam("imageFile") MultipartFile imageFile,
                      @RequestParam("color") String color,
                      @RequestParam("licensePlate") String licensePlate,
                      @RequestParam("seatNumber") int seatNumber,
                      @RequestParam("price") int price,
                      @RequestParam("carCategoryName") String carCategoryName,
                      @RequestParam("branchName") String branchName) throws IOException {
        Car result = carDAO.addCar(name, imageFile, color, licensePlate, seatNumber, price,
                carCategoryName, branchName);
        System.out.println(result);

        //Fix l???i ph???n h???i ??? ????y
        if (result == null){
            return new ResponseEntity("Xe ???? t???n t???i trong database", HttpStatus.NOT_IMPLEMENTED);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteCarByID")
    public Car deleteCarByID(@RequestParam int id){
        return carDAO.deleteCarByID(id);
    }

    @PutMapping("/updateCarByID/{id}")
    public Car updateCarByID(@PathVariable("id") int id, @RequestBody Car car){
        return carDAO.updateCarByID(car, id);
    }
}
