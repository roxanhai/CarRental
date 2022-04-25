package com.example.chungxe.controller;

import com.example.chungxe.dao.CarCategoryDAO;
import com.example.chungxe.model.CarCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CarCategoryDAO carCategoryDAO;

    @GetMapping("")
    public List<CarCategory> getListCategory() {
        return carCategoryDAO.getListCategory();
    }

    @GetMapping("/name")
    public  CarCategory getCarCategoryByName(@RequestParam("carCategoryName") String carCategoryName) {
        return carCategoryDAO.getCarCategoryByName(carCategoryName);
    }
}
