package com.example.chungxe.controller;


import com.example.chungxe.dao.CustomerDAO;
import com.example.chungxe.model.CustomerStat;
import com.example.chungxe.model.dto.Credential;
import com.example.chungxe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerDAO customerDAO;

    @PostMapping("/login")
    public Customer checkLogin(@RequestBody Credential credential) {
        return customerDAO.checkLogin(credential.getUsername(), credential.getPassword());
    }

    @GetMapping("/{id}")
    public Customer getCustomerByID(@PathVariable Integer id){
        return customerDAO.getCustomerByID(id);
    }

    @GetMapping("/phoneNum")
    public ResponseEntity<?> getCustomerByPhoneNum(@RequestParam String phoneNum){
        Customer result = customerDAO.getCustomerByPhoneNumber(phoneNum);
        if(result == null) return new ResponseEntity("Không tìm thấy Customer thoả mãn yêu cầu", HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statisticByCustomer")
    public List<CustomerStat> getRevenueStatByCustomer(){
        return customerDAO.getRevenueStatByCustomer();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer){
        Customer result = customerDAO.registerAccount(customer);
        if(result == null)  return new ResponseEntity("Thông tin tài khoản đã tồn tại", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(result);
    }
}
