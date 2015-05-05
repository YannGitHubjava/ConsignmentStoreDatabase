package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */

//displays the record sold, who the consignor was, how much the consignor and the store made off the sale.
public class SoldRecordsGUI extends ConsignmentStoreViewer{

    private JTable soldRecordsTable;
    private JButton exitButton;
    private JPanel soldRecordsPanel;
    private ResultSet resultSet;
    private ConsignmentStoreController storeController;
    private StoreTableModel stm;
    SoldRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(soldRecordsPanel);
        pack();
        setVisible(true);

        //sets up the soldRecords table for GUI display.
        resultSet = displaySoldRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        soldRecordsTable.setModel(stm);
        soldRecordsTable.setGridColor(Color.BLACK);

        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }
}
