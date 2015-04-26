package com.KevinMcClean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 4/21/2015.
 */
public class NewConsignorsGUI extends ConsignmentStoreViewer{
    private JTextField nameTextField;
    private JTextField phoneNumberTextField;
    private JTextField addressTextField;
    private JButton addNewConsignorButton;
    private JButton exitButton;
    private JPanel newConsignorsGUIPanel;

    NewConsignorsGUI(ConsignmentStoreController csc){
        setContentPane(newConsignorsGUIPanel);
        pack();
        setVisible(true);
        addNewConsignorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameText = nameTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();
                String address = addressTextField.getText();
                if (!nameText.isEmpty() && !phoneNumber.isEmpty() && !address.isEmpty()){
                    //TODO set this so that it takes the information from the Strings and sends things off to be turned into a proper
                    nameTextField.setText(null);
                    phoneNumberTextField.setText(null);
                    addressTextField.setText(null);
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
                newConsignorsGUIPanel.removeAll();
                setVisible(false);
            }
        });
    }
}
