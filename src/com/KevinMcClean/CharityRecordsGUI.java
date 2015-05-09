package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */
public class CharityRecordsGUI extends ConsignmentStoreViewer{
    private JTable charityRecordsTable;
    private JPanel charityRecordsPanel;
    private JButton exitButton;
    private ResultSet resultSet;
    private ConsignmentStoreController storeController;
    private StoreTableModel stm;

    //this shows all the records that have been sent to a charity.
    CharityRecordsGUI(ConsignmentStoreController csc) {
        this.storeController = csc;
        setContentPane(charityRecordsPanel);
        pack();
        setVisible(true);


        resultSet = displayCharityRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        charityRecordsTable.setModel(stm);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.charityGUIOpen = false;
                dispose();
            }
        });
    }
}
