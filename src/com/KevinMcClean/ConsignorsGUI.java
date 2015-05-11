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
//this shows all the consignors that have placed stuff in the store.
public class ConsignorsGUI extends ConsignmentStoreViewer{
    private JTable consignorsTable;
    private JButton newConsignorsButton;
    private JButton exitButton;
    private JPanel consignorsGUIPanel;
    private ResultSet resultSet;
    private StoreTableModel stm;
    private ConsignmentStoreController storeController;


    ConsignorsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(consignorsGUIPanel);
        pack();
        setVisible(true);

        //sets the consignorsTable to show that the consignors.
        resultSet = displayConsignorsViewer(storeController);
        stm = new StoreTableModel(storeController, resultSet);
        consignorsTable.setModel(stm);


        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.consignorsGUIOpen= false;
                dispose();
            }
        });
        //Goes to the newConsignorsGUI GUI.
        newConsignorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ConsignmentStoreViewerGUI.newConsignorsGUIOpen) {
                    ConsignmentStoreViewerGUI.newConsignorsGUIOpen = true;
                    NewConsignorsGUI newConsignorsGUI = new NewConsignorsGUI(storeController);
                }
            }
        });
        //closes the screen.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsignmentStoreViewerGUI.consignorsGUIOpen = false;
                dispose();
            }
        });
    }
}