package com.example.chungxe.controller;

import com.example.chungxe.dao.BranchDAO;
import com.example.chungxe.model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchDAO branchDAO;

    @Autowired
    ServletContext application;


    @GetMapping("")
    public List<Branch> getListBranch() {
        return branchDAO.getListBranch();
    }

    @GetMapping("/name")
    public Branch getBranchByName(@RequestParam("branchName") String branchName){
        return branchDAO.getBranchByName(branchName);
    }


}
