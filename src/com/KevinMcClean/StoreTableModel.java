package com.KevinMcClean;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Kevin on 4/26/2015.
 */
public class StoreTableModel extends AbstractTableModel{
    private int rowCount = 0;
    private int columnCount = 0;
    private ConsignmentStoreController storeController;
    ResultSet resultSet;

    //This takes the StoreTableModel and uses it to create tables used in the GUI.
    public StoreTableModel(ConsignmentStoreController csc, ResultSet rs) {
        this.storeController = csc;
        this.resultSet = rs;
        setup(this.resultSet);
    }

    //setup in the resultSet. Provided by Clara in movies table program.
    public void setup(ResultSet resultSet){
        countRows();
        try{
            columnCount = resultSet.getMetaData().getColumnCount();
        }
        catch(SQLException sqle){
            System.out.println("Couldn't count columns.");
        }
        this.rowCount = getRowCount();

    }

    //Updates the results after something has occurred. Provided by Clara in the movies table program.
    public void updateResultSet (ResultSet rs){
        this.resultSet = rs;
        setup(resultSet);
    }

    //countsRows in the resultSet. Provided by Clara in movies table program.
    public void countRows(){
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }
    }

    @Override
    public String getColumnName(int column) {
        try {
            String columnResult = resultSet.getMetaData().getColumnName(column + 1);
            String[] columnNameArray = columnResult.split("_");
            String columnName = "";
            for(String word: columnNameArray){
               columnName = columnName + " " + word;
            }

            columnName.trim();
            return columnName;
            //old way.
            //return resultSet.getMetaData().getColumnName(column + 1);

        }
        catch(SQLException sqle){
            System.out.println("Could not fetch column " + column + 1);
            return "?";
        }
    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try{
            //  System.out.println("get value at, row = " +row);
            resultSet.absolute(row+1);
            Object o = resultSet.getObject(col+1);
            return o.toString();
        }catch (SQLException se) {
            System.out.println(se);
            //se.printStackTrace();
            return se.toString();

        }
    }
}
