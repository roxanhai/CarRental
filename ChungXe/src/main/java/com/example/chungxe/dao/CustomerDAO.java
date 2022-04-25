package com.example.chungxe.dao;

import com.example.chungxe.model.Customer;

import java.util.List;

public interface CustomerDAO {
    Customer checkLogin(String username, String password);
    Customer getCustomerByID(int cusId);
    List<Customer> getStatisticCustomerRevenue(String startDate, String endDate);
}
