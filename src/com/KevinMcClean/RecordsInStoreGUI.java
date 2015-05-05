package com.KevinMcClean;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */
//Show the recordsInMainRoom table.
public class RecordsInStoreGUI extends ConsignmentStoreViewer {
    private JPanel recordsGUIPanel;
    private JTable recordsTable;
    StoreTableModel stm;
    private JButton buyButton;
    private JButton exitButton;
    private JButton sellButton;
    private ConsignmentStoreController storeController;
    private ResultSet resultSet;

    RecordsInStoreGUI(ConsignmentStoreController csc) {
        setContentPane(recordsGUIPanel);
        pack();
        setVisible(true);
        this.storeController = csc;

        //sets up the recordsTable for display.
        resultSet = displayRecordsinMainRoomViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        recordsTable.setModel(stm);
        recordsTable.setGridColor(Color.black);


        //goes to the BuyGUI.
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyGUI buyGUI = new BuyGUI(storeController);
            }
        });

        //sells a chosen record.
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordsTable.getColumnCount();
                int row = recordsTable.getSelectedRow();
                Object valueAt = recordsTable.getValueAt(row, 0);
                String valueString = valueAt.toString();
                System.out.println(valueString);
                //Integer recordID = Integer.parseInt(valueString);
                //recordSaleViewer(recordID);
            }
        });

        //closes the screen.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
