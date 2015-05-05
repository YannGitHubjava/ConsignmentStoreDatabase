package com.KevinMcClean;
import java.math.BigDecimal;
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
    ResultSet rsDisplayConsignors = null;
    ResultSet rsDisplayRecordsInMainRoom = null;
    ResultSet rsDisplayCharityRecords = null;
    ResultSet rsDisplayBasementRecords = null;

    LinkedList<Statement> allStatements = new LinkedList<Statement>();

    PreparedStatement psAddRecord;
    PreparedStatement psNewConsignor;
    PreparedStatement psReturnRecord;
    PreparedStatement psSelectRecord;
    PreparedStatement psDeleteRecord;
    PreparedStatement psGetSaleID;
    PreparedStatement psSoldRecord;
    PreparedStatement psSale;
    PreparedStatement psDisplayConsignors;
    PreparedStatement psDisplayRecordsInMainRoom;
    PreparedStatement psDisplayCharityRecords;
    PreparedStatement psMonthOldRecords;
    PreparedStatement psDisplayBasementRecords;

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
    public ResultSet displayBasementRecords(){
        try {
            String displayBasementRecordsString = "SELECT * FROM basementRecords";
            psDisplayBasementRecords = conn.prepareStatement(displayBasementRecordsString);
            allStatements.add(psDisplayBasementRecords);
            rsDisplayBasementRecords = statement.executeQuery(displayBasementRecordsString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayBasementRecords;
        }
        return rsDisplayBasementRecords;
    }
    //this has the database send back data on the records that are in the recordsInMainRoom table, which is used for a display in the GUI.
    public ResultSet displayRecordsInMainRoom(){
        try {
            String displayRecordsinMainRoomString = "SELECT recordsInMainRoom.*,  consignors.firstname || consignors.lastName AS Name, consignors.consignorID FROM recordsInMainRoom JOIN consignors ON recordsInMainroom.consignorID = consignors.consignorID";
            psDisplayRecordsInMainRoom = conn.prepareStatement(displayRecordsinMainRoomString);
            allStatements.add(psDisplayRecordsInMainRoom);
            rsDisplayRecordsInMainRoom = statement.executeQuery(displayRecordsinMainRoomString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the records.");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayRecordsInMainRoom;
        }
        return rsDisplayRecordsInMainRoom;
    }

    //this has the database send back data on the records that are in the charityRecords table, which is used for a display in the GUI.
    public ResultSet displayCharityRecords(){
        try {
            String displayCharityRecordsString = "SELECT * FROM charityRecords";
            psDisplayCharityRecords = conn.prepareStatement(displayCharityRecordsString);
            allStatements.add(psDisplayCharityRecords);
            rsDisplayCharityRecords = statement.executeQuery(displayCharityRecordsString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayCharityRecords;
        }
        return rsDisplayCharityRecords;
    }

    //this has the database send back data on the records that are in the recordsInMainRoom table and over a month old, which is used for a display in the GUI.
    public ResultSet displayMonthOldRecords(){
        try {
            java.sql.Date monthOldDate = getMonthOldDate();
            String monthOldRecords = "SELECT * FROM recordsInMainRoom WHERE consignmentDate <= ? VALUES (?)";
            psMonthOldRecords = conn.prepareStatement(monthOldRecords);
            allStatements.add(psMonthOldRecords);
            psMonthOldRecords.setDate(1, monthOldDate);
            rsDisplayConsignors = statement.executeQuery(monthOldRecords);
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

    //this has the database send back data on the records that are in the basementRecords table and over a year old, which is used for a display in the GUI.
    public ResultSet displayYearOldRecords(){
        try {
            java.sql.Date monthOldDate = getYearOldDate();
            String monthOldRecords = "SELECT * FROM basementRecords WHERE consignmentDate <= ? VALUES (?)";
            psMonthOldRecords = conn.prepareStatement(monthOldRecords);
            allStatements.add(psMonthOldRecords);
            psMonthOldRecords.setDate(1, monthOldDate);
            rsDisplayConsignors = statement.executeQuery(monthOldRecords);
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

    //this sends out a dataset for a table of soldRecords.
    public ResultSet displaySoldRecords(){
        try {
            String displayConsignorsString = "SELECT * FROM soldRecords";
            psDisplayConsignors = conn.prepareStatement(displayConsignorsString);
            allStatements.add(psDisplayConsignors);
            rsDisplayConsignors = statement.executeQuery(displayConsignorsString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayConsignors;
        }
        return rsDisplayConsignors;
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
        java.sql.Date todaysDate = new java.sql.Date(intYear, intMonth, intDate);
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
        intMonth--;
        Integer intDate = Integer.parseInt(splitDate[DATE]);
        java.sql.Date todaysDate = new java.sql.Date(intYear, intMonth, intDate);
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
        java.sql.Date todaysDate = new java.sql.Date(intYear, intMonth, intDate);
        return todaysDate;
    }

    //this sends out a dataset for a table of consignors.
    public ResultSet displayConsignors(){
        try {
            String displayConsignorsString = "SELECT * FROM consignors";
            psDisplayConsignors = conn.prepareStatement(displayConsignorsString);
            allStatements.add(psDisplayConsignors);
            rsDisplayConsignors = statement.executeQuery(displayConsignorsString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rsDisplayConsignors;
        }
        return rsDisplayConsignors;
    }

    //this method selects the record by recordID and the table that is chosen.
    public ResultSet selectRecord(String table, int recordID) {
        String selectSoldRecord = "SELECT * FROM " + table + " WHERE recordID = ? VALUES (?)";
        ResultSet selectRecordRS;
        try {
            psSelectRecord = conn.prepareStatement(selectSoldRecord);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);
            String recordString = psSelectRecord.toString();
            selectRecordRS = statement.executeQuery(recordString);
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
        String deleteRecordSales = "DELETE FROM " + table + "WHERE recordID = ? VALUES (?)";
        try {
            psDeleteRecord = conn.prepareStatement(deleteRecordSales);
            allStatements.add(psDeleteRecord);
            psDeleteRecord.setInt(1, recordID);
            psDeleteRecord.execute();
        } catch (SQLException sqle)
        {
            System.out.println("Could not delete from recordsInMainRoom database.");
            return false;
        }
        return true;
    }

    //this has the database send back data on the consignors, which is used for a display in the GUI.
    public void newConsignor(String firstName, String lastName, String address, String city, String state, Integer phoneNo) {
        String newConsignor = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            BigDecimal bdPhoneNo = BigDecimal.valueOf(phoneNo);

            psNewConsignor = conn.prepareStatement(newConsignor);
            allStatements.add(psNewConsignor);
            psNewConsignor.setString(1, firstName);
            psNewConsignor.setString(2, lastName);
            psNewConsignor.setString(3, address);
            psNewConsignor.setString(4, city);
            psNewConsignor.setString(5, state);
            psNewConsignor.setBigDecimal(6, bdPhoneNo);
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
    public boolean recordSales(int recordID) {
        String addSoldRecord= "INSERT INTO soldRecords (recordID, consignorID, salesID, artist, title, price, consignmentDate, salesDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String addSale = "INSERT INTO sales (recordID, consignorID, salesDate, price, totalToStore, totalToConsignor) VALUES (?, ?, ?, ?, ?, ?)";
        String getSalesID = "SELECT salesID FROM sales WHERE recordID = ? VALUES (?)";

        //this method selects the record that is going to be sold.
        String table = "recordsInMainRoom";
        rs = selectRecord(table, recordID);
        java.sql.Date saleDate = getDate();
        //this try block puts a sale on the sales list.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                Float price = rs.getFloat("price");
                Float totalToConsignor = (price * Float.parseFloat(".4"));
                Float totalToStore = (price-totalToConsignor);

                psReturnRecord = conn.prepareStatement(addSale);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setDate(3, saleDate);
                psReturnRecord.setFloat(4, price);
                psReturnRecord.setFloat(5, totalToStore);
                psReturnRecord.setFloat(6, totalToConsignor);
                psReturnRecord.execute();
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to sales database.");
            return false;
        }

        //this try block adds the sold record to the soldRecords table.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignmentDate");

                psGetSaleID = conn.prepareCall(getSalesID);
                allStatements.add(psReturnRecord);
                ResultSet rs2 = statement.executeQuery(getSalesID);


                int salesID = -1;
                while(rs2.next()){
                    salesID = rs.getInt("salesID");
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
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to sold record database.");
            return false;
        }

        //this method deletes the record from the table.
        boolean delete = deleteRecordFromTable(table, recordID);
        if (!delete){
            return false;
        }
        return true;
    }

    //moves a record from the basementRecords table to the charityRecords table.
    public boolean recordToCharity(int recordID) {
        String recordToCharity = "INSERT INTO charityRecords (recordID, consignorID, artist, title, price, consignmentDate, charitytDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String selectRecordToCharity = "SELECT * FROM basementRecords WHERE recordID = ? VALUES (?)";
        String deleteRecordToCharity = "DELETE * FROM basementRecords WHERE recordID = ? VALUES (?)";

        //this block selects the record that is sent to charity from the records in the basement.
        try {
            psSelectRecord = conn.prepareStatement(selectRecordToCharity);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);
            String recordString = psSelectRecord.toString();
            rs = statement.executeQuery(recordString);
        } catch (SQLException sqle) {
            System.err.println("Error: Couldn't find record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        //this try block puts the record in the charity table.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignmentDate");
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
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to returned record database.");
            return false;
        }

        try{
            //this try block deletes the record from the basement database.
            psDeleteRecord = conn.prepareStatement(deleteRecordToCharity);
            allStatements.add(psDeleteRecord);
            psDeleteRecord.setInt(1, recordID);
            psDeleteRecord.execute();
        }
        catch (SQLException sqle){
            System.out.println("Could not delete from recordsInMainRoom database.");
            return false;
        }
        return true;
    }

    //moves a record from the recordsInMainRoom table to the basementRecords table.
    public boolean recordToBasement(int recordID) {
        String addRecordToBasement = "INSERT INTO basementRecords (recordID, consignorID, artist, title, price, consignmentDate, basementDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String selectRecordToBasement = "SELECT * FROM recordsInMainRoom WHERE recordID = ? VALUES (?)";
        String deleteRecordToBasement = "DELETE * FROM recordsInMainRoom WHERE recordID = ? VALUES (?)";
        try {
            psSelectRecord = conn.prepareStatement(selectRecordToBasement);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);
            String recordString = psSelectRecord.toString();
            rs = statement.executeQuery(recordString);
        } catch (SQLException sqle) {
            System.err.println("Error: Couldn't find record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = Float.parseFloat("1");
                java.sql.Date consignmentDate = rs.getDate("consignmentDate");
                java.sql.Date basementDate = getDate();

                psReturnRecord = conn.prepareStatement(addRecordToBasement);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, basementDate);
                psReturnRecord.execute();
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to returned record database.");
            return false;
        }

        try{
            psDeleteRecord = conn.prepareStatement(deleteRecordToBasement);
            allStatements.add(psDeleteRecord);
            psDeleteRecord.setInt(1, recordID);
            psDeleteRecord.execute();
        }
        catch (SQLException sqle){
            System.out.println("Could not delete from recordsInMainRoom database.");
            return false;
        }
        return true;
    }

    //moves a record from the recordsInMainRoom table to the returnedRecords table.
    public boolean returnRecord(int recordID) {
        String addReturnedRecord = "INSERT INTO returnedRecords (recordID, consignorID, artist, title, price, consignmentDate, returnedDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String selectReturnedRecord = "SELECT * FROM recordsInMainRoom WHERE recordID = ? VALUES (?)";
        String deleteReturnedRecord = "DELETE * FROM recordsInMainRoom WHERE recordID = ? VALUES (?)";
        try {
            psSelectRecord = conn.prepareStatement(selectReturnedRecord);
            allStatements.add(psSelectRecord);
            psSelectRecord.setInt(1, recordID);
            String recordString = psSelectRecord.toString();
            rs = statement.executeQuery(recordString);


        } catch (SQLException sqle) {
            System.err.println("Error: Couldn't find record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return false;
        }

        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                java.sql.Date consignmentDate = rs.getDate("consignmentDate");
                java.sql.Date returnDate = getDate();

                psReturnRecord = conn.prepareStatement(addReturnedRecord);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, returnDate);
                psReturnRecord.execute();
                System.out.println("Success!");
            }
        }
        catch(SQLException sqle){
            System.out.println("Could not add to returned record database.");
            return false;
        }

        try{
            psDeleteRecord = conn.prepareStatement(deleteReturnedRecord);
            allStatements.add(psDeleteRecord);
            psDeleteRecord.setInt(1, recordID);
            psDeleteRecord.execute();
        }
        catch (SQLException sqle){
            System.out.println("Could not delete from recordsInMainRoom database.");
            return false;
        }
        return true;
    }

    //this adds a record to the recordsInMainRoom table.
    public Record buyRecord(int consignorID, String artist, String title, double price){
        String addRecord = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (?, ?, ?, ?, ?)";
        Record newRecord = null;
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
            return null;
        }

        String getNewRecord = "SELECT TOP 1  FROM recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (?, ?, ?, ?, ?)";
        try {
            while (rs.next()) {

                int newRecordID = rs.getInt("recordID");
                int newConsignorID = rs.getInt("consignorID");
                String newArtist = rs.getString("artist");
                String newTitle = rs.getString("title");
                Double newPrice = rs.getDouble("price");
                Date newConsignmentDate = rs.getDate("consignmentDate");
                newRecord = new Record(newRecordID, newConsignorID, newArtist, newTitle, newPrice, newConsignmentDate);
            }
        }
        catch (SQLException sqle) {
            System.err.println("Error adding record.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }


        //if we get here, everything should have worked...
        //Return the list of laptops, which will be empty if there is no data in the database
        return newRecord;
    }

}