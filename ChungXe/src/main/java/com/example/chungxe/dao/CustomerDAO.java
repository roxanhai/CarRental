package com.example.chungxe.dao;

import com.example.chungxe.model.Customer;
import com.example.chungxe.model.CustomerStat;

import java.util.List;

public interface CustomerDAO {
    Customer checkLogin(String username, String password);
    Customer getCustomerByID(int cusId);
    List<Customer> getStatisticCustomerRevenue(String startDate, String endDate);
    Customer getCustomerByPhoneNumber(String phoneNum);
    List<CustomerStat> getRevenueStatByCustomer();
}
