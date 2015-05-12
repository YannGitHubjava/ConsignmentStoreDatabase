package com.KevinMcClean;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignmentStoreViewer extends JFrame{
    ConsignmentStoreController myController;
    private ResultSet resultSet;
    protected final static Double NOT_A_DOUBLE = Double.MIN_VALUE;
    protected final static int NOT_AN_INT = Integer.MIN_VALUE;
    protected final static int SET_TO_SUBTRACTION = -1;

    ConsignmentStoreViewer(){
    }

    ConsignmentStoreViewer(ConsignmentStoreController csc){
        this.myController = csc;
        ConsignmentStoreViewerGUI gui = new ConsignmentStoreViewerGUI(myController);
    }

    //This has the ConsignmentStoreController request that the database add a record to the recordsInMainRoom table.
    public boolean buyRecordsViewer(String artist, String title, Double price, int consignorID, ConsignmentStoreController csc){
        return csc.requestBuyRecords(artist, title, price, consignorID);
    }

    //This has the ConsignmentStoreController request that the database shut down any open connections.
    public void cleanupViewer(){
        myController.requestCleanUp();
    }

    //this pulls up the consignors who are still owed money.
    public ResultSet consignorsOwedViewer(ConsignmentStoreController csc){
        resultSet = csc.requestConsignorsOwed();
        return resultSet;
    }

    //this searches the count of how many copies of an album are already in the store.
    public boolean countSearchViewer(String artist, String title, ConsignmentStoreController csc){
        return csc.requestCountSearch(artist, title);
    }

    //this displays the records from the basementRecords table.
    public ResultSet displayBasementRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("basementRecords", " ORDER BY artist, title");
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all the records in the charityRecords table.
    public ResultSet displayCharityRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("charityRecords", " ORDER BY artist, title");
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database add a record to the recordsInMainRoom table.
    public ResultSet displayConsignorsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("consignors", " ORDER BY last_name, first_name");
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all records in the the recordsInMainRoom table that are over a month old.
    public ResultSet displayMonthOldsRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayMonthOldRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all the records in the recordsInMainRoom table.
    public ResultSet displayRecordsInMainRoomViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("mainRoomRecords", " ORDER BY artist, title");
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all the records in the soldRecords table.
    public ResultSet displaySoldRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("soldRecords", " ORDER BY artist, title");
        return resultSet;
    }

    //this displays the sales from the store.
    public ResultSet displaySalesViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("sales", "");
        return resultSet;
    }

    //this displays the returnedRecords table.
    public ResultSet displayReturnedRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestTableDisplay("returnedRecords", " ORDER BY artist, title");
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database display all records in the the basementRecords table that are over a year old.
    public ResultSet displayYearOldRecordsViewer(ConsignmentStoreController csc){
        resultSet = csc.requestDisplayYearOldRecords();
        return resultSet;
    }

    //This has the ConsignmentStoreController request that the database add a consignor to the consignors table.
    public boolean newConsignorViewer(String firstName, String lastName, String address, String city, String state, String phoneNo, ConsignmentStoreController csc){
        return csc.requestNewConsignor(firstName, lastName, address, city, state, phoneNo);
    }

    //This has the ConsignmentStoreController request that the database sell a records, which moves it from its...
    //...table to the soldRecords table, records the sale in the sales table, and deletes it from its original table.
    public boolean recordMainRoomSaleViewer(int recordID, ConsignmentStoreController csc){
        boolean recordSale = csc.requestRecordSale(recordID,"mainRoomRecords");
        return recordSale;
    }

    //this allows a sale from the recordsBasement.
    public boolean recordBasementSaleViewer(int recordID, ConsignmentStoreController csc){
        boolean recordSale = csc.requestRecordSale(recordID,"basementRecords");
        return recordSale;
    }

    //This has the ConsignmentStoreController request that the database gives a records to charity, which moves it from...
    // its basementRecords table to the charityRecords table.
    public boolean recordToCharityViewer(int recordID, ConsignmentStoreController csc){
        boolean recordToCharity = csc.requestRecordToCharity(recordID);
        return recordToCharity;
    }

    //This has the ConsignmentStoreController request that the database returns a record to the consignor, which moves it from...
    // its recordsInMainRoom table to the returnedRecords table.
    public boolean returnRecordToConsignorViewer(int recordID, ConsignmentStoreController csc){
        boolean returnRecord = csc.requestReturnRecord(recordID);
        return returnRecord;
    }

    //This has the ConsignmentStoreController request that the database move a record to the basement, which moves it from...
    // its recordsInMainRoom table to the basementRecords table.
    public boolean recordToBasementViewer(int recordID, ConsignmentStoreController csc){
        boolean recordToBasement = csc.requestRecordToBasement(recordID);
        return recordToBasement;
    }
    public ResultSet searchViewer(String table, String artist, String queryString,ConsignmentStoreController csc){
        resultSet = csc.requestSearch(table, artist, queryString);
        return resultSet;
    }


    //this searches by artist and title.
    public ResultSet searchByArtistAndTitle(String table, String artist, String title, String queryString, ConsignmentStoreController csc){
        resultSet = csc.requestSearchByArtistAndTitle(table, artist, title, queryString);
        return resultSet;
    }

    //this updates the records of the chosen table.
    public boolean updateRecordsViewer(String table, String updatedField, Float amount, String searchField, int consignorID, ConsignmentStoreController csc){
        return csc.requestUpdateRecords(table, updatedField, amount, searchField, consignorID);
    }

    //this removes "The " from the beginning of names of alphabetization purposes (ex. "The Rolling Stones" becomes "Rolling Stones, The".
    public String ivCheckNameForThe(String name){

        if (name.startsWith("The ")){
            String splitName[] = name.split(" ");
            String changedName = "";
            for (int i = 1; i < splitName.length; i++){
                changedName = changedName + splitName[i];
            }
            changedName = changedName + ", The";
            return changedName;
        }
        else {
            return name;
        }
    }

    //this checks to make sure that the phone number provided is entirely integers and 10 digits long.
    public boolean ivCheckPhoneNo(String phoneNo){
        try {
            Long.parseLong(phoneNo);
            System.out.println("Phone Number length: " + phoneNo.length());
            if(phoneNo.length() != 10){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    //this checks to make sure the price is double.
    public Double ivIsPriceDouble(String price){
        Double priceDouble;
        try{
            priceDouble = Double.parseDouble(price);

        }
        catch(NumberFormatException nfe){
            return NOT_A_DOUBLE;
        }
        return priceDouble;
    }

    //this checks to make sure a row is selected.
    public boolean ivIsRowSelected(int row){
        if(row >= 0){
            return true;
        }
        else{
            return false;
        }
    }

    //this gets the id (i.e. "record_id" or "consignor_id"), which is useful for finding a specific record.
    public int getID(String columnName, int row, JTable table, ResultSet rs){
        int id = NOT_AN_INT;
        int column;
        Object valueAt;
        String valueString;
        try {
            column = (rs.findColumn(columnName)-1);
            valueAt = table.getValueAt(row, column);
            valueString = valueAt.toString();
            id = Integer.parseInt(valueString);
        }
        catch (SQLException sqle){

        }
        return id;
    }
}
