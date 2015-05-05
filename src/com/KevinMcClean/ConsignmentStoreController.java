package com.KevinMcClean;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class ConsignmentStoreController {
    ConsignmentStoreModel model;
    ResultSet resultSet;

    //The ConsignmentStoreController (commonly called _Controller or csc is other classes) needs access to the database model.
    ConsignmentStoreController() {
        model = new ConsignmentStoreModel();
    }

    //this requests that the database buy a new record.
    public void requestBuyRecords(String artist, String title, Double price, int consignorID){
        model.buyRecord(consignorID, artist, title, price);

    }

    //requests that the database shutdown any open connections.
    public void requestCleanUp(){
        model.cleanup();
    }

    //request that the database return a list of the records in the bargain basement.
    public ResultSet requestDisplayBasementRecords(){
        resultSet = model.displayBasementRecords();
        return resultSet;
    }

    ////request that the database return a list of the records that have been given to a charity.
    public ResultSet requestDisplayCharityRecords(){
        resultSet = model.displayCharityRecords();
        return resultSet;
    }

    //request that the database return a list of the consignors.
    public ResultSet requestDisplayConsignors(){
        resultSet = model.displayConsignors();
        return resultSet;
    }

    //request that the database return a list of the records in the main room that have been in the store for over a month.
    public ResultSet requestDisplayMonthOldRecords(){
        resultSet = model.displayMonthOldRecords();
        return resultSet;
    }

    //request that the database return a list of the records in the main room.
    public ResultSet requestDisplayRecordsinMainRoom(){
        resultSet = model.displayRecordsInMainRoom();
        return resultSet;
    }

    //request that the database return a list of the records that have been sold.
    public ResultSet requestDisplaySoldRecords(){
        resultSet = model.displaySoldRecords();
        return resultSet;
    }

    //request that the database return a list of the records that have been in the bargain basement for over a year.
    public ResultSet requestDisplayYearOldRecords(){
        resultSet = model.displayYearOldRecords();
        return resultSet;
    }

    //this method requests that a new consignor be added to the database.
    public void requestNewConsignor(String firstName, String lastName, String address, String city, String state, Integer phoneNo){
        model.newConsignor(firstName,lastName, address, city, state, phoneNo);
    }

    //this method requests that a new record sale be added to the database. It also moves the record from its table to the soldRecords table.
    public boolean requestRecordSale(int recordID){
        boolean recordSale = model.recordSales(recordID);
        return recordSale;
    }

    //this method requests that a new record be moved from the basementRecords table to the charityRecords table in the database.
    public boolean requestRecordToCharity(int recordID){
        boolean recordToCharity = model.recordToCharity(recordID);
        return recordToCharity;
    }

    //this method sends a request that a record be removed from the recordsInStore table and put into the returnedRecords table.
    public boolean requestReturnRecord(int recordID){
        boolean recordReturn = model.returnRecord(recordID);
        return recordReturn;
    }

    //this methods sends a request that a record be removed from the recordsInStore table and put into the basementRecords table.
    public boolean requestRecordToBasement(int recordID){
        boolean recordToBasement = model.recordToBasement(recordID);
        return recordToBasement;
    }

}
