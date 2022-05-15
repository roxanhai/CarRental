package com.example.chungxe.dao.imp;

import com.example.chungxe.dao.CustomerDAO;
import com.example.chungxe.dao.DAO;
import com.example.chungxe.model.CarStat;
import com.example.chungxe.model.Customer;
import com.example.chungxe.model.CustomerStat;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDAOImp extends DAO implements CustomerDAO {

    public CustomerDAOImp() {
        super();
    }

    @Override
    public Customer checkLogin(String username, String password) {
        Customer customer = null;
        String sql = "SELECT * FROM tblCustomer WHERE username = ? AND password = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("fullname");
                String identityCard = rs.getString("identityCard");
                String telephone = rs.getString("telephone");
                String address = rs.getString("address");
                customer = Customer.builder()
                        .id(id)
                        .fullName(fullName)
                        .identityCard(identityCard)
                        .telephone(telephone)
                        .address(address)
                        .username(username)
                        .build();
            }
            else {
                customer = new Customer();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public Customer registerAccount(Customer customer) {
        Customer temp = this.getCustomerByPhoneNumber(customer.getTelephone());
        if(temp==null){
            String sql = "INSERT into sqa.tblCustomer(fullname, identityCard, telephone, address, username, password)" +
                    "values(?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, customer.getFullName());
                ps.setString(2, customer.getIdentityCard());
                ps.setString(3, customer.getTelephone());
                ps.setString(4, customer.getAddress());
                ps.setString(5, customer.getUsername());
                ps.setString(6, customer.getPassword());
                int res = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return this.getCustomerByPhoneNumber(customer.getTelephone());
        }

        return null;
    }


    @Override
    public Customer getCustomerByID(int cusId) {
        Customer customer = null;
        String sql = "SELECT * FROM tblCustomer WHERE id = ?";
        try {
            PreparedStatement ps =con.prepareStatement(sql);
            ps.setInt(1, cusId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("fullname");
                String identityCard = rs.getString("identityCard");
                String telephone = rs.getString("telephone");
                String address = rs.getString("address");
                String username = rs.getString("username");
                customer = Customer.builder()
                        .id(cusId)
                        .fullName(fullName)
                        .identityCard(identityCard)
                        .telephone(telephone)
                        .address(address)
                        .username(username)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getStatisticCustomerRevenue(String startDate, String endDate) {
        return null;
    }

    @Override
    public Customer getCustomerByPhoneNumber(String phoneNum) {
        Customer customer = null;
        String sql = "SELECT * FROM tblCustomer WHERE telephone = ?";
        try {
            PreparedStatement ps =con.prepareStatement(sql);
            ps.setString(1, phoneNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int cusId = rs.getInt("id");
                String fullName = rs.getString("fullname");
                String identityCard = rs.getString("identityCard");
                String telephone = rs.getString("telephone");
                String address = rs.getString("address");
                String username = rs.getString("username");
                customer = Customer.builder()
                        .id(cusId)
                        .fullName(fullName)
                        .identityCard(identityCard)
                        .telephone(telephone)
                        .address(address)
                        .username(username)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(customer==null){
            return null;
        }
        return customer;
    }

    @Override
    public List<CustomerStat> getRevenueStatByCustomer() {
        List<CustomerStat> result = new ArrayList<CustomerStat>();
        String sql = "-- CREATE TABLE CarStat  \n" +
                "-- SELECT tblCar.id, tblCar.name, tblCar.licensePlate, COUNT(tblBill.totalPrice) AS revenue\n" +
                "-- FROM sqa.tblCar LEFT JOIN sqa.tblBill ON tblCar.id = tblBill.carId\n" +
                "-- GROUP BY sqa.tblCar.id;\n" +
                "\n" +
                "SELECT tblCustomer.id, tblCustomer.fullname, tblCustomer.telephone,tblCustomer.identityCard, SUM(tblBill.totalPrice) AS revenue\n" +
                "FROM sqa.tblCustomer LEFT JOIN sqa.tblBill ON tblCustomer.id = tblBill.customerId\n" +
                "GROUP BY tblCustomer.id\n" +
                "Order By Revenue DESC;\n";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cusId = rs.getInt("id");
                String fullname = rs.getString("fullname");
                String telephone = rs.getString("telephone");
                String identityCard = rs.getString("identityCard");
                float revenue = rs.getFloat("revenue");
                result.add (new CustomerStat(cusId, fullname, telephone,identityCard, revenue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
