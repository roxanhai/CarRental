package com.example.chungxe.controller;


import com.example.chungxe.dao.CustomerDAO;
import com.example.chungxe.model.dto.Credential;
import com.example.chungxe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



}
