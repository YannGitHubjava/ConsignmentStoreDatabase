package com.KevinMcClean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

    ResultSet rsConisgnorPayments = null;
    ResultSet rsMoveRecordToCharity = null;
    ResultSet rsMoveRecordToBasement = null;
    ResultSet rsDisplayConsignors = null;
    ResultSet rsTableDisplay = null;
    ResultSet rsConsignorsOwed = null;
    ResultSet rsCheckConsignors = null;
    ResultSet rsCountSearch = null;
    ResultSet rsRecordSale = null;
    ResultSet rsReturnedRecords = null;
    ResultSet rsSelectRecord = null;

    LinkedList<Statement> allStatements = new LinkedList<Statement>();
    LinkedList<ResultSet> allSets = new LinkedList<ResultSet>();

    PreparedStatement psAddRecord;
    PreparedStatement psDeleteRecord;
    PreparedStatement psGetSaleID;
    PreparedStatement psMonthOldRecords;
    PreparedStatement psMoveRecord;
    PreparedStatement psNewConsignor;
    PreparedStatement psConsignorPayments;
    PreparedStatement psRecordToBasement;
    PreparedStatement psReturnRecord;
    PreparedStatement psSearchArtist;
    PreparedStatement psSelectRecord;
    PreparedStatement psSellRecord;
    PreparedStatement psUpdateTable;
    PreparedStatement psYearOldRecords;
    PreparedStatement psCountSearch;

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
    public boolean buyRecord(int consignorID, String artist, String title, double price){
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
            return true;
        }
        catch (SQLException sqle) {
            System.err.println("Error adding record.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
    }

    //closes down the connection to the database.
    public void cleanup() {
        try {
            for(ResultSet rs: allSets) {
                if (rs != null) {
                    rs.close();  //Close result set
                    System.out.println("ResultSet closed");
                }
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
    public ResultSet tableDisplay(String table, String queryString){
        try {
            String tableDisplayString = "SELECT * FROM " + table + queryString;
            allSets.add(rsTableDisplay);
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
    public ResultSet search(String table, String artist, String queryString){
        try {

            String tableDisplayString = "SELECT * FROM " + table + queryString;

            allStatements.add(psSearchArtist);
            psSearchArtist = conn.prepareStatement(tableDisplayString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psSearchArtist.setString(1, artist);
            allSets.add(rsTableDisplay);
            rsTableDisplay = psSearchArtist.executeQuery();
        }
        catch(SQLException sqle){

            System.out.println("Could not fetch the data from " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
        }
        return rsTableDisplay;
    }

    //this fetches the number of rows for both the mainRoomRecords and the basementRecords.
    public boolean countSearch(String artist, String title){

        String[] tableArray = {"mainRoomRecords", "basementRecords"};

        int rows = 0;

        try {
            //this runs through both tables and gets the number of copies of the record for each table.
            for(String table: tableArray) {
                String countSearchSQLString = "SELECT COUNT(*) AS RowCount FROM " + table + " WHERE artist LIKE ? AND title LIKE ? ";

                allStatements.add(psCountSearch);
                psCountSearch = conn.prepareStatement(countSearchSQLString);
                psCountSearch.setString(1, artist);
                psCountSearch.setString(2, title);

                allSets.add(rsCountSearch);

                allSets.add(rsCountSearch);
                rsCountSearch = psCountSearch.executeQuery();

                while (rsCountSearch.next()) {
                    rows = rows + rsCountSearch.getInt("RowCount");
                }
            }

            //checks to make sure there are 5 or less copies.
            if(rows <= 5){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the number of copies in the store.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
    }

    //this gets the consignors that are owed money.
    public ResultSet searchForConsignorOwed(){

        try{
            String consignorsOwedSQLString = "SELECT consignor_id, first_name, last_name, phone_number, amount_owed FROM consignors WHERE amount_owed > 0";
            allSets.add(rsConsignorsOwed);
            rsConsignorsOwed = statement.executeQuery(consignorsOwedSQLString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the data from consignors table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
        }
        return rsConsignorsOwed;
    }

    //this has the database send back data on the records that are in the basementRecords table, which is used for a display in the GUI.
    public ResultSet searchByArtistAndTitle(String table, String artist, String title, String queryString){
        try {

            String tableDisplayString = "SELECT * FROM " + table + queryString;

            allStatements.add(psSearchArtist);
            psSearchArtist = conn.prepareStatement(tableDisplayString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psSearchArtist.setString(1, artist);
            psSearchArtist.setString(2, title);

            allSets.add(rsTableDisplay);
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

            allSets.add(rsDisplayConsignors);
            rsDisplayConsignors = psMonthOldRecords.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the month old Records from the mainRoomRecords table.");
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

            allSets.add(rsDisplayConsignors);
            rsDisplayConsignors = psYearOldRecords.executeQuery();
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayConsignors;
        }
        return rsDisplayConsignors;
    }

    //this gets the total that has been paid out to a given consignor.
    public Float getConsignorPayments(int id){
        String consignorPaymentsString = "SELECT total_paid FROM consignors WHERE consignor_id = ?";
        Float totalPaidToDate = 0f;
        try {
            allStatements.add(psConsignorPayments);
            psConsignorPayments = conn.prepareStatement(consignorPaymentsString);
            psConsignorPayments.setInt(1, id);

            allSets.add(rsConisgnorPayments);
            rsConisgnorPayments = psConsignorPayments.executeQuery();

            while(rsConisgnorPayments.next()){
                totalPaidToDate = rsConisgnorPayments.getFloat("total_paid");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the total_paid field");
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
        Integer intMonth = Integer.parseInt(splitDate[MONTH]);
        Integer intDate = Integer.parseInt(splitDate[DATE]);

        java.sql.Date todaysDate = new java.sql.Date(intYear-1901, intMonth-1, intDate);
        return todaysDate;
    }

    //this method selects the record by recordID and the table that is chosen.
    public ResultSet selectRecord(String table, Integer recordID) {

        String selectRecord = "SELECT * FROM " + table + " WHERE record_id = ?";

        try {
            psSelectRecord = conn.prepareStatement(selectRecord);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);

            allSets.add(rsSelectRecord);
            rsSelectRecord = psSelectRecord.executeQuery();
        } catch (SQLException sqle) {
            System.err.println("Error: Couldn't find record.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }
        return rsSelectRecord;
    }

    //this deletes the chosen record from the chosen table.
    public boolean deleteRecordFromTable(String table, int recordID) {
        String deleteRecordSales = "DELETE FROM " + table + " WHERE record_id = ?";
        try {
            allStatements.add(psDeleteRecord);
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
    public boolean newConsignor(String firstName, String lastName, String address, String city, String state, String phoneNo) {
        String newConsignor = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES (?, ?, ?, ?, ?, ?, 0, 0)" ;

        allSets.add(rsCheckConsignors);
        rsCheckConsignors = tableDisplay("consignors", "");

        try {
            while(rsCheckConsignors.next()){
                String checkFirst = rsCheckConsignors.getString("first_name");
                String checkLast = rsCheckConsignors.getString("last_name");
                String checkAddress = rsCheckConsignors.getString("address");
                String checkCity = rsCheckConsignors.getString("city");
                String checkState = rsCheckConsignors.getString("state");
                String checkPhone = rsCheckConsignors.getString("phone_number");

                if(checkFirst.equalsIgnoreCase(firstName) && checkLast.equalsIgnoreCase(lastName) &&
                        checkAddress.equalsIgnoreCase(address) && checkCity.equalsIgnoreCase(city) &&
                        checkState.equalsIgnoreCase(checkState) && checkPhone.equalsIgnoreCase(phoneNo)){
                    return false;
                }
            }

            psNewConsignor = conn.prepareStatement(newConsignor);
            allStatements.add(psNewConsignor);
            psNewConsignor.setString(1, firstName);
            psNewConsignor.setString(2, lastName);
            psNewConsignor.setString(3, address);
            psNewConsignor.setString(4, city);
            psNewConsignor.setString(5, state);
            psNewConsignor.setString(6, phoneNo);
            psNewConsignor.execute();
        } catch (SQLException sqle) {
            System.err.println("Error adding new consignor.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
        return true;

    }

    //this lists makes a new sale listing and moves the record into the soldRecords table. It also deletes the table from whichever table it is in.
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
        allSets.add(rsRecordSale);
        rsRecordSale = selectRecord(table, recordID);
        java.sql.Date saleDate = getDate();

        //this try block puts a sale on the sales table, and once that is successful adds the sold record to the soldRecords table.
        try {
            while(rsRecordSale.next()) {
                consignorID = rsRecordSale.getInt("consignor_id");
                price = rsRecordSale.getFloat("price");
                totalToConsignor = (price *.4f);
                String totalToConsignorString = String.format("%.02f", totalToConsignor);
                Float roundedTotalToConsignor = Float.parseFloat(totalToConsignorString);
                totalToStore = (price-roundedTotalToConsignor);
                System.out.println("Total to consignor: " + roundedTotalToConsignor);
                artist = rsRecordSale.getString("artist");
                title = rsRecordSale.getString("title");
                java.sql.Date consignmentDate = rsRecordSale.getDate("consignment_date");

                //adds to the sales table.
                psSellRecord = conn.prepareStatement(addSale);
                allStatements.add(psSellRecord);
                psSellRecord.setInt(1, recordID);
                psSellRecord.setInt(2, consignorID);
                psSellRecord.setDate(3, todaysDate);
                psSellRecord.setFloat(4, price);
                psSellRecord.setFloat(5, totalToStore);
                psSellRecord.setFloat(6, roundedTotalToConsignor);
                psSellRecord.execute();
                System.out.println("Added to sales table.");


                //this gets the salesID, which is needed for the soldRecords table.
                psGetSaleID = conn.prepareCall(getSalesID);
                allStatements.add(psGetSaleID);
                psGetSaleID.setInt(1, recordID);
                ResultSet rs2 = psGetSaleID.executeQuery();


                int salesID = Integer.MIN_VALUE;
                while(rs2.next()){
                    salesID = rs2.getInt("sales_id");
                }

                //adds the record to the soldRecords list.
                psMoveRecord = conn.prepareStatement(addSoldRecord);
                allStatements.add(psMoveRecord);
                psMoveRecord.setInt(1, recordID);
                psMoveRecord.setInt(2, consignorID);
                psMoveRecord.setInt(3, salesID);
                psMoveRecord.setString(4, artist);
                psMoveRecord.setString(5, title);
                psMoveRecord.setFloat(6, price);
                psMoveRecord.setDate(7, consignmentDate);
                psMoveRecord.setDate(8, saleDate);
                psMoveRecord.execute();
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to sales database.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        //updates the consignors table with what the store owes the consignor.
        boolean update = updateRecords("consignors", "amount_owed", totalToConsignor, "consignor_id", consignorID);
        if (update){
            System.out.println("Consignor has been paid.");
        }
        else{
            System.out.println("Consignor has not been paid.");
        }

        //this method deletes the record from the table.
        boolean delete = deleteRecordFromTable(table, recordID);
        if (delete){
            System.out.println("Record deleted from " + table + " table.");
            return true;
        }
        else{
            System.out.println("Record not deleted from " + table + " table.");
            return false;
        }
    }

    //moves a record from the basementRecords table to the charityRecords table.
    public boolean recordReturned(int recordID) {
        String returnRecordString = "INSERT INTO returnedRecords (record_id, consignor_id, artist, title, price, consignment_date, returned_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //this block selects the record that is being returned.
        allSets.add(rsReturnedRecords);
        rsReturnedRecords = selectRecord("mainRoomRecords", recordID);


        //this try block puts the record in the returnedRecords table.
        try {
            while(rsReturnedRecords.next()) {
                int consignorID = rsReturnedRecords.getInt("consignor_id");
                String artist = rsReturnedRecords.getString("artist");
                String title = rsReturnedRecords.getString("title");
                Float price = rsReturnedRecords.getFloat("price");
                java.sql.Date consignmentDate = rsReturnedRecords.getDate("consignment_date");
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
        allSets.add(rsMoveRecordToCharity);
        rsMoveRecordToCharity = selectRecord("basementRecords", recordID);

        //this try block puts the record in the charity table.
        try {
            while(rsMoveRecordToCharity.next()) {
                int consignorID = rsMoveRecordToCharity.getInt("consignor_id");
                String artist = rsMoveRecordToCharity.getString("artist");
                String title = rsMoveRecordToCharity.getString("title");
                Float price = rsMoveRecordToCharity.getFloat("price");
                java.sql.Date consignmentDate = rsMoveRecordToCharity.getDate("consignment_date");
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

        boolean isDeleted = deleteRecordFromTable("basementRecords", recordID);
        if (!isDeleted){
            System.out.println("Could not delete record from basementRecords table.");
            return false;
        }
        return true;
    }

    //moves a record from the mainRoomRecords table to the basementRecords table.
    public boolean recordToBasement(int recordID) {
        String addRecordToBasement = "INSERT INTO basementRecords (record_id, consignor_id, artist, title, price, consignment_date, basement_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        //selects the record to move to the basement.
        allSets.add(rsMoveRecordToBasement);
        rsMoveRecordToBasement = selectRecord("mainRoomRecords", recordID);

        try {
            while(rsMoveRecordToBasement.next()) {
                int consignorID = rsMoveRecordToBasement.getInt("consignor_id");
                String artist = rsMoveRecordToBasement.getString("artist");
                String title = rsMoveRecordToBasement.getString("title");
                Float price = Float.parseFloat("1");
                java.sql.Date consignmentDate = rsMoveRecordToBasement.getDate("consignment_date");
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

        //checks to make sure it has been deleted from the mainRoomRecord table.
        boolean isDeleted = deleteRecordFromTable("mainRoomRecords", recordID);
        if (!isDeleted){
            System.out.println("Could not delete record from mainRoomRecords");
            return false;
        }
        return true;
    }

    //this updates a specific record at a specific field according to a specific criteria.
    public boolean updateRecords(String table, String updatedField, Float amount, String searchField, int id){
        String updateString = "UPDATE " + table + " SET " + updatedField + " = ? WHERE " + searchField + " = ?";

        if(updatedField.equals("total_paid")) {
            Float totalPaymentsToDate = getConsignorPayments(id);
            amount = amount + totalPaymentsToDate;
        }

        try{
            psUpdateTable = conn.prepareStatement(updateString);
            allStatements.add(psUpdateTable);
            psUpdateTable.setFloat(1, amount);
            psUpdateTable.setInt(2, id);
            psUpdateTable.execute();
        }
        catch(SQLException sqle){
            System.out.println("Could not update " + table + " table.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }
        return true;
    }

}