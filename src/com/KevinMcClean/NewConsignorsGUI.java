package com.KevinMcClean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.InputMismatchException;

/**
 * Created by Kevin on 4/21/2015.
 */
//this GUI opens when someone needs to add a new consignor to the database.
public class NewConsignorsGUI extends ConsignmentStoreViewer{
    private JTextField firstNameTextField;
    private JTextField phoneNumberTextField;
    private JTextField addressTextField;
    private JButton addNewConsignorButton;
    private JButton exitButton;
    private JPanel newConsignorsGUIPanel;
    private JTextField lastNameTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private ConsignmentStoreController storeController;

    NewConsignorsGUI(ConsignmentStoreController csc){
        this.storeController = csc;
        setContentPane(newConsignorsGUIPanel);
        pack();
        setVisible(true);
        addNewConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstNameText = firstNameTextField.getText();
                String lastNameText = lastNameTextField.getText();
                String address = addressTextField.getText();
                String city = cityTextField.getText();
                String state = stateTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();
                //checks to make sure that all the fields have been filled in.
                if (!firstNameText.isEmpty() && !lastNameText.isEmpty() && !address.isEmpty() && !city.isEmpty() && !state.isEmpty() && !phoneNumber.isEmpty() ){
                    newConsignorViewer(firstNameText, lastNameText, address, city, state, phoneNumber, storeController);


                    firstNameTextField.setText(null);
                    lastNameTextField.setText(null);
                    addressTextField.setText(null);
                    cityTextField.setText(null);
                    stateTextField.setText(null);
                    phoneNumberTextField.setText(null);

                }
                else{
                    JOptionPane.showMessageDialog(newConsignorsGUIPanel, "You must fill out all the fields to add a consignor.");
                }
            }
        });
        //closes the GUI.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
