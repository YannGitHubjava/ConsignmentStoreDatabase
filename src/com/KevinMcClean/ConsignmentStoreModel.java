package com.KevinMcClean;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


/**
 * Created by Kevin on 4/21/2015.
 */
public class ConsignmentStoreModel {

    // JDBC driver name, protocol, used to create a connection to the DB
    private static String protocol = "jdbc:derby:";
    private static String dbName = "ConsignmentStoreDB";

    //  Database credentials - for embedded, usually defaults. A client-server DB would need to authenticate connections
    private static final String USER = "temp";
    private static final String PASS = "password";

    Statement statement = null;

    Connection conn = null;

    ResultSet rs = null;
    ResultSet rsDisplayBasementRecords = null;
    ResultSet rsDisplayCharityRecords = null;
    ResultSet rsDisplayConsignors = null;
    ResultSet rsDisplayMainRoomRecords = null;
    ResultSet rsTableDisplay = null;

    LinkedList<Statement> allStatements = new LinkedList<Statement>();

    PreparedStatement psAddRecord;
    PreparedStatement psDeleteRecord;
    PreparedStatement psDisplayBasementRecords;
    PreparedStatement psDisplayCharityRecords;
    PreparedStatement psDisplayConsignors;
    PreparedStatement psDisplayMainRoomRecords;
    PreparedStatement psGetSaleID;
    PreparedStatement psMonthOldRecords;
    PreparedStatement psNewConsignor;
    PreparedStatement psConsignorPayments;
    PreparedStatement psRecordToBasement;
    PreparedStatement psReturnRecord;
    PreparedStatement psSearchArtist;
    PreparedStatement psSearchArtistAndTitle;
    PreparedStatement psSearchTitle;
    PreparedStatement psSelectRecord;
    PreparedStatement psSellRecord;
    PreparedStatement psUpdateTable;
    PreparedStatement psYearOldRecords;



    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DATE = 2;

    public ConsignmentStoreModel() {
        try {
            createConnection();
        } catch (Exception e) {
            System.out.println("Could not create a connection.");
        }
    }

    //this adds a record to the mainRoomRecords table.
    public void buyRecord(int consignorID, String artist, String title, double price){
        String addRecord = "INSERT INTO mainRoomRecords (consignor_id, artist, title, price, consignment_date) VALUES (?, ?, ?, ?, ?)";
        try {

            java.sql.Date consignmentDate = getDate();

            psAddRecord = conn.prepareStatement(addRecord);
            allStatements.add(psAddRecord);
            psAddRecord.setInt(1, consignorID);
            psAddRecord.setString(2, artist);
            psAddRecord.setString(3, title);
            psAddRecord.setDouble(4, price);
            psAddRecord.setDate(5, consignmentDate);
            psAddRecord.execute();
        }
        catch (SQLException sqle) {
            System.err.println("Error adding record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
        }

        //if we get here, everything should have worked...
        //Return the list of laptops, which will be empty if there is no data in the database
    }

    //closes down the connection to the database.
    public void cleanup() {
        // TODO Auto-generated method stub
        try {
            if (rs != null) {
                rs.close();  //Close result set
                System.out.println("ResultSet closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        //Close all of the statements. Stored a reference to each statement in allStatements so we can loop over all of them and close them all.
        for (Statement s : allStatements) {

            if (s != null) {
                try {
                    s.close();
                    System.out.println("Statement closed");
                } catch (SQLException se) {
                    System.out.println("Error closing statement");
                    se.printStackTrace();
                }
            }
        }

        try {
            if (conn != null) {
                conn.close();  //Close connection to database
                System.out.println("Database connection closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    //connects the class to the database.
    private void createConnection() throws Exception {

        try {
            conn = DriverManager.getConnection(protocol + dbName + ";create=true", USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            allStatements.add(statement);
        } catch (Exception e) {
            //There are a lot of things that could go wrong here. Should probably handle them all separately but have not done so here.
            //Should put something more helpful here...
            throw e;
        }
    }

    //this has the database send back data on the records that are in the basementRecords table, which is used for a display in the GUI.
    public ResultSet tableDisplay(String table){
        try {
            String tableDisplayString = "SELECT * FROM " + table;
            if (table.contains("Records")){
                tableDisplayString = tableDisplayString + " ORDER BY artist, title";
            }
            else if(table.contains("consignors")){
                tableDisplayString = tableDisplayString + " ORDER BY last_name, first_name";
            }
            System.out.println("Table string: " + tableDisplayString);
            rsTableDisplay = statement.executeQuery(tableDisplayString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the data from " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsTableDisplay;
        }
        return rsTableDisplay;
    }

    //this has the database send back data on the records that are in the basementRecords table, which is used for a display in the GUI.
    public ResultSet searchByArtist(String table, String artist){
        try {
            artist = "%" + artist + "%";
            String tableDisplayString = "SELECT * FROM " + table + " WHERE artist LIKE ? ORDER BY artist, title";
            psSearchArtist = conn.prepareStatement(tableDisplayString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psSearchArtist.setString(1, artist);
            rsTableDisplay = psSearchArtist.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the data from " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsTableDisplay;
        }
        return rsTableDisplay;
    }

    //this has the database send back data on the records that are in the basementRecords table, which is used for a display in the GUI.
    public ResultSet searchByArtistAndTitle(String table, String artist, String title){
        try {
            artist = "%" + artist + "%";
            title = "%" + title + "%";
            String tableDisplayString = "SELECT * FROM " + table + " WHERE artist LIKE ? AND title LIKE ? ORDER BY artist, title";
            psSearchArtist = conn.prepareStatement(tableDisplayString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psSearchArtist.setString(1, artist);
            psSearchArtist.setString(2, title);
            rsTableDisplay = psSearchArtist.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the data from " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsTableDisplay;
        }
        return rsTableDisplay;
    }

    //this has the database send back data on the records that are in the basementRecords table, which is used for a display in the GUI.
    public ResultSet searchByTitle(String table, String title){
        try {
            title = "%" + title + "%";
            String tableDisplayString = "SELECT * FROM " + table + " WHERE title LIKE ? ORDER BY artist, title";
            psSearchArtist = conn.prepareStatement(tableDisplayString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psSearchArtist.setString(1, title);
            rsTableDisplay = psSearchArtist.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the data from " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsTableDisplay;
        }
        return rsTableDisplay;
    }

    //this has the database send back data on the records that are in the mainRoomRecords table and over a month old, which is used for a display in the GUI.
    public ResultSet displayMonthOldRecords(){
        try {
            java.sql.Date monthOldDate = getMonthOldDate();
            String monthOldRecords = "SELECT mainRoomRecords.*, consignors.first_name, consignors.last_name, consignors.phone_number FROM mainRoomRecords JOIN consignors ON mainRoomRecords.consignor_id = consignors.consignor_id WHERE consignment_date <= ?";
            allStatements.add(psMonthOldRecords);
            psMonthOldRecords = conn.prepareStatement(monthOldRecords, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psMonthOldRecords.setDate(1, monthOldDate);
            rsDisplayConsignors = psMonthOldRecords.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the month old Records from the mainRoomRecords table.");
            //TODO remove these two before turning in.
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayConsignors;
        }
        return rsDisplayConsignors;
    }

    //this has the database send back data on the records that are in the basementRecords table and over a year old, which is used for a display in the GUI.
    public ResultSet displayYearOldRecords(){
        try {
            java.sql.Date yearOldDate = getYearOldDate();
            String yearOldRecords = "SELECT * FROM basementRecords WHERE consignment_date <= ?";
            allStatements.add(psYearOldRecords);
            psYearOldRecords = conn.prepareStatement(yearOldRecords, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psYearOldRecords.setDate(1, yearOldDate);
            rsDisplayConsignors = psYearOldRecords.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            //TODO remove these two before turning in.
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayConsignors;
        }
        return rsDisplayConsignors;
    }

    public Float getConsignorPayments(int id){
        String consignorPaymentsString = "SELECT total_paid FROM consignors WHERE consignor_id = ?";
        Float totalPaidToDate = null;
        try {
            psConsignorPayments = conn.prepareStatement(consignorPaymentsString);
            psConsignorPayments.setInt(1, id);
            rs = psConsignorPayments.executeQuery();
            while(rs.next()){
                totalPaidToDate = rs.getFloat("total_paid");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the total_paid field");
            //TODO remove these two before turning in.
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }
        return totalPaidToDate;
    }

    //this gets the current date and formats it for use in Derby.
    public java.sql.Date getDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String dateString = sdf.format(date).toString();
        String splitDate[] = dateString.split("-");
        Integer intYear = Integer.parseInt(splitDate[YEAR]);
        Integer intMonth = Integer.parseInt(splitDate[MONTH]);
        Integer intDate = Integer.parseInt(splitDate[DATE]);
        java.sql.Date todaysDate = new java.sql.Date(intYear-1900, intMonth-1, intDate);
        return todaysDate;
    }

    //this gets the date from a month ago and formats it for use in Derby.
    public java.sql.Date getMonthOldDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String dateString = sdf.format(date).toString();
        String splitDate[] = dateString.split("-");
        Integer intYear = Integer.parseInt(splitDate[YEAR]);
        Integer intMonth = Integer.parseInt(splitDate[MONTH]);
        Integer intDate = Integer.parseInt(splitDate[DATE]);
        java.sql.Date todaysDate = new java.sql.Date(intYear-1900, intMonth-2, intDate);
        return todaysDate;
    }

    //this gets the date from a year ago and formats it for use in Derby.
    public java.sql.Date getYearOldDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String dateString = sdf.format(date).toString();
        String splitDate[] = dateString.split("-");
        Integer intYear = Integer.parseInt(splitDate[YEAR]);
        intYear--;
        Integer intMonth = Integer.parseInt(splitDate[MONTH]);
        Integer intDate = Integer.parseInt(splitDate[DATE]);
        java.sql.Date todaysDate = new java.sql.Date(intYear-1901, intMonth-1, intDate);
        return todaysDate;
    }

    //this method selects the record by recordID and the table that is chosen.
    public ResultSet selectRecord(String table, Integer recordID) {
        System.out.println("RecordID: " + recordID);
        String selectRecord = "SELECT * FROM " + table + " WHERE record_id = ?";
        ResultSet selectRecordRS;
        try {
            System.out.println(recordID);
            psSelectRecord = conn.prepareStatement(selectRecord);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);
            selectRecordRS = psSelectRecord.executeQuery();
        } catch (SQLException sqle) {
            System.err.println("Error: Couldn't find record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }
        return selectRecordRS;
    }

    //this deletes the chosen record from the chosen table.
    public boolean deleteRecordFromTable(String table, int recordID) {
        String deleteRecordSales = "DELETE FROM " + table + " WHERE record_id = ?";
        try {
            psDeleteRecord = conn.prepareStatement(deleteRecordSales);
            allStatements.add(psDeleteRecord);
            psDeleteRecord.setInt(1, recordID);
            psDeleteRecord.execute();
        } catch (SQLException sqle)
        {
            System.out.println("Could not delete from mainRoomRecords database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
        return true;
    }

    //this has the database send back data on the consignors, which is used for a display in the GUI.
    public void newConsignor(String firstName, String lastName, String address, String city, String state, String phoneNo) {
        String newConsignor = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, total_paid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            psNewConsignor = conn.prepareStatement(newConsignor);
            allStatements.add(psNewConsignor);
            psNewConsignor.setString(1, firstName);
            psNewConsignor.setString(2, lastName);
            psNewConsignor.setString(3, address);
            psNewConsignor.setString(4, city);
            psNewConsignor.setString(5, state);
            psNewConsignor.setString(6, phoneNo);
            psNewConsignor.setFloat(7, 0);
            psNewConsignor.execute();
        } catch (SQLException sqle) {
            System.err.println("Error adding new consignor.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return;
        }
        System.out.println("Success!");
    }

    //this lists makes a new sale listing and moves the record into the soldRecords table. It also deletes the table from whichever table it is in.
    //TODO make it so that the program knows which table contains the record. Should be a string that comes into this method.
    public boolean recordSales(int recordID, String table) {

        int consignorID = 0;
        String artist;
        String title;
        Float price;
        Float totalToConsignor = null;
        Float totalToStore;
        java.sql.Date todaysDate = getDate();

        String addSoldRecord= "INSERT INTO soldRecords (record_id, consignor_id, sales_id, artist, title, price, consignment_date, date_sold) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String addSale = "INSERT INTO sales (record_id, consignor_id, sale_date, price, total_to_store, total_to_consignor) VALUES (?, ?, ?, ?, ?, ?)";
        String getSalesID = "SELECT sales_id FROM sales WHERE record_id = ?";

        //this method selects the record that is going to be sold.
        rs = selectRecord(table, recordID);
        java.sql.Date saleDate = getDate();
        //this try block puts a sale on the sales list.
        try {
            while(rs.next()) {
                consignorID = rs.getInt("consignor_id");
                price = rs.getFloat("price");
                totalToConsignor = (price * Float.parseFloat(".4"));
                totalToStore = (price-totalToConsignor);

                psSellRecord = conn.prepareStatement(addSale);
                allStatements.add(psSellRecord);
                psSellRecord.setInt(1, recordID);
                psSellRecord.setInt(2, consignorID);
                psSellRecord.setDate(3, todaysDate);
                psSellRecord.setFloat(4, price);
                psSellRecord.setFloat(5, totalToStore);
                psSellRecord.setFloat(6, totalToConsignor);
                psSellRecord.execute();
                System.out.println("Added to sales table.");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to sales database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        //this try block adds the sold record to the soldRecords table.
        rs = selectRecord(table, recordID);
        try {
            while(rs.next()) {
                consignorID = rs.getInt("consignor_id");
                artist = rs.getString("artist");
                title = rs.getString("title");
                price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignment_date");

                psGetSaleID = conn.prepareCall(getSalesID);
                allStatements.add(psReturnRecord);
                psGetSaleID.setInt(1, recordID);
                ResultSet rs2 = psGetSaleID.executeQuery();
                System.out.println(rs2.toString());
                int salesID = -1;
                while(rs2.next()){
                    salesID = rs2.getInt("sales_id");
                }

                psReturnRecord = conn.prepareStatement(addSoldRecord);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setInt(3, salesID);
                psReturnRecord.setString(4, artist);
                psReturnRecord.setString(5, title);
                psReturnRecord.setFloat(6, price);
                psReturnRecord.setDate(7, consignmentDate);
                psReturnRecord.setDate(8, saleDate);
                psReturnRecord.execute();
                System.out.println("Record added to soldRecords database.");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to soldRecord database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
        //
        updateRecords("consignors", "total_paid", totalToConsignor, "consignor_id", consignorID);

        //this method deletes the record from the table.
        boolean delete = deleteRecordFromTable(table, recordID);
        if (!delete){
            System.out.println("Record not deleted from " + table + " table.");
            return false;
        }
        return true;
    }

    //moves a record from the basementRecords table to the charityRecords table.
    public boolean recordReturned(int recordID) {
        String returnRecordString = "INSERT INTO returnedRecords (record_id, consignor_id, artist, title, price, consignment_date, returned_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //this block selects the record that is being returned.
        rs = selectRecord("mainRoomRecords", recordID);


        //this try block puts the record in the returnedRecords table.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignor_id");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignment_date");
                java.sql.Date charityDate = getDate();

                psReturnRecord = conn.prepareStatement(returnRecordString);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, charityDate);
                psReturnRecord.execute();
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to returnedRecords database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
        deleteRecordFromTable("mainRoomRecords", recordID);
        return true;
    }

    //moves a record from the basementRecords table to the charityRecords table.
    public boolean recordToCharity(int recordID) {
        String recordToCharity = "INSERT INTO charityRecords (record_id, consignor_id, artist, title, price, consignment_date, charity_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //this method selects the record that is sent to charity from the records in the basement.
        rs = selectRecord("basementRecords", recordID);

        //this try block puts the record in the charity table.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignor_id");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignment_date");
                java.sql.Date charityDate = getDate();
                psReturnRecord = conn.prepareStatement(recordToCharity);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, charityDate);
                psReturnRecord.execute();
                System.out.println("Record added to charityRecords table.");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to charityRecords database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        deleteRecordFromTable("basementRecords", recordID);
        return true;
    }

    //moves a record from the mainRoomRecords table to the basementRecords table.
    public boolean recordToBasement(int recordID) {
        String addRecordToBasement = "INSERT INTO basementRecords (record_id, consignor_id, artist, title, price, consignment_date, basement_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        rs = selectRecord("mainRoomRecords", recordID);

        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignor_id");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = Float.parseFloat("1");
                java.sql.Date consignmentDate = rs.getDate("consignment_date");
                java.sql.Date basementDate = getDate();

                psRecordToBasement = conn.prepareStatement(addRecordToBasement);
                allStatements.add(psRecordToBasement);
                psRecordToBasement.setInt(1, recordID);
                psRecordToBasement.setInt(2, consignorID);
                psRecordToBasement.setString(3, artist);
                psRecordToBasement.setString(4, title);
                psRecordToBasement.setFloat(5, price);
                psRecordToBasement.setDate(6, consignmentDate);
                psRecordToBasement.setDate(7, basementDate);
                psRecordToBasement.execute();
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to returned record database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();

            return false;
        }

        boolean isDeleted = deleteRecordFromTable("mainRoomRecords", recordID);
        if (!isDeleted){
            System.out.println("Could not delete record from mainRoomRecords");
            return false;
        }
        return true;
    }

    public void updateRecords(String table, String updatedField, Float amount, String searchField, int id){
        String updateString = "UPDATE " + table + " SET " + updatedField + " = ? WHERE " + searchField + " = ?";
        Float totalPaymentsToDate = getConsignorPayments(id);
        amount = amount + totalPaymentsToDate;
        try{
            psUpdateTable = conn.prepareStatement(updateString);
            allStatements.add(psUpdateTable);
            psUpdateTable.setFloat(1, amount);
            psUpdateTable.setInt(2, id);
        }
        catch(SQLException sqle){
            System.out.println("Could not update " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
        }
    }

}