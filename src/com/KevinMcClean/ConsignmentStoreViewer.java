package com.KevinMcClean;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignmentStoreViewer extends JFrame{
    ConsignmentStoreController myController;
    private ResultSet resultSet;

    ConsignmentStoreViewer(){



    }

    ConsignmentStoreViewer(ConsignmentStoreController csc){
        this.myController = csc;
        ConsignmentStoreViewerGUI gui = new ConsignmentStoreViewerGUI(myController);
    }

    //This has the ConsignmentStoreController request that the database add a record to the recordsInMainRoom table.
    public void buyRecordsViewer(String artist, String title, String price, int consignorID){
        Double doublePrice = Double.parseDouble(price);
        myController.requestBuyRecords(artist, title, doublePrice, consignorID);
    }

    //This has the ConsignmentStoreController request that the database shut down any open connections.
    public void cleanupViewer(){
        myController.requestCleanUp();
    }

    //This has the ConsignmentStoreController request that the database display all the records in the charityRecords table.
    public ResultSet displayCharityRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayCharityRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database add a record to the recordsInMainRoom table.
    public ResultSet displayConsignorsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayConsignors();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all records in the the recordsInMainRoom table that are over a month old.
    public ResultSet displayMonthOldsRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayMonthOldRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all the records in the recordsInMainRoom table.
    public ResultSet displayRecordsinMainRoomViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayRecordsinMainRoom();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all the records in the soldRecords table.
    public ResultSet displaySoldRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplaySoldRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all records in the the basementRecords table that are over a year old.
    public ResultSet displayYearOldRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayYearOldRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database add a consignor to the consignors table.
    public void newConsignorViewer(String firstName, String lastName, String address, String city, String state, Integer phoneNo, ConsignmentStoreController csc){
        csc.requestNewConsignor(firstName, lastName, address, city, state, phoneNo);
    }

    //This has the ConsignmentStoreController request that the database sell a records, which moves it from its...
    //...table to the soldRecords table, records the sale in the sales table, and deletes it from its original table.
    public boolean recordSaleViewer(int recordID){
        boolean recordSale = myController.requestRecordSale(recordID);
        return recordSale;
    }

    //This has the ConsignmentStoreController request that the database gives a records to charity, which moves it from...
    // its basementRecords table to the charityRecords table.
    public boolean recordToCharityViewer(int recordID){
        boolean recordToCharity = myController.requestRecordToCharity(recordID);
        return recordToCharity;
    }

    //This has the ConsignmentStoreController request that the database returns a record to the consignor, which moves it from...
    // its recordsInMainRoom table to the returnedRecords table.
    public boolean returnRecordViewer(int recordID){
        boolean returnRecord = myController.requestReturnRecord(recordID);
        return returnRecord;
    }

    //This has the ConsignmentStoreController request that the database move a record to the basement, which moves it from...
    // its recordsInMainRoom table to the basementRecords table.
    public boolean recordToBasementViewer(int recordID){
        boolean recordToBasement = myController.requestRecordToBasement(recordID);
        return recordToBasement;
    }
}
