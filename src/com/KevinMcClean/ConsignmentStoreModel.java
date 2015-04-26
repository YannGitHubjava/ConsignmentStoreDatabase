package com.KevinMcClean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    LinkedList<Statement> allStatements = new LinkedList<Statement>();



    public ConsignmentStoreModel() {
        try{
            createConnection();
        }
        catch (Exception e){
            System.out.println("Could not create a connection.");
        }
    }



    private void createConnection() throws Exception {

        try {
            conn = DriverManager.getConnection(protocol + dbName + ";create=true", USER, PASS);
            statement = conn.createStatement();
            allStatements.add(statement);
        } catch (Exception e) {
            //There are a lot of things that could go wrong here. Should probably handle them all separately but have not done so here.
            //Should put something more helpful here...
            throw e;
        }
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

    public LinkedList<Record> displayAllRecords() {

        LinkedList<Record> allRecords = new LinkedList<Record>();

        String displayAll = "SELECT * FROM recordsInStore";
        try {
            rs = statement.executeQuery(displayAll);
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

    public LinkedList<Consignor> displayAllConsignors() {

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
