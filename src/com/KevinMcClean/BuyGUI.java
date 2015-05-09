package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    int row = consignorsTable.getSelectedRow();
                    int column = -1;
                    Object valueAt;
                    String valueString;
                    try {
                        column = (resultSet.findColumn("CONSIGNOR_ID")-1);
                        valueAt = consignorsTable.getValueAt(row, column);
                        valueString = valueAt.toString();
                        consignorID = Integer.parseInt(valueString);
                    }
                    catch (SQLException sqle){
                        JOptionPane.showMessageDialog(buyGUIPanel, "Could not fine the \"Consignor Id\" column.");
                        return;
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(buyGUIPanel, "Please select a consignor.");
                        return;
                    }

                    artistText = ivCheckNameForThe(artistText);
                    albumTitle = ivCheckNameForThe(albumTitle);

                    Double priceDouble = ivIsPriceDouble(priceText);
                    if(priceDouble == NOT_A_DOUBLE){
                        JOptionPane.showMessageDialog(buyGUIPanel, "This is not a price. Please try again.");
                        return;
                    }


                    buyRecordsViewer(artistText, albumTitle, priceDouble, consignorID, storeController);
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
                ConsignmentStoreViewerGUI.buyGUIOpen = false;
                dispose();
            }
        });
    }
}