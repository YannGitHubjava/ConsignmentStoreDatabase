package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        //sets up the table.
        resultSet = displayCharityRecordsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        charityRecordsTable.setModel(stm);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.charityGUIOpen = false;
                dispose();
            }
        });

        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.charityGUIOpen = false;
                dispose();
            }
        });
    }
}
