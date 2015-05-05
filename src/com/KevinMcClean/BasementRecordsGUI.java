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
    private JTable basementRecordsTable;
    private ResultSet resultSet;
    private ConsignmentStoreController storeController;
    private StoreTableModel stm;

    //This shows all the records that are in the basementRecords table.
    BasementRecordsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(basementRecordsTable);
        pack();
        setVisible(true);

        resultSet = storeController.requestDisplayBasementRecords();
        stm = new StoreTableModel(storeController, resultSet);
        basementRecordsTable.setModel(stm);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }
}
