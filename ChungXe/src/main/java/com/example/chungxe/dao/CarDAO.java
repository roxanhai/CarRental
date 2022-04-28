package com.example.chungxe.dao;

import com.example.chungxe.model.Branch;
import com.example.chungxe.model.Car;
import com.example.chungxe.model.CarStat;
import com.example.chungxe.model.CarCategory;
import com.example.chungxe.model.Statistic;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarDAO {
    List<Car> getListCar();
    Car getCarByID(int carID);
    List<Car> searchCar(String kw, int nbrSeat, int branchId, int categoryId);
    List<Integer> getListNbrSeat();
    List<Statistic> getStatisticByCar(String startDate, String endDate);
    List<CarStat> getRevenueStatByCar();
    //New Code
    Car addCar(String name, MultipartFile imageFile, String color, String licensePlate,
               int seatNumber, float price, String carCategoryName, String branchName) throws IOException;

    Car getCarByLicensePlate (String licensePlate);

}
