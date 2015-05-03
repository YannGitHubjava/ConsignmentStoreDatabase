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

    public void buyRecords(String artist, String title, String price, int consignorID){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date consignmentDate = new Date();
        String stringDate = sdf.format(consignmentDate);
        System.out.println(stringDate);
        Double doublePrice = Double.parseDouble(price);
        myController.requestBuyRecords(artist, title, stringDate, doublePrice, consignorID);
    }

    public ResultSet displayCharityRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayCharityRecords();
        return resultSet;
    }

    public ResultSet displayConsignorsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayConsignors();
        return resultSet;
    }

    public ResultSet displayRecordsinMainRoomViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayRecordsinMainRoom();
        return resultSet;
    }

    public ResultSet displaySoldRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplaySoldRecords();
        return resultSet;
    }

    public void newConsignorViewer(String firstName, String lastName, String address, String city, String state, Integer phoneNo){
        myController.requestNewConsignor(firstName, lastName, address, city, state, phoneNo);
    }

    public void newGUI(){
    }

    public boolean recordSaleViewer(int recordID){
        boolean recordSale = myController.requestRecordSale(recordID);
        return recordSale;
    }

    public boolean recordToCharityViewer(int recordID){
        boolean recordToCharity = myController.requestRecordToCharity(recordID);
        return recordToCharity;
    }

    public boolean returnRecordViewer(int recordID){
        boolean returnRecord = myController.requestReturnRecord(recordID);
        return returnRecord;
    }

    public boolean recordToBasementViewer(int recordID){
        boolean recordToBasement = myController.requestRecordToBasement(recordID);
        return recordToBasement;
    }
}
