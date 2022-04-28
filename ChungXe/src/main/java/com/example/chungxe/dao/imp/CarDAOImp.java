package com.example.chungxe.dao.imp;

import com.example.chungxe.dao.BranchDAO;
import com.example.chungxe.dao.CarCategoryDAO;
import com.example.chungxe.dao.CarDAO;
import com.example.chungxe.dao.DAO;
import com.example.chungxe.model.Branch;
import com.example.chungxe.model.Car;
import com.example.chungxe.model.CarCategory;
import com.example.chungxe.model.CarStat;
import com.example.chungxe.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CarDAOImp extends DAO implements CarDAO {

    public CarDAOImp(){
        super();
    }

    @Autowired
    private CarCategoryDAO carCategoryDAO;
    @Autowired
    private BranchDAO branchDAO;

    @Override
    public List<Car> getListCar() {
        List<Car> listCar = new ArrayList<>();
        String sql = "SELECT * FROM tblCar";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                String licensePlate = rs.getString("licensePlate");
                int seatNumber = rs.getInt("seatNumber");
                float price = rs.getFloat("price");
                String image = rs.getString("image64");
                String status = rs.getString("status");
                int categoryId = rs.getInt("categoryId");
                int branchId = rs.getInt("branchId");
                CarCategory carCategory = carCategoryDAO.getCarCategoryByID(categoryId);
                Branch branch = branchDAO.getBranchByID(branchId);
                Car car = Car.builder()
                        .id(id)
                        .name(name)
                        .color(color)
                        .licensePlate(licensePlate)
                        .seatNumber(seatNumber)
                        .price(price)
                        .image64(image)
                        .status(status)
                        .carCategory(carCategory)
                        .branch(branch)
                        .build();
                listCar.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCar;
    }

    @Override
    public Car getCarByID(int carID) {
        Car car = null;
        String sql = "SELECT * FROM tblCar WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, carID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String color = rs.getString("color");
                String licensePlate = rs.getString("licensePlate");
                int seatNumber = rs.getInt("seatNumber");
                float price = rs.getFloat("price");
                String image = rs.getString("image64");
                String status = rs.getString("status");
                int categoryId = rs.getInt("categoryId");
                int branchId = rs.getInt("branchId");
                CarCategory carCategory = carCategoryDAO.getCarCategoryByID(categoryId);
                Branch branch = branchDAO.getBranchByID(branchId);
                car = Car.builder()
                        .id(carID)
                        .name(name)
                        .color(color)
                        .licensePlate(licensePlate)
                        .seatNumber(seatNumber)
                        .price(price)
                        .image64(image)
                        .status(status)
                        .carCategory(carCategory)
                        .branch(branch)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }


    @Override
    public List<Car> searchCar(String kw, int nbrSeat, int branchId, int categoryId) {
        List<Car> listCar = new ArrayList<>();
        String sql = "SELECT * FROM tblCar c \n" +
                "WHERE 1=1 \n";
        if (kw != null && !kw.isEmpty()) {
            sql += "AND c.name like ?";
        }
        if (nbrSeat > 0) {
            sql += "AND c.seatNumber = " + nbrSeat + "\n";
        }
        if (branchId > 0) {
            sql += "AND c.branchId = " + branchId + "\n";
        }
        if (categoryId > 0) {
            sql += "AND c.categoryId = " + categoryId;
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            if (kw != null && !kw.isEmpty()) {
                ps.setString(1, "%" + kw + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                String licensePlate = rs.getString("licensePlate");
                int seatNumber = rs.getInt("seatNumber");
                float price = rs.getFloat("price");
                String image = rs.getString("image");
                String status = rs.getString("status");
                int branch_id = rs.getInt("branchId");
                int category_id = rs.getInt("categoryId");
                CarCategory carCategory = carCategoryDAO.getCarCategoryByID(category_id);
                Branch branch = branchDAO.getBranchByID(branch_id);
                Car car = Car.builder()
                        .id(id)
                        .name(name)
                        .color(color)
                        .licensePlate(licensePlate)
                        .seatNumber(seatNumber)
                        .price(price)
                        .image64(image)
                        .status(status)
                        .carCategory(carCategory)
                        .branch(branch)
                        .build();
                listCar.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listCar;
    }

    @Override
    public List<Integer> getListNbrSeat() {
        List<Integer> listNbrSeat = new ArrayList<>();
        String sql = "SELECT DISTINCT seatNumber from tblCar";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int nbr_of_seat = rs.getInt("seatNumber");
                listNbrSeat.add(nbr_of_seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNbrSeat;
    }

    @Override
    public List<Statistic> getStatisticByCar(String startDate, String endDate) {
        List<Statistic> result = new ArrayList<>();
        String sql = "select tblcar.id, shortBill.doanhthu, tblcar.name \n" +
                "from tblcar\n" +
                "inner join (\n" +
                "\tselect sum(totalPrice) as doanhthu, carId, createdAt from tblbill\n" +
                "\twhere createdAt BETWEEN ? AND ?\n" +
                ") as shortBill\n" +
                "on tblcar.id = carId\n" +
                "order by shortBill.doanhthu DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int doanhthu = rs.getInt("doanhthu");
                int carId = rs.getInt("id");
                String carName = rs.getString("name");
                result.add(new Statistic(doanhthu, carId,  startDate, endDate, carName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<CarStat> getRevenueStatByCar() {
        List<CarStat> result = new ArrayList<CarStat>();
        String sql = "SELECT tblCar.id, tblCar.name, tblCar.licensePlate, SUM(tblBill.totalPrice) AS revenue " +
                "FROM sqa.tblCar LEFT JOIN sqa.tblBill ON tblCar.id = tblBill.carId " +
                "GROUP BY tblCar.id Order By Revenue DESC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int carId = rs.getInt("id");
                String name = rs.getString("name");
                String licensePlate = rs.getString("licensePlate");
                float revenue = rs.getFloat("revenue");
                result.add (new CarStat(carId, name, licensePlate, revenue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Car getCarByLicensePlate(String licensePlate) {
        Car car = null;
        String sql = "SELECT * from tblCar WHERE licensePlate = ?";
        try {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,licensePlate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String licensePlateData = rs.getString("licensePlate");
                String color = rs.getString("color");
                int seatNumber = rs.getInt("seatNumber");
                float price = rs.getFloat("price");
                String image64 = rs.getString("image64");
                String status = rs.getString("status");
                int categoryId = rs.getInt("categoryId");
                int branchId = rs.getInt("branchId");
                CarCategory carCategory = carCategoryDAO.getCarCategoryByID(categoryId);
                Branch branch = branchDAO.getBranchByID(branchId);
                car = Car.builder()
                        .id(id)
                        .name(name)
                        .color(color)
                        .licensePlate(licensePlate)
                        .seatNumber(seatNumber)
                        .price(price)
                        .image64(image64)
                        .status(status)
                        .carCategory(carCategory)
                        .branch(branch)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public Car addCar(String name, MultipartFile imageFile, String color, String licensePlate, int seatNumber, float price, String carCategoryName, String branchName) throws IOException {

        Car result = this.getCarByLicensePlate(licensePlate);

        if(result == null) {
            String image64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
            CarCategory carCategory = carCategoryDAO.getCarCategoryByName(carCategoryName);
            Branch branch = branchDAO.getBranchByName(branchName);
            Car new_car = new Car();
            new_car.setName(name);
            new_car.setColor(color);
            new_car.setLicensePlate(licensePlate);
            new_car.setImage64(image64);
            new_car.setSeatNumber(seatNumber);
            new_car.setPrice(price);
            new_car.setStatus("10");
            new_car.setCarCategory(carCategory);
            new_car.setBranch(branch);


            String sql = "insert into tblCar(name, color, licensePlate, seatNumber, price, image64, status, categoryId, branchId) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, color);
                ps.setString(3, licensePlate);
                ps.setInt(4, seatNumber);
                ps.setFloat(5, price);
                ps.setString(6, image64);
                ps.setString(7, "A");
                ps.setInt(8, carCategory.getId());
                ps.setInt(9, branch.getId());
                String queryResult = String.valueOf(ps.executeUpdate());

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new_car;
        }
        return null;
    }




}
