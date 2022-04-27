package com.example.chungxe.dao;

import com.example.chungxe.model.Bill;
import com.example.chungxe.model.dto.BillDTO;
import com.example.chungxe.model.dto.ShortBill;

import java.util.Date;
import java.util.List;
//import java.sql.Date;
public interface BillDAO {
    List<ShortBill> getBillsByCar(int carId, String startDate, String endDate);
    Bill getBillById(int billId);
    List<BillDTO> getBillByDate(String startDate, String endDate);
    List<Bill> getNotConfirmedBills();
    void confirmBill(int billId, String status);
    List<Bill> getBillsByLicensePlate(String licensePlate);
    List<Bill> getBillsByPhoneNum(String phoneNum);
    Bill createBill(BillDTO billDTO);
    Bill updateBillById(BillDTO bill, int id);

}
