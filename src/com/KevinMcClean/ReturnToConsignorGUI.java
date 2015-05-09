package com.KevinMcClean;

import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 5/8/2015.
 */
public class ReturnToConsignorGUI extends ConsignmentStoreViewer{
    private JButton exitButton;
    private JTable returnedRecordsTable;
    private JPanel returnToConsignorGUIPanel;
    private ConsignmentStoreController storeController;
    private ResultSet resultSet;
    private StoreTableModel stm;

    ReturnToConsignorGUI(ConsignmentStoreController csc){
        setContentPane(returnToConsignorGUIPanel);
        pack();
        setVisible(true);
        this.storeController = csc;

        //sets up the recordsTable for display.
        resultSet = displayReturnedRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        returnedRecordsTable.setModel(stm);
        returnedRecordsTable.setGridColor(Color.black);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.returnedRecordsGUIOpen = false;
                dispose();
            }
        });
    }
}
