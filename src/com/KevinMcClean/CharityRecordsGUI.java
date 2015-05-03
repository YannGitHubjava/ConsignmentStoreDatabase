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
    private ConsignmentStoreController myController;
    private StoreTableModel stm;

    CharityRecordsGUI(ConsignmentStoreController csc) {
        this.myController = csc;
        setContentPane(charityRecordsPanel);
        pack();
        setVisible(true);

        resultSet = displayCharityRecordsViewer(myController);
        stm = new StoreTableModel(myController, resultSet);
        charityRecordsTable.setModel(stm);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
