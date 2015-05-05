package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Created by Kevin on 4/21/2015.
 */
public class BuyGUI extends ConsignmentStoreViewer{
    private JPanel buyGUIPanel;
    private JTable consignorsTable;
    private JTextField artistTextField;
    private JTextField priceTextField;
    private JTextField albumTitleTextField;
    private JButton exitButton;
    private JButton purchaseButton;
    private int consignorID;
    private ResultSet resultSet;
    private StoreTableModel stm;
    private ConsignmentStoreController storeController;

    //this is where the user can buy records from the consignor.
    BuyGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(buyGUIPanel);
        pack();
        setVisible(true);


        resultSet = displayConsignorsViewer(storeController);
        stm = new StoreTableModel(myController, resultSet);
        consignorsTable.setModel(stm);


        //the purchase Button is used to buy records once the fields have been filled out.
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String artistText = artistTextField.getText();
                String priceText = priceTextField.getText();
                String albumTitle = albumTitleTextField.getText();
                if (!artistText.isEmpty()&& !priceText.isEmpty() && !albumTitle.isEmpty()){
                    buyRecordsViewer(artistText, albumTitle, priceText, consignorID);
                    //TODO have this information turned into a relevant string for use by the database.
                    artistTextField.setText(null);
                    priceTextField.setText(null);
                    albumTitleTextField.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(buyGUIPanel, "You must fill out all the fields for purchase.");
                }
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