package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        //sets up the table.
        resultSet = displayConsignorsViewer(storeController);
        stm = new StoreTableModel(myController, resultSet);
        consignorsTable.setModel(stm);

        //from http://stackoverflow.com/questions/4737495/disposing-and-closing-windows-in-java.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConsignmentStoreViewerGUI.buyGUIOpen = false;
                dispose();
            }
        });

        //the purchase Button is used to buy records once the fields have been filled out.
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String artistText = artistTextField.getText();
                String priceText = priceTextField.getText();
                String albumTitle = albumTitleTextField.getText();

                //this makes sure it only runs if all the fields have been filled out.
                if (!artistText.isEmpty()&& !priceText.isEmpty() && !albumTitle.isEmpty()){

                    int row = consignorsTable.getSelectedRow();

                    //checks to make sure that a consignor is selected.
                    boolean isRowSelected = ivIsRowSelected(row);
                    if(!isRowSelected){
                        JOptionPane.showMessageDialog(buyGUIPanel, "Please select a consignor.");
                        return;
                    }

                    consignorID = getID("CONSIGNOR_ID", row, consignorsTable, resultSet);

                    //run input validation on the title and artist name.
                    artistText = ivCheckNameForThe(artistText);
                    albumTitle = ivCheckNameForThe(albumTitle);

                    //checks to see if the price is a double.
                    Double priceDouble = ivIsPriceDouble(priceText);
                    if(priceDouble == NOT_A_DOUBLE){
                        JOptionPane.showMessageDialog(buyGUIPanel, "This is not a price. Please try again.");
                        return;
                    }

                    //checks to see if there are 5 copies in the store. Only allows 5 copies in the store.
                    boolean tooManyCopies = countSearchViewer(artistText, albumTitle, storeController);
                    if(!tooManyCopies){
                        JOptionPane.showMessageDialog(buyGUIPanel, "There are already 5 copies in the store. Cannot purchase another.");
                        return;
                    }

                    //attempts to buy the record chosen. Lets the user know if it failed.
                    boolean recordPurchased = buyRecordsViewer(artistText, albumTitle, priceDouble, consignorID, storeController);
                    if(recordPurchased){
                        JOptionPane.showMessageDialog(buyGUIPanel, "Record purchased.");
                    }
                    else{
                        JOptionPane.showMessageDialog(buyGUIPanel, "Could not purchase record.");
                        return;
                    }

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