package com.KevinMcClean;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kevin on 4/21/2015.
 */
//Show the recordsInMainRoom table.
public class RecordsInMainRoomGUI extends ConsignmentStoreViewer {
    private JPanel recordsGUIPanel;
    private JTable recordsTable;
    StoreTableModel stm;
    private JButton buyButton;
    private JButton exitButton;
    private JButton sellButton;
    private ConsignmentStoreController storeController;
    private ResultSet resultSet;
    Integer recordID;

    RecordsInMainRoomGUI(ConsignmentStoreController csc) {
        setContentPane(recordsGUIPanel);
        pack();
        setVisible(true);
        this.storeController = csc;

        //sets up the recordsTable for display.
        resultSet = displayRecordsInMainRoomViewer(storeController);
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

                int row = recordsTable.getSelectedRow();
                recordID = getID("RECORD_ID", row,recordsTable, resultSet);

                if (recordID == NOT_AN_INT)
                {
                    JOptionPane.showMessageDialog(recordsGUIPanel, "Could not sell this record. Please make sure you have selected an album.");
                }
                recordSaleViewer(recordID, storeController);
                resultSet = displayRecordsInMainRoomViewer(storeController);
                stm.updateResultSet(resultSet);
                recordsTable.setModel(stm);

            }
        });

        //closes the screen.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.mainRoomRecordsOpen = false;
                dispose();
            }
        });
    }
}
