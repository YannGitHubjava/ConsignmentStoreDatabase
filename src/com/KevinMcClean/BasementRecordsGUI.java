package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 5/4/2015.
 */
public class BasementRecordsGUI extends ConsignmentStoreViewer{
    private JButton exitButton;
    private JButton sellButton;

    private JTable basementRecordsTable;

    private JPanel basementRecordsGUIPanel;

    private ResultSet resultSet;

    private ConsignmentStoreController storeController;

    private StoreTableModel stm;

    Integer recordID;

    //This shows all the records that are in the basementRecords table.
    BasementRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(basementRecordsGUIPanel);
        pack();
        setVisible(true);
        //setDefaultCloseOperation(closeOperation());

        resultSet = displayBasementRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        basementRecordsTable.setModel(stm);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.basementRecordsOpen = false;
                dispose();
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = basementRecordsTable.getSelectedRow();
                recordID = getID("RECORD_ID", row,basementRecordsTable, resultSet);
                if (recordID == NOT_AN_INT)
                {
                    JOptionPane.showMessageDialog(basementRecordsTable, "Could not sell this record. Please make sure you have selected an album.");
                }
                boolean basementSale = recordBasementSaleViewer(recordID, storeController);
                if(basementSale){
                    JOptionPane.showMessageDialog(basementRecordsGUIPanel, "Record sold.");
                }
                else{
                    JOptionPane.showMessageDialog(basementRecordsGUIPanel, "Record not sold.");
                }
                resultSet = displayRecordsInMainRoomViewer(storeController);
                stm.updateResultSet(resultSet);
                basementRecordsTable.setModel(stm);
            }
        });

    }
}
