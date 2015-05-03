package com.KevinMcClean;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class ConsignmentStoreController {
    ConsignmentStoreModel model;
    LinkedList<Record> allRecords;
    LinkedList<Consignor> allConsignors;
    ResultSet resultSet;

    ConsignmentStoreController() {
        model = new ConsignmentStoreModel();
        requestUpdatedConsignorsLIst();
        requestUpdatedRecordsLIst();

    }

    public void requestBuyRecords(String artist, String title, String date, Double price, int consignorID){
        Record buyRecord = model.buyRecord(consignorID, artist, title, price, date);
        if (buyRecord != null){
            allRecords.add(buyRecord);
        }
        else{
            System.out.println("Database could not purchase record.");
        }

    }

    public ResultSet requestDisplayCharityRecords(){
        resultSet = model.displayCharityRecords();
        return resultSet;
    }

    public ResultSet requestDisplayConsignors(){
        resultSet = model.displayConsignors();
        return resultSet;
    }

    public ResultSet requestDisplayRecordsinMainRoom(){
        resultSet = model.displayRecordsinMainRoom();
        return resultSet;
    }

    public ResultSet requestDisplaySoldRecords(){
        resultSet = model.displaySoldRecords();
        return resultSet;
    }

    public void requestNewConsignor(String firstName, String lastName, String address, String city, String state, Integer phoneNo){
        model.newConsignor(firstName,lastName, address, city, state, phoneNo);
    }

    public boolean requestRecordSale(int recordID){
        boolean recordSale = model.recordSales(recordID);
        return recordSale;
    }

    public boolean requestRecordToCharity(int recordID){
        boolean recordToCharity = model.recordToCharity(recordID);
        return recordToCharity;
    }

    public void requestUpdatedRecordsLIst(){
        allRecords = model.updateRecordsList();
    }

    public void requestUpdatedConsignorsLIst(){
        allConsignors = model.updateConsignorsList();
    }

    public boolean requestReturnRecord(int recordID){
        boolean recordReturn = model.returnRecord(recordID);
        return recordReturn;
    }

    public boolean requestRecordToBasement(int recordID){
        boolean recordToBasement = model.recordToBasement(recordID);
        return recordToBasement;
    }

    public void toTable(){

        ArrayList tableData = new ArrayList();
        for (Record r: allRecords) {
            tableData.add(r.toTable());
        }
    }

    public int getRecordCount() {
        int count = allRecords.size();
        return count;
    }
}
