package com.KevinMcClean;

import java.sql.ResultSet;

public class ConsignmentStoreController {
    ConsignmentStoreModel model;
    ResultSet resultSet;

    //The ConsignmentStoreController (commonly called _Controller or csc is other classes) needs access to the database model.
    ConsignmentStoreController() {
        model = new ConsignmentStoreModel();
    }

    //this requests that the database buy a new record.
    public boolean requestBuyRecords(String artist, String title, Double price, int consignorID){
        return model.buyRecord(consignorID, artist, title, price);
    }

    //requests that the database shutdown any open connections.
    public void requestCleanUp(){
        model.cleanup();
    }

    //asks for a ResultSet of the consignors who are owed money.
    public ResultSet requestConsignorsOwed(){
        return model.searchForConsignorOwed();
    }

    //requests the a countSearch be done.
    public boolean requestCountSearch(String artist, String title){
        return model.countSearch(artist, title);
    }

    //request that the database return a list of the records in the bargain basement.
    public ResultSet requestTableDisplay(String tableName, String queryString){
        resultSet = model.tableDisplay(tableName, queryString);
        return resultSet;
    }

    //request that the database return a list of the records in the main room that have been in the store for over a month.
    public ResultSet requestDisplayMonthOldRecords(){
        resultSet = model.displayMonthOldRecords();
        return resultSet;
    }

    //request that the database return a list of the records that have been in the bargain basement for over a year.
    public ResultSet requestDisplayYearOldRecords(){
        resultSet = model.displayYearOldRecords();
        return resultSet;
    }

    //this method requests that a new consignor be added to the database.
    public boolean requestNewConsignor(String firstName, String lastName, String address, String city, String state, String phoneNo){
        return model.newConsignor(firstName,lastName, address, city, state, phoneNo);
    }

    //this method requests that a new record sale be added to the database. It also moves the record from its table to the soldRecords table.
    public boolean requestRecordSale(int recordID, String table){
        boolean recordSale = model.recordSales(recordID, table);
        return recordSale;
    }

    //this method requests that a new record be moved from the basementRecords table to the charityRecords table in the database.
    public boolean requestRecordToCharity(int recordID){
        boolean recordToCharity = model.recordToCharity(recordID);
        return recordToCharity;
    }

    //this method sends a request that a record be removed from the recordsInStore table and put into the returnedRecords table.
    public boolean requestReturnRecord(int recordID){
        boolean recordReturn = model.recordReturned(recordID);
        return recordReturn;
    }

    //this methods sends a request that a record be removed from the recordsInStore table and put into the basementRecords table.
    public boolean requestRecordToBasement(int recordID){
        boolean recordToBasement = model.recordToBasement(recordID);
        return recordToBasement;
    }

    //this asks the database to perform a search.
    public ResultSet requestSearch(String table, String artist, String queryString){
        resultSet = model.search(table, artist, queryString);
        return resultSet;
    }

    //this searches by album and by title.
    public ResultSet requestSearchByArtistAndTitle(String table, String artist, String title, String queryString){
        resultSet = model.searchByArtistAndTitle(table, artist, title, queryString);
        return resultSet;
    }

    //this requests a record update.
    public boolean requestUpdateRecords(String table, String updatedField, Float amount, String searchField, int consignorID){
        return model.updateRecords(table, updatedField, amount, searchField, consignorID);
    }
}
