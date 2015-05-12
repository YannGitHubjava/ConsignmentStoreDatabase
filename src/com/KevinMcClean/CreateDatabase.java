package com.KevinMcClean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by Kevin on 4/26/2015.
 */
//This class is used to create the initial data to show how the database works. It is separate from the database itself, so...
    //...that the database can be created and updated like a database would IRL. It is based on the basic databases that are...
    //...in the various programs that Clara has demonstrated in class.
public class CreateDatabase {

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


    //actuall sets up the databases.
    public void setupDatabase() {
        try {
            createConnection();
        } catch (Exception e) {
            System.err.println("Unable to connect to database. Error message and stack trace follow");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        try {
            createRecordsInMainRoomTable();
            createConsignorTable();
            createSalesTable();
            createRecordsInBargainBasement();
            createRecordsSoldTable();
            createRecordsSentToCharityTable();
            createRecordsReturnedTable();
        } catch (SQLException sqle) {
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            return;
        }
        try {
            addRecordsInStoreTestData();
            addConsignorTestData();
            addBasementRecordsTestData();
            addCharityRecordsTestData();
            addReturnedRecordsTestData();
            addSoldRecordsTestData();
            addSalesRecordsTestData();
        } catch (Exception sqle) {

            System.err.println("Unable to add test data to database. Error message and stack trace follow");
            System.err.println(sqle.getMessage());
            sqle.printStackTrace();
            return;
        }
        cleanup();
    }

//creates the table that holds the list of consignors.
    private void createConsignorTable() throws SQLException {
        String createConsignorsTableSQL = "CREATE TABLE consignors (consignor_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, first_name VARCHAR(25), last_name VARCHAR(25), address VARCHAR(50), city VARCHAR(50), state VARCHAR(2), phone_number VARCHAR(20), amount_owed FLOAT, total_paid FLOAT)";
        String deleteTableSQL = "DROP TABLE consignors";

        try {
            statement.executeUpdate(createConsignorsTableSQL);
            System.out.println("Created consignors table.");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted consignors Table");

                    statement.executeUpdate(createConsignorsTableSQL);
                    System.out.println("Created consignors table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //creates the records that are in the main room of the business.
    private void createRecordsInMainRoomTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE mainRoomRecords (record_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignor_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE)";
        String deleteTableSQL = "DROP TABLE mainRoomRecords";
        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created recordsInMainRoom table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted recordsInStore Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created recordsInStore table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //these tracks the records that are in the bargain basement.
    private void createRecordsInBargainBasement() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE basementRecords (record_id INT, consignor_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE, basement_date DATE )";
        String deleteTableSQL = "DROP TABLE basementRecords";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created basementRecords table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted basementRecords Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created basementRecords table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //this tracks the records that have been sold.
    private void createRecordsSoldTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE soldRecords (record_id INT, consignor_id INT, sales_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE, date_sold DATE )";
        String deleteTableSQL = "DROP TABLE soldRecords";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created soldRecords table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted soldRecords Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created soldRecords table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //this is where the records that have been given to charity are tracked.
    private void createRecordsSentToCharityTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE charityRecords (record_id INT, consignor_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE, charity_date DATE )";
        String deleteTableSQL = "DROP TABLE charityRecords";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created charityRecords table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted charityRecords Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created charityRecords table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //creates the table of records that have been returned to the owner.
    private void createRecordsReturnedTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE returnedRecords (record_id INT, consignor_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE, returned_date DATE )";
        String deleteTableSQL = "DROP TABLE returnedRecords";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created returnedRecords table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted returnedRecords Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created returnedRecords table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //creates the table that tracks the sales that have occurred.
    private void createSalesTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE sales (sales_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, record_id INT, consignor_id INT, sale_date DATE, price FLOAT, total_to_Store FLOAT, total_to_consignor FLOAT)";
        String deleteTableSQL = "DROP TABLE sales";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created sales table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    System.out.println("Deleted sales Table");

                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Created sales table");
                } catch (SQLException e) {
                    //Still doesn't work. Throw the exception.
                    throw e;
                }
            } else {
                //do nothing - if the table exists, leave it be.
            }
        }
    }

    //this adds records to the record store database so there's stuff to work with.
    private void addRecordsInStoreTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        //"CREATE TABLE recordsInStore (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        try {
            String addRecord1 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Shivaree', 'Who''s Got Trouble?', 7.00, '2015-04-24')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (1, 'Al Green', 'Greatest Hits', 9.99, '2015-03-24')";
            statement.executeUpdate(addRecord2);
            String addRecord3 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (3, 'B.B. King', 'Live At The Regal', 4.99, '2012-04-24')";
            statement.executeUpdate(addRecord3);
            String addRecord4 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (2, 'Modern Lovers, The', 'The Modern Lovers', 14.99, '2015-04-24')";
            statement.executeUpdate(addRecord4);
            String addRecord5 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'Los Lobos', 'How Will The Wolf Survive?', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord5);
            String addRecord6 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Buzzcocks, The', 'Singles Going Steady', 6.99, '2013-04-24')";
            statement.executeUpdate(addRecord6);
            String addRecord7 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Jimi Hendrix Experience, The', 'Are You Experienced?', 9.99, '2015-01-20')";
            statement.executeUpdate(addRecord7);
            String addRecord8 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'Gayngs', 'Relayted', 2.99, '2015-04-24')";
            statement.executeUpdate(addRecord8);
            String addRecord9 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'John Lee Hooker', 'Live At Newport', 12.99, '2015-04-24')";
            statement.executeUpdate(addRecord9);
            String addRecord10 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'Johnny Cash', 'At San Quentin', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord10);
            String addRecord11 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Beatles, The', 'Please Please Me', 7.00, '2015-03-24')";
            statement.executeUpdate(addRecord11);
            String addRecord12 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (1, 'White Stripes, The', 'Elephant', 9.99, '2015-03-24')";
            statement.executeUpdate(addRecord12);
            String addRecord13 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (3, 'Sonic Youth', 'Daydream Nation', 8.99, '2014-08-24')";
            statement.executeUpdate(addRecord13);
            String addRecord14 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Modern Lovers, The', 'The Modern Lovers', 14.99, '2015-04-24')";
            statement.executeUpdate(addRecord14);
            String addRecord15 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'Nirvana', 'Nevermind', 8.99, '2015-04-01')";
            statement.executeUpdate(addRecord15);
            String addRecord16 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (5, 'Elvis Presley', 'The Top Ten Hits', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord16);
            String addRecord17 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Beatles, The', 'Rubber Soul', 12.99, '2015-04-25')";
            statement.executeUpdate(addRecord17);
            String addRecord18 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Tom Waits', 'Franks Wild Years', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord18);
            String addRecord19 = "INSERT INTO mainRoomRecords(consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Pixies, The', 'Doolittle', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord19);
            String addRecord20 = "INSERT INTO mainRoomRecords (consignor_iD, artist, title, price, consignment_date) VALUES (4, 'Rolling Stones, The', 'Let It Bleed', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord20);


        } catch (SQLException sqle) {
            System.err.println("Unable to add test data, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();

            throw sqle;
        }
    }

    //adds test data to the basementRecords table.
    private void addBasementRecordsTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        //"CREATE TABLE recordsInStore (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        try {
            String addRecord1 = "INSERT INTO basementRecords (record_id, consignor_iD, artist, title, price, consignment_date, basement_date) VALUES (71, 4, 'Shivaree', 'Who''s Got Trouble?', 1.00, '2014-08-24', '2014-09-25')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO basementRecords (record_id, consignor_iD, artist, title, price, consignment_date, basement_date) VALUES (61, 1, 'Al Green', 'Greatest Hits', 1.00, '2015-03-24', '2015-03-26')";
            statement.executeUpdate(addRecord2);
            String addRecord3 = "INSERT INTO basementRecords (record_id, consignor_iD, artist, title, price, consignment_date, basement_date) VALUES (51, 4, 'Rolling Stones, The', 'Let It Bleed', 1.00, '2013-04-24', '2014-04-30')";
            statement.executeUpdate(addRecord3);

        } catch (SQLException sqle) {
            System.err.println("Unable to add test data for basementRecords, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            throw sqle;
        }
    }

    //adds test data to the charityRecords table.
    private void addCharityRecordsTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        //"CREATE TABLE recordsInStore (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        try {
            String addRecord1 = "INSERT INTO charityRecords (record_id, consignor_iD, artist, title, price, consignment_date, charity_date) VALUES (1, 4, 'Shivaree', 'Who''s Got Trouble?', 7.00, '2010-08-24', '2011-08-25')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO charityRecords (record_id, consignor_iD, artist, title, price, consignment_date, charity_date) VALUES (1, 1, 'Al Green', 'Greatest Hits', 9.99, '2014-03-20', '2015-03-24')";
            statement.executeUpdate(addRecord2);

        } catch (SQLException sqle) {
            System.err.println("Unable to add test data for charityRecords, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            throw sqle;
        }
    }

    //adds test data to the sales table.
    private void addSalesRecordsTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        try {
            String addRecord1 = "INSERT INTO sales (record_id, consignor_id, sale_date, price, total_to_Store, total_to_consignor) VALUES (99, 5, '2010-09-25', 7.00, 4.20, 2.80)";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO sales (record_id, consignor_id, sale_date, price, total_to_Store, total_to_consignor) VALUES (98, 2, '2014-04-24', 9.99, 5.99, 4.00)";
            statement.executeUpdate(addRecord2);

        } catch (SQLException sqle) {
            System.err.println("Unable to add test data for soldRecords table, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            throw sqle;
        }
    }

    //adds test data to the soldRecords table.
    private void addSoldRecordsTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        //createRecordsTableSQL = "CREATE TABLE soldRecords (record_id INT, consignor_id INT, sales_id INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignment_date DATE, sales_date DATE )";
        try {
            String addRecord1 = "INSERT INTO soldRecords (record_id, consignor_id, sales_id, artist, title, price, consignment_date, date_sold) VALUES (99, 5, 1, 'Shivaree', 'Who''s Got Trouble?', 7.00, '2010-08-24', '2010-09-25')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO soldRecords (record_id, consignor_id, sales_id, artist, title, price, consignment_date, date_sold) VALUES (98, 2, 2, 'Al Green', 'Greatest Hits', 9.99, '2014-03-20', '2014-04-24')";
            statement.executeUpdate(addRecord2);

        } catch (SQLException sqle) {
            System.err.println("Unable to add test data for soldRecords table, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            throw sqle;
        }
    }

    //adds test data to the returnedRecords table.
    private void addReturnedRecordsTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        //"CREATE TABLE recordsInStore (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        try {
            String addRecord1 = "INSERT INTO returnedRecords (record_id, consignor_iD, artist, title, price, consignment_date, returned_date) VALUES (1, 4, 'Shivaree', 'Who''s Got Trouble?', 7.00, '2010-08-24', '2010-09-25')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO returnedRecords (record_id, consignor_iD, artist, title, price, consignment_date, returned_date) VALUES (1, 1, 'Al Green', 'Greatest Hits', 9.99, '2014-03-20', '2014-04-24')";
            statement.executeUpdate(addRecord2);

        } catch (SQLException sqle) {
            System.err.println("Unable to add test data for returnedRecords table, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();
            throw sqle;
        }
    }

    //this adds some data to the consignor database so there's stuff to work with.
    private void addConsignorTestData() throws Exception {
        // Test data.
        if (statement == null) {
            //This isn't going to work
            throw new Exception("Statement not initialized");
        }
        try {
            String addRecord1 = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES ('Jen', 'Bigelow', '123 Maple St.', 'Minneapolis', 'MN', '6121234567', 0.0, 0.0)";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES ('Ben', 'Laurie', '456 Adams St.', 'Minneapolis', 'MN', '6121111111', 4.00, 0.00)";
            statement.executeUpdate(addRecord2);
            String addRecord3 = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES ('Rob', 'Brantseg', '789 Place St.', 'St. Paul', 'MN', '6519876543', 0.0, 0.0)";
            statement.executeUpdate(addRecord3);
            String addRecord4 = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES ('Alex', 'Davy', '000 Nonexistent Dr.', 'Edina', 'MN', '612000000', 0.0, 0.0)";
            statement.executeUpdate(addRecord4);
            String addRecord5 = "INSERT INTO consignors (first_name, last_name, address, city, state, phone_number, amount_owed, total_paid) VALUES ('Lisa', 'Bloomberg', '123 Maple St.', 'Eau Claire', 'WI', '9529529520', 0.0, 2.80)";
            statement.executeUpdate(addRecord5);
        } catch (SQLException sqle) {
            System.err.println("Unable to add test data, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();

            throw sqle;
        }
    }

    //sets up the connection to the database.
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

    //from Clara's database programs. Closes out the ResultSets and PreparedStatements.
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
}