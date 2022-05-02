package com.example.chungxe.controller;

import com.example.chungxe.dao.BillDAO;
import com.example.chungxe.model.Bill;
import com.example.chungxe.model.dto.BillDTO;
import com.example.chungxe.model.dto.Confirm;
import com.example.chungxe.model.dto.ShortBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    BillDAO billDAO;

    /*GET REQUEST*/
    @GetMapping("car")
    public List<BillDTO> getBillsByCarAndDate(@RequestParam int carId, @RequestParam String startDate, @RequestParam String endDate){
        startDate += " 00:00:00";
        endDate += " 23:59:59";
        System.out.println(startDate);
        System.out.println(endDate);
        return billDAO.getBillsByDateAndCar(carId, startDate, endDate);
    }
    @GetMapping("/bill_id")
    public ResponseEntity<?> getBillById(@RequestParam int id){
        BillDTO result = billDAO.getBillById(id);
        if(result == null) return new ResponseEntity("Không tìm thấy Bill thoả mãn yêu cầu", HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create_bill")
    public BillDTO createBill(@RequestBody BillDTO billDTO) {
        return billDAO.createBill(billDTO);
    }

    @GetMapping("/pending")
    public List<Bill> getNotConfirmedBills(){
        return billDAO.getNotConfirmedBills();
    }

    @GetMapping("/date")
    public List<BillDTO> getBillsByDate(@RequestParam String startDate, @RequestParam String endDate){
        return billDAO.getBillByDate(startDate, endDate);
    }

    @GetMapping("/licensePlate")
    public ResponseEntity<?> getBillsBylicensePlate(@RequestParam String licensePlate){
        List<Bill> result = billDAO.getBillsByLicensePlate(licensePlate);
        if(result == null) return new ResponseEntity("Không tìm thấy Bill thoả mãn yêu cầu", HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/phoneNum")
    public ResponseEntity<?> getBillsByPhoneNum(@RequestParam String phoneNum){
        List<Bill> result = billDAO.getBillsByPhoneNum(phoneNum);
        if(result == null) return new ResponseEntity("Không tìm thấy Bill thoả mãn yêu cầu", HttpStatus.NOT_IMPLEMENTED);
        return ResponseEntity.ok(result);
    }
    /*POST REQUEST*/
    @PostMapping("confirm")
    public void confirmBill(@RequestBody Confirm data){
        billDAO.confirmBill(data.getId(), data.getStatus());
    }

    @PutMapping("/{id}")
    public BillDTO updateBillById(@PathVariable("id") int id, @RequestBody BillDTO bill){

        return billDAO.updateBillById(bill, id);
    }
}
