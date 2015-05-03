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
        } catch (Exception sqle) {

            System.err.println("Unable to add test data to database. Error message and stack trace follow");
            System.err.println(sqle.getMessage());
            sqle.printStackTrace();
            return;
        }
    }

    private void createConsignorTable() throws SQLException {
        String createConsignorsTableSQL = "CREATE TABLE consignors (consignorID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, firstName VARCHAR(25), lastName VARCHAR(25), address VARCHAR(50), city VARCHAR(50), state VARCHAR(2), phoneNumber BIGINT, totalPaid FLOAT)";
        String deleteTableSQL = "DROP TABLE consignors";

        try {
            statement.executeUpdate(createConsignorsTableSQL);
            System.out.println("Created recordsInStore table");
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

    private void createRecordsInMainRoomTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE recordsInMainRoom (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        String deleteTableSQL = "DROP TABLE recordsInMainRoom";

        try {
            statement.executeUpdate(createRecordsTableSQL);
            System.out.println("Created recordsInStore table");
        } catch (SQLException sqle) {
            //Seems the table already exists, or some other error has occurred.
            //Let's try to check if the DB exists already by checking the error code returned. If so, delete it and re-create it
            if (sqle.getSQLState().startsWith("X0")) {    //Error code for table already existing starts with XO
                try {
                    statement.executeUpdate(deleteTableSQL);
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted recordsInStore Table");
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

    private void createRecordsInBargainBasement() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE basementRecords (recordID INT, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE, basementDate DATE )";
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
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted basementRecords Table");
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

    private void createRecordsSoldTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE soldRecords (recordID INT, consignorID INT, salesID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE, salesDate DATE )";
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
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted soldRecords Table");
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

    private void createRecordsSentToCharityTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE charityRecords (recordID INT, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE, charityDate DATE )";
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
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted charityRecords Table");
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



    //creates the table of records that have been returned.
    private void createRecordsReturnedTable() throws SQLException {
        String createRecordsTableSQL = "CREATE TABLE returnedRecords (recordID INT, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE, returnedDate DATE )";
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
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted returnedRecords Table");
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
        String createRecordsTableSQL = "CREATE TABLE sales (salesID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, recordID INT, consignorID INT, saleDate DATE, price FLOAT, totalToStore FLOAT, totalToConsignor FLOAT)";
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
                    statement.executeUpdate(createRecordsTableSQL);
                    System.out.println("Deleted sales Table");
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
            String addRecord1 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Shivaree', 'Who''s Got Trouble?', 7.00, '2015-04-24')";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (1, 'Al Green', 'Greatest Hits', 9.99, '2015-03-24')";
            statement.executeUpdate(addRecord2);
            String addRecord3 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (3, 'B.B. King', 'Live At The Regal', 4.99, '2012-04-24')";
            statement.executeUpdate(addRecord3);
            String addRecord4 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (2, 'The Modern Lovers', 'The Modern Lovers', 14.99, '2015-04-24')";
            statement.executeUpdate(addRecord4);
            String addRecord5 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'Los Lobos', 'How Will The Wolf Survive?', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord5);
            String addRecord6 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Buzzcocks, The', 'Singles Going Steady', 6.99, '2013-04-24')";
            statement.executeUpdate(addRecord6);
            String addRecord7 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Jimi Hendrix Experience, The', 'Are You Experienced?', 9.99, '2015-01-20')";
            statement.executeUpdate(addRecord7);
            String addRecord8 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'Gayngs', 'Relayted', 2.99, '2015-04-24')";
            statement.executeUpdate(addRecord8);
            String addRecord9 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'John Lee Hooker', 'Live At Newport', 12.99, '2015-04-24')";
            statement.executeUpdate(addRecord9);
            String addRecord10 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'Johnny Cash', 'At San Quentin', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord10);
            String addRecord11 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Beatles, The', 'Please Please Me', 7.00, '2015-03-24')";
            statement.executeUpdate(addRecord11);
            String addRecord12 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (1, 'White Stripes, The', 'Elephant', 9.99, '2015-03-24')";
            statement.executeUpdate(addRecord12);
            String addRecord13 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (3, 'Sonic Youth', 'Daydream Nation', 8.99, '2014-08-24')";
            statement.executeUpdate(addRecord13);
            String addRecord14 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'The Modern Lovers', 'The Modern Lovers', 14.99, '2015-04-24')";
            statement.executeUpdate(addRecord14);
            String addRecord15 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'Nirvana', 'Nevermind', 8.99, '2015-04-01')";
            statement.executeUpdate(addRecord15);
            String addRecord16 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (5, 'Elvis Presley', 'The Top Ten Hits', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord16);
            String addRecord17 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Beatles, The', 'Rubber Soul', 12.99, '2015-04-25')";
            statement.executeUpdate(addRecord17);
            String addRecord18 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Tom Waits', 'Franks Wild Years', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord18);
            String addRecord19 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Pixies, The', 'Doolittle', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord19);
            String addRecord20 = "INSERT INTO recordsInMainRoom (consignorID, artist, title, price, consignmentDate) VALUES (4, 'Rolling Stones, The', 'Let It Bleed', 5.99, '2015-04-24')";
            statement.executeUpdate(addRecord20);


        } catch (SQLException sqle) {
            System.err.println("Unable to add test data, check validity of SQL statements?");
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
        //"CREATE TABLE recordsInStore (recordID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, consignorID INT, artist VARCHAR(50), title VARCHAR(50), price FLOAT, consignmentDate DATE)";
        try {
            String addRecord1 = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES ('Jen', 'Bigelow', '123 Maple St.', 'Minneapolis', 'MN', 6121234567, 0.0)";
            statement.executeUpdate(addRecord1);
            String addRecord2 = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES ('Ben', 'Laurie', '456 Adams St.', 'Minneapolis', 'MN', 6121111111, 100.0)";
            statement.executeUpdate(addRecord2);
            String addRecord3 = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES ('Rob', 'Brantseg', '789 Place St.', 'St. Paul', 'MN', 6519876543, 5.20)";
            statement.executeUpdate(addRecord3);
            String addRecord4 = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES ('Alex', 'Davy', '000 Nonexistent Dr.', 'Edina', 'MN', 612000000, 50.73)";
            statement.executeUpdate(addRecord4);
            String addRecord5 = "INSERT INTO consignors (firstName, lastName, address, city, state, phoneNumber, totalPaid) VALUES ('Lisa', 'Bloomberg', '123 Maple St.', 'Eau Claire', 'WI', 9529529520, 11.11)";
            statement.executeUpdate(addRecord5);
        } catch (SQLException sqle) {
            System.err.println("Unable to add test data, check validity of SQL statements?");
            System.err.println("Unable to create database. Error message and stack trace follow");
            System.err.println(sqle.getMessage() + " " + sqle.getErrorCode());
            sqle.printStackTrace();

            throw sqle;
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
}