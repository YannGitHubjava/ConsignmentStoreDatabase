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

    NewConsignorsGUI(ConsignmentStoreController csc){
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
                if (!firstNameText.isEmpty() && !lastNameText.isEmpty() && !address.isEmpty() && !city.isEmpty() && !state.isEmpty() && !phoneNumber.isEmpty() ){
                    Integer phoneInt;
                    try{
                        phoneInt = Integer.parseInt(phoneNumber);
                    }
                    catch(InputMismatchException ime){
                        JOptionPane.showMessageDialog(newConsignorsGUIPanel, "Phone number must be all integers, no dashes or letters.");
                        return;
                    }

                    newConsignorViewer(firstNameText, lastNameText, address, city, state, phoneInt);

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
                //TODO have this check to see if the user has filled out the form properly, and send it off to become an SQLQuery if they did.
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
