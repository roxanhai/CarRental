package com.example.chungxe.dao.imp;


import com.example.chungxe.dao.*;
import com.example.chungxe.model.Bill;
import com.example.chungxe.model.Car;
import com.example.chungxe.model.Customer;
import com.example.chungxe.model.Employee;
import com.example.chungxe.model.dto.BillDTO;
import com.example.chungxe.model.dto.ShortBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.sql.Date;
import java.util.Date;
import java.util.List;

@Service
public class BillDAOImp extends DAO implements BillDAO {

    public BillDAOImp(){
        super();
    }

    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private CarDAO carDAO;

    @Override
    public List<BillDTO> getBillsByDateAndCar(int carId, String startDate, String endDate) {
        List<BillDTO> result = new ArrayList<>();
        Car car = carDAO.getCarByID(carId);
        String sql = "select * FROM sqa.tblBill where carId = ? and createdAt BETWEEN ? and ? and confirmStatus = 'confirm' ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String paymentMethod = rs.getString("paymentMethod");
                String confirmStatus = rs.getString("confirmStatus");
                float totalPrice = rs.getFloat("totalPrice");
                String startDateData = rs.getString("startDate");
                String endDateData = rs.getString("endDate");
                int empId = rs.getInt("employeeId");
                int carID = carId;
                int cusId = rs.getInt("customerId");

                result.add(new BillDTO(id, createdAt, paymentStatus ,confirmStatus, paymentMethod
                ,totalPrice, startDateData, endDateData ,empId, carID, cusId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BillDTO getBillById(int billId) {
        BillDTO result = null;
        String sql = "select * from tblBill where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String confirmStatus = rs.getString("confirmStatus");
                String paymentMethod = rs.getString("paymentMethod");
                float totalPrice = rs.getFloat("totalPrice");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int employeeId = rs.getInt("employeeId");
                int customerId = rs.getInt("customerId");
                int carId = rs.getInt("carId");
                result = new BillDTO(billId, createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice,
                        startDate, endDate, employeeId, carId, customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public List<BillDTO> getBillByDate(String startDate, String endDate) {
        List<BillDTO> result = new ArrayList<BillDTO>();
        String sql = "SELECT * FROM tblBill WHERE createdAt BETWEEN ? AND ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String confirmStatus = rs.getString("confirmStatus");
                String paymentMethod = rs.getString("paymentMethod");
                float totalPrice = rs.getFloat("totalPrice");
                String startDateModel = rs.getString("startDate");
                String endDateModel = rs.getString("endDate");
                int employeeId = rs.getInt("employeeId");
                int carId = rs.getInt("carId");
                int customerId = rs.getInt("customerId");

                result.add(new BillDTO(id, createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice, startDateModel, endDateModel, employeeId, carId, customerId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BillDTO createBill(BillDTO billDTO) {
        BillDTO bill = null;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String createdAt = dtf.format(now);

        String sql = "INSERT into tblBill(createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice, startDate, endDate, carId, customerId)" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, createdAt);
            ps.setString(2, billDTO.getPaymentStatus());
            ps.setString(3, billDTO.getConfirmStatus());
            ps.setString(4, billDTO.getPaymentMethod());
            ps.setFloat(5, billDTO.getTotalPrice());
            ps.setString(6, billDTO.getStartDate());
            ps.setString(7, billDTO.getEndDate());
            ps.setInt(8, billDTO.getCarId());
            ps.setInt(9, billDTO.getCustomerId());
            int res = ps.executeUpdate();
            if (res > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    bill = getBillById(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "UPDATE sqa.tblCar SET status = 'U/A' WHERE ID = " + bill.getCarId();
        try {
            PreparedStatement ps = con.prepareStatement(sql2);
            int res = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    @Override
    public BillDTO updateBillById(BillDTO bill, int id) {
        BillDTO result = null;
        BillDTO billData =  this.getBillById(id);
        String sql = "UPDATE sqa.tblBill SET createdAt = ?, paymentStatus = ? , confirmStatus = ?, paymentMethod = ?, totalPrice = ?, startDate = ?, endDate = ?,employeeId = ?, carId = ?, customerId = ?  WHERE id = ? ";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            if(bill.getCreatedAt()!=null) ps.setString(1,bill.getCreatedAt());
            else  ps.setString(1,billData.getCreatedAt());

            if(bill.getPaymentStatus()==null) ps.setString(2, billData.getPaymentStatus());
            else ps.setString(2, bill.getPaymentStatus());

            if(bill.getConfirmStatus()==null) ps.setString(3,billData.getConfirmStatus());
            else ps.setString(3,bill.getConfirmStatus());


            if(bill.getPaymentMethod()==null) ps.setString(4,billData.getPaymentMethod());
            else ps.setString(4,bill.getPaymentMethod());

            if(bill.getTotalPrice()==0.0)  ps.setFloat(5,billData.getTotalPrice());
            else  ps.setFloat(5,bill.getTotalPrice());

            if(bill.getStartDate()==null)   ps.setString(6,billData.getStartDate());
            else ps.setString(6,bill.getStartDate());

            if(bill.getEndDate()==null) ps.setString(7,billData.getEndDate());
            else ps.setString(7,bill.getEndDate());

            if(bill.getEmployeeId()==0) ps.setInt(8,billData.getEmployeeId());
            else ps.setInt(8,bill.getEmployeeId());

            if(bill.getCarId()==0) ps.setInt(9,billData.getCarId());
            else ps.setInt(9,bill.getCarId());

            if(bill.getCustomerId()==0) ps.setInt(10,billData.getCustomerId());
            else ps.setInt(10,bill.getCustomerId());



            ps.setInt(11, id);


            int res = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        result = this.getBillById(id);
        return result;
    }

    @Override
    public List<Bill> getNotConfirmedBills()  {
        List<Bill> result = new ArrayList<>();
        String sql = "select * from tblbill\n" +
                "where confirmStatus=\"pending\"";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int billId = rs.getInt("id");
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String confirmStatus = rs.getString("confirmStatus");
                String paymentMethod = rs.getString("paymentMethod");
                float totalPrice = rs.getFloat("totalPrice");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int employeeId = rs.getInt("employeeId");
                int customerId = rs.getInt("customerId");
                int carId = rs.getInt("carId");
                Employee employee = employeeDAO.getEmployeeByID(employeeId);
                Customer customer = customerDAO.getCustomerByID(customerId);
                Car car = carDAO.getCarByID(carId);
                result.add(  new Bill(billId, createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice,
                        startDate, endDate, employee, car, customer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void confirmBill(int billId, String status) {
        String sql = "update tblbill\n" +
                "set confirmStatus=?\n" +
                "where id = ?;\n";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, billId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Bill> getBillsByLicensePlate(String licensePlate) {
        Car carData = carDAO.getCarByLicensePlate(licensePlate);
        if(carData ==null) return null;
        List<Bill> result = new ArrayList<>();
        String sql = "select * from tblbill\n" +
                "where carId = ?";
        int check =0;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,carData.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int billId = rs.getInt("id");
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String confirmStatus = rs.getString("confirmStatus");
                String paymentMethod = rs.getString("paymentMethod");
                float totalPrice = rs.getFloat("totalPrice");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int employeeId = rs.getInt("employeeId");
                int customerId = rs.getInt("customerId");
                int carId = rs.getInt("carId");
                Employee employee = employeeDAO.getEmployeeByID(employeeId);
                Customer customer = customerDAO.getCustomerByID(customerId);
                Car car = carDAO.getCarByID(carId);
                result.add(  new Bill(billId, createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice,
                        startDate, endDate, employee, car, customer));
                check++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0) return null;
        return result;
    }

    @Override
    public List<Bill> getBillsByPhoneNum(String phoneNum) {
        Customer customerData = customerDAO.getCustomerByPhoneNumber(phoneNum);
        if(customerData ==null) return null;
        List<Bill> result = new ArrayList<>();
        String sql = "select * from tblbill\n" +
                "where customerId = ?";
        int check =0;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,customerData.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int billId = rs.getInt("id");
                String createdAt = rs.getString("createdAt");
                String paymentStatus = rs.getString("paymentStatus");
                String confirmStatus = rs.getString("confirmStatus");
                String paymentMethod = rs.getString("paymentMethod");
                float totalPrice = rs.getFloat("totalPrice");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int employeeId = rs.getInt("employeeId");
                int customerId = rs.getInt("customerId");
                int carId = rs.getInt("carId");
                Employee employee = employeeDAO.getEmployeeByID(employeeId);
                Customer customer = customerDAO.getCustomerByID(customerId);
                Car car = carDAO.getCarByID(carId);
                result.add(  new Bill(billId, createdAt, paymentStatus, confirmStatus, paymentMethod, totalPrice,
                        startDate, endDate, employee, car, customer));
                check++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0) return null;
        return result;
    }

}
