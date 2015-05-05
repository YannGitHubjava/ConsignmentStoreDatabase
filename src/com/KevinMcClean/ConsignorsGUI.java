package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */
//this shows all the consignors that have placed stuff in the store.
public class ConsignorsGUI extends ConsignmentStoreViewer{
    private JTable consignorsTable;
    private JButton newConsignorsButton;
    private JButton exitButton;
    private JPanel consignorsGUIPanel;
    private ResultSet resultSet;
    private StoreTableModel stm;


    ConsignorsGUI(ConsignmentStoreController csc){
        this.myController = csc;
        setContentPane(consignorsGUIPanel);
        pack();
        setVisible(true);

        //sets the consignorsTable to show that the consignors.
        resultSet = displayConsignorsViewer(myController);
        stm = new StoreTableModel(myController, resultSet);
        consignorsTable.setModel(stm);

        //Goes to the newConsignorsGUI GUI.
        newConsignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewConsignorsGUI newConsignorsGUI = new NewConsignorsGUI(myController);
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