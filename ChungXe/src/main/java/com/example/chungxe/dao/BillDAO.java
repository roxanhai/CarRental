package com.example.chungxe.dao;

import com.example.chungxe.model.Bill;
import com.example.chungxe.model.dto.BillDTO;
import com.example.chungxe.model.dto.ShortBill;

import java.util.Date;
import java.util.List;
//import java.sql.Date;
public interface BillDAO {
    List<BillDTO> getBillsByDateAndCar(int carId, String startDate, String endDate);
    BillDTO getBillById(int billId);
    List<BillDTO> getBillByDate(String startDate, String endDate);
    List<Bill> getNotConfirmedBills();
    void confirmBill(int billId, String status);
    List<Bill> getBillsByLicensePlate(String licensePlate);
    List<Bill> getBillsByPhoneNum(String phoneNum);
    BillDTO createBill(BillDTO billDTO);
    BillDTO updateBillById(BillDTO bill, int id);

}
