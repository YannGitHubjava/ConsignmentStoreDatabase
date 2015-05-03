package com.KevinMcClean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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
    ResultSet rspsDisplayCharityRecords = null;

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


    public ConsignmentStoreModel() {
        try {
            createConnection();
        } catch (Exception e) {
            System.out.println("Could not create a connection.");
        }
    }


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


    //this sends out a dataset for a table of records on the main floor.
    public ResultSet displayRecordsinMainRoom(){
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

    //this sends out a ResultSet for the charityRecords.
    public ResultSet displayCharityRecords(){
        try {
            String displayCharityRecordsString = "SELECT * FROM charityRecords";
            psDisplayCharityRecords = conn.prepareStatement(displayCharityRecordsString);
            allStatements.add(psDisplayCharityRecords);
            rspsDisplayCharityRecords = statement.executeQuery(displayCharityRecordsString);
        }
        catch(SQLException sqle){
            System.out.println("Could not fetch the consignors");
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return rspsDisplayCharityRecords;
        }
        return rspsDisplayCharityRecords;
    }

    public ResultSet displayMonthOldRecords(){
        try {
            java.sql.Date todaysDate = getDate();

            //from StackOverFlow http://stackoverflow.com/questions/16392892/how-to-reduce-one-month-from-current-date-and-stored-in-date-variable-using-java
            int month = Calendar.MONTH - 1;
            String displayConsignorsString = "SELECT * FROM recordsInMainRoom WHERE consignmentDate >= ";
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


    public java.sql.Date getDate(){
        Date date = new Date();
        String dateString = date.toString();
        String[] yearMonthDate = dateString.split("-");
        Integer year = Integer.parseInt(yearMonthDate[0]);
        Integer month = Integer.parseInt(yearMonthDate[1]);
        Integer currentDate = Integer.parseInt(yearMonthDate[2]);
        java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);
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

    public boolean recordSales(int recordID) {
        String addSoldRecord= "INSERT INTO soldRecords (recordID, consignorID, salesID, artist, title, price, consignmentDate, salesDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String addSale = "INSERT INTO sales (recordID, consignorID, salesDate, price, totalToStore, totalToConsignor) VALUES (?, ?, ?, ?, ?, ?)";

        String getSalesID = "SELECT salesID FROM sales WHERE recordID = ? VALUES (?)";

        //this method selects the record that is going to be sold.
        String table = "recordsInMainRoom";
        rs = selectRecord(table, recordID);

        //this try block puts a sale on the sales list.
        try {
            while(rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Float price = rs.getFloat("price");
                Float totalToConsignor = (price * Float.parseFloat(".4"));
                Float totalToStore = (price-totalToConsignor);

                java.sql.Date consignmentDate = rs.getDate("consignmentDate");

                Date date = new Date();
                String dateString = date.toString();
                String[] yearMonthDate = dateString.split("-");
                Integer year = Integer.parseInt(yearMonthDate[0]);
                Integer month = Integer.parseInt(yearMonthDate[1]);
                Integer currentDate = Integer.parseInt(yearMonthDate[2]);
                java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);

                psReturnRecord = conn.prepareStatement(addSale);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setDate(3, todaysDate);
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

                Date date = new Date();
                String dateString = date.toString();
                String[] yearMonthDate = dateString.split("-");
                Integer year = Integer.parseInt(yearMonthDate[0]);
                Integer month = Integer.parseInt(yearMonthDate[1]);
                Integer currentDate = Integer.parseInt(yearMonthDate[2]);
                java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);

                psReturnRecord = conn.prepareStatement(addSoldRecord);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setInt(3, salesID);
                psReturnRecord.setString(4, artist);
                psReturnRecord.setString(5, title);
                psReturnRecord.setFloat(6, price);
                psReturnRecord.setDate(7, consignmentDate);
                psReturnRecord.setDate(8, todaysDate);
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

                Date date = new Date();
                String dateString = date.toString();
                String[] yearMonthDate = dateString.split("-");
                Integer year = Integer.parseInt(yearMonthDate[0]);
                Integer month = Integer.parseInt(yearMonthDate[1]);
                Integer currentDate = Integer.parseInt(yearMonthDate[2]);
                java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);

                psReturnRecord = conn.prepareStatement(recordToCharity);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, todaysDate);
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

                Date date = new Date();
                String dateString = date.toString();
                String[] yearMonthDate = dateString.split("-");
                Integer year = Integer.parseInt(yearMonthDate[0]);
                Integer month = Integer.parseInt(yearMonthDate[1]);
                Integer currentDate = Integer.parseInt(yearMonthDate[2]);
                java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);

                psReturnRecord = conn.prepareStatement(addRecordToBasement);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, todaysDate);
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

                Date date = new Date();
                String dateString = date.toString();
                String[] yearMonthDate = dateString.split("-");
                Integer year = Integer.parseInt(yearMonthDate[0]);
                Integer month = Integer.parseInt(yearMonthDate[1]);
                Integer currentDate = Integer.parseInt(yearMonthDate[2]);
                java.sql.Date todaysDate = new java.sql.Date(year, month, currentDate);

                psReturnRecord = conn.prepareStatement(addReturnedRecord);
                allStatements.add(psReturnRecord);
                psReturnRecord.setInt(1, recordID);
                psReturnRecord.setInt(2, consignorID);
                psReturnRecord.setString(3, artist);
                psReturnRecord.setString(4, title);
                psReturnRecord.setFloat(5, price);
                psReturnRecord.setDate(6, consignmentDate);
                psReturnRecord.setDate(7, todaysDate);
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

    public Record buyRecord(int consignorID, String artist, String title, double price, String dateString){
        String addRecord = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (?, ?, ?, ?, ?)";
        Record newRecord = null;
        try {

            String[] yearMonthDate = dateString.split("-");
            Integer year = Integer.parseInt(yearMonthDate[0]);
            Integer month = Integer.parseInt(yearMonthDate[1]);
            Integer date = Integer.parseInt(yearMonthDate[2]);
            java.sql.Date todaysDate = new java.sql.Date(year, month, date);

            psAddRecord = conn.prepareStatement(addRecord);
            allStatements.add(psAddRecord);
            psAddRecord.setInt(1, consignorID);
            psAddRecord.setString(2, artist);
            psAddRecord.setString(3, title);
            psAddRecord.setDouble(4, price);
            psAddRecord.setDate(5, todaysDate);
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


    public LinkedList<Record> updateRecordsList() {

        LinkedList<Record> allRecords = new LinkedList<Record>();

        String displayRecordsInMainRoom = "SELECT * FROM recordsInMainRoom";
        try {
            rs = statement.executeQuery(displayRecordsInMainRoom);
        } catch (SQLException sqle) {
            System.err.println("Could not retrieve Records");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }

        //puts individual laptop information into strings.
        try {
            while (rs.next()) {

                int recordID = rs.getInt("recordID");
                int consignorID = rs.getInt("consignorID");
                String artist = rs.getString("artist");
                String title = rs.getString("title");
                Double price = rs.getDouble("price");
                Date consignmentDate = rs.getDate("consignmentDate");
                Record r = new Record(recordID, consignorID, artist, title, price, consignmentDate);
                allRecords.add(r);

            }
        } catch (SQLException sqle) {
            System.err.println("Error reading results.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;

        }
        //if we get here, everything should have worked...
        //Return the list of laptops, which will be empty if there is no data in the database
        return allRecords;
    }

    public LinkedList<Consignor> updateConsignorsList() {

        LinkedList<Consignor> allConsignors = new LinkedList<Consignor>();

        String displayAll = "SELECT * FROM consignors";
        try {
            rs = statement.executeQuery(displayAll);
        } catch (SQLException sqle) {
            System.err.println("Could not retrieve consignors.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }

        //puts individual laptop information into strings.
        try {
            while (rs.next()) {
                int consignorID = rs.getInt("consignorID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                BigDecimal phoneNumber = rs.getBigDecimal("phoneNumber");
                Double totalPaid = rs.getDouble("totalPaid");
                Consignor c = new Consignor(consignorID, firstName, lastName, address, city, state, phoneNumber, totalPaid);
                allConsignors.add(c);

            }
        } catch (SQLException sqle) {
            System.err.println("Error reading results.");
            //TODO delete these two lines (they give out too much information).
            System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
            sqle.printStackTrace();
            return null;
        }
        //if we get here, everything should have worked...
        //Return the list of laptops, which will be empty if there is no data in the database
        return allConsignors;
    }
}

        //changes the cellphone user.
        /*public boolean reassignCellphone(CellPhone cellPhone, String newuser){
            String reassignCellphone = "UPDATE cellphones SET staff = (?) WHERE id = (?)";
            try {
                psReassignCellphone = conn.prepareStatement(reassignCellphone);
                allStatements.add(psReassignCellphone);
                psReassignCellphone.setString(1, newuser);
                psReassignCellphone.setInt(2, cellPhone.id);
                psReassignCellphone.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }

        //updates the information on the laptop so that it is correct.
        public boolean reassignLaptop(Laptop laptop, String newuser){
            String reassignLaptop = "UPDATE laptops SET staff = (?) WHERE id = (?)";
            try {
                psReassignLaptop = conn.prepareStatement(reassignLaptop);
                allStatements.add(psReassignLaptop);
                psReassignLaptop.setString(1, newuser);
                psReassignLaptop.setInt(2, laptop.id);
                psReassignLaptop.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean deleteLaptop(Laptop laptop){
            String deleteLaptop = "DELETE FROM laptops WHERE id = (?)";

            //deletes the laptop with the chosen id from the table.
            try {
                psDelLaptop = conn.prepareStatement(deleteLaptop);
                allStatements.add(psDelLaptop);
                psDelLaptop.setInt(1, laptop.id);
                psDelLaptop.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean deleteCellphone(CellPhone cellPhone){
            String deleteCellphone = "DELETE FROM cellphones WHERE id = (?)";

            //finds the cellphone selected by the user and deletes it.
            try {
                psDelCellphone = conn.prepareStatement(deleteCellphone);
                allStatements.add(psDelCellphone);
                psDelCellphone.setInt(1, cellPhone.id);
                psDelCellphone.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean addCellphone(CellPhone cellPhone) {


            //Create SQL query to add this laptop info to DB

            String addLaptopSQLps = "INSERT INTO cellphones (make, model, staff) VALUES ( ? , ? , ?)" ;
            try {
                psAddLaptop = conn.prepareStatement(addLaptopSQLps);
                allStatements.add(psAddLaptop);
                psAddLaptop.setString(1, cellPhone.getCellPhoneMake());
                psAddLaptop.setString(2, cellPhone.getCellPhoneModel());
                psAddLaptop.setString(3, cellPhone.getCellPhoneStaff());
                psAddLaptop.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }

        //adds a laptop.
        public boolean addLaptop(Laptop laptop) {


            //Create SQL query to add this laptop info to DB

            String addLaptopSQLps = "INSERT INTO laptops (make, model, staff) VALUES ( ? , ? , ?)" ;

            //takes the information on the laptop and inserts it into table.
            try {
                psAddLaptop = conn.prepareStatement(addLaptopSQLps);
                allStatements.add(psAddLaptop);
                psAddLaptop.setString(1, laptop.getLapTopMake());
                psAddLaptop.setString(2, laptop.getLapTopModel());
                psAddLaptop.setString(3, laptop.getLapTopStaff());
                psAddLaptop.execute();
            }
            catch (SQLException sqle) {
                System.err.println("Error preparing statement or executing prepared statement to add laptop");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return false;
            }
            return true;
        }


         Returns null if any errors in fetching laptops
         *  Returnd empty list if no laptops in DB
         *

        public LinkedList<Laptop> displayAllLaptops() {

            LinkedList<Laptop> allLaptops = new LinkedList<Laptop>();

            String displayAll = "SELECT * FROM laptops";
            try {
                rs = statement.executeQuery(displayAll);
            }
            catch (SQLException sqle) {
                System.err.println("Error fetching all laptops");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return null;
            }

            //puts individual laptop information into strings.
            try {
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    String staff = rs.getString("staff");
                    Laptop l = new Laptop(id, make, model, staff);
                    allLaptops.add(l);

                }
            } catch (SQLException sqle) {
                System.err.println("Error reading from result set after fetching all laptop data");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return null;

            }

            //if we get here, everything should have worked...
            //Return the list of laptops, which will be empty if there is no data in the database
            return allLaptops;
        }

        //displays all the cellphones.
        public LinkedList<CellPhone> displayAllCellphones() {

            LinkedList<CellPhone> allCellphones = new LinkedList<CellPhone>();

            String displayAll = "SELECT * FROM cellphones";
            try {
                rs = statement.executeQuery(displayAll);
            }
            catch (SQLException sqle) {
                System.err.println("Error fetching all cellphones");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return null;
            }

            //puts all of the information about the cell phones into individual strings and outputs them.
            try {
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String make = rs.getString("make");
                    String model = rs.getString("model");
                    String staff = rs.getString("staff");
                    CellPhone c = new CellPhone(id, make, model, staff);
                    allCellphones.add(c);

                }
            } catch (SQLException sqle) {
                System.err.println("Error reading from result set after fetching all cellphone data");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return null;
            }

            //if we get here, everything should have worked...
            //Return the list of laptops, which will be empty if there is no data in the database
            return allCellphones;
        }

        //in theory this should pull the necessary information, but does not.
        public String staffSearch(String user){
            String staffSearchString = "SELECT laptops.*, cellphones.* FROM laptops CROSS JOIN cellphones WHERE laptops.staff = (?) OR cellphones.staff = (?)";

            try {
                psStaffSearch = conn.prepareStatement(staffSearchString);
                allStatements.add(psStaffSearch);
                psStaffSearch.setString(1, user);
                psStaffSearch.setString(2, user);
                psStaffSearch.execute();
            }

            catch (SQLException sqle) {
                System.err.println("Error: could not execute search.");
                System.out.println(sqle.getErrorCode() + " " + sqle.getMessage());
                sqle.printStackTrace();
                return null;
            }

            try {
                System.out.print(user + ": ");
                while(rs.next()){
                    System.out.println(rs.getString("make") + ", " + rs.getString("model"));
                }
                return "";
            }
            catch(NullPointerException npe){
                return "";
            }
            catch (SQLException sqle){
                System.out.println("Catch!");
                return "";
            }
        }
    }
}
*/
